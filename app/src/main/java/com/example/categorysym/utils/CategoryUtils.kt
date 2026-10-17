package com.example.categorysym.utils

import com.example.categorysym.data.AppItem
import com.example.categorysym.data.Category
import com.example.categorysym.data.CustomCategory
import java.text.Collator
import java.util.Locale

fun categorize(
    apps: List<AppItem>,
    learnedCategories: Map<String, String> = emptyMap(),
    usages: Map<String, Int> = emptyMap(),
    enabledCategories: Set<String> = emptySet(),
    customCategories: List<CustomCategory> = emptyList()
): List<Category> {

    // Internal mutable lists for all 20 possible categories
    val communicator = mutableListOf<AppItem>()
    val databank = mutableListOf<AppItem>()
    val xDevices = mutableListOf<AppItem>()
    val www = mutableListOf<AppItem>()
    val market = mutableListOf<AppItem>()
    val office = mutableListOf<AppItem>()
    val system = mutableListOf<AppItem>()
    val uncategorized = mutableListOf<AppItem>()
    
    val games = mutableListOf<AppItem>()
    val entertainment = mutableListOf<AppItem>()
    val finance = mutableListOf<AppItem>()
    val reference = mutableListOf<AppItem>()
    val books = mutableListOf<AppItem>()
    val massMedia = mutableListOf<AppItem>()
    val education = mutableListOf<AppItem>()
    val business = mutableListOf<AppItem>()
    val socialMedia = mutableListOf<AppItem>()
    val health = mutableListOf<AppItem>()
    val bet = mutableListOf<AppItem>()
    val productivity = mutableListOf<AppItem>()
    
    // Dynamic maps for custom categories
    val customAppsMap = customCategories.associateBy({ it.name }, { mutableListOf<AppItem>() })

    fun isEnabled(name: String) = enabledCategories.contains(name)

    apps
        .filterNot { it.packageName == "com.example.categorysym" }
        .forEach { app ->
            val name = app.name.lowercase()
            val pkg = app.packageName.lowercase()

            val learnedCategory = learnedCategories[app.packageName]
            if (!learnedCategory.isNullOrBlank()) {
                // If the user manually moved it to a category that is currently disabled, 
                // we still respect the manual move but might need to show it in Unknown if we want strict enforcement.
                // However, usually manual move means "I want it here", so we'll treat it as a priority.
                when (learnedCategory) {
                    "Communicator" -> communicator.add(app)
                    "Databank" -> databank.add(app)
                    "X (Devices)" -> xDevices.add(app)
                    "WWW" -> www.add(app)
                    "Market" -> market.add(app)
                    "Office" -> office.add(app)
                    "System" -> system.add(app)
                    "Games" -> if (isEnabled("Games")) games.add(app) else uncategorized.add(app)
                    "Entertainment" -> if (isEnabled("Entertainment")) entertainment.add(app) else uncategorized.add(app)
                    "Finance" -> if (isEnabled("Finance")) finance.add(app) else uncategorized.add(app)
                    "Reference" -> if (isEnabled("Reference")) reference.add(app) else uncategorized.add(app)
                    "Books" -> if (isEnabled("Books")) books.add(app) else uncategorized.add(app)
                    "Mass Media" -> if (isEnabled("Mass Media")) massMedia.add(app) else uncategorized.add(app)
                    "Education" -> if (isEnabled("Education")) education.add(app) else uncategorized.add(app)
                    "Business" -> if (isEnabled("Business")) business.add(app) else uncategorized.add(app)
                    "Social Media" -> if (isEnabled("Social Media")) socialMedia.add(app) else uncategorized.add(app)
                    "Health" -> if (isEnabled("Health")) health.add(app) else uncategorized.add(app)
                    "Bet" -> if (isEnabled("Bet")) bet.add(app) else uncategorized.add(app)
                    "Productivity" -> if (isEnabled("Productivity")) productivity.add(app) else uncategorized.add(app)
                    else -> {
                        // Check if it's a custom category
                        val customList = customAppsMap[learnedCategory]
                        if (customList != null) {
                            customList.add(app)
                        } else {
                            uncategorized.add(app)
                        }
                    }
                }
                return@forEach
            }

            when {
                // 1. Communicator: Calls, Messaging, Communities, Teleconference
                pkg.contains("whatsapp") || pkg.contains("telegram") || pkg.contains("signal") ||
                pkg.contains("messaging") || pkg.contains("contacts") || pkg.contains("dialer") ||
                pkg.contains("phone") || pkg.contains("discord") || pkg.contains("slack") ||
                pkg.contains("viber") || pkg.contains("skype") || pkg.contains("wechat") ||
                pkg.contains("zoom") || pkg.contains("teams") || pkg.contains("meet") ||
                name.contains("message") || name.contains("call") ->
                    communicator.add(app)

                // 2. Databank: Traditional utilities (Calendar, Clock, Calculator, Voice Recorder, Notes, Translation, Dictionaries)
                pkg.contains("calendar") || pkg.contains("clock") || pkg.contains("calculator") ||
                pkg.contains("keep") || pkg.contains("notes") || pkg.contains("recorder") ||
                pkg.contains("voice") || pkg.contains("translate") || pkg.contains("tureng") ||
                name.contains("alarm") || name.contains("stopwatch") || name.contains("timer") ||
                name.contains("calculator") || name.contains("çeviri") || name.contains("translate") ||
                name.contains("tureng") ->
                    databank.add(app)

                // 3. X (Devices): Music Player, Camera, Navigation, Pegasus
                pkg.contains("camera") || pkg.contains("photos") || pkg.contains("gallery") ||
                pkg.contains("maps") || pkg.contains("navigation") || pkg.contains("waze") ||
                pkg.contains("moovit") || pkg.contains("music") || pkg.contains("player") ||
                pkg.contains("deezer") || pkg.contains("vlc") || pkg.contains("pegasus") ||
                pkg.contains("flypgs") || name.contains("walkman") || name.contains("ipod") ||
                name.contains("yt music") || name.contains("pegasus") || name.contains("harita") ->
                    xDevices.add(app)

                // 4. WWW: Web, Cloud (Drive, OneDrive, Dropbox), Torrent, Remote Desktop, VPN, Google App, Obilet
                pkg.contains("chrome") || pkg.contains("browser") || pkg.contains("firefox") ||
                pkg.contains("opera") || pkg.contains("edge") || pkg.contains("brave") ||
                pkg.contains("google.android.apps.docs") ||
                pkg.contains("googlequicksearchbox") ||
                pkg.contains("drive") || pkg.contains("cloud") || pkg.contains("onedrive") ||
                pkg.contains("dropbox") || pkg.contains("mega") || pkg.contains("torrent") ||
                pkg.contains("remote") || pkg.contains("anydesk") || pkg.contains("teamviewer") ||
                pkg.contains("vpn") || pkg.contains("gmail") || pkg.contains("mail") ||
                pkg.contains("email") || pkg.contains("outlook") || pkg.contains("obilet") ||
                name.contains("internet") || name.contains("email") || name.contains("mail") ||
                name.contains("obilet") ->
                    www.add(app)

                // 5. Market: Shopping, Food Delivery, Loyalty Apps, App Stores (Play Store, A101, Algida, Daha Daha, Erikli, Hopi, N11, letgo, etc.)
                pkg.contains("vending") || pkg.contains("play") || pkg.contains("store") ||
                pkg.contains("market") || pkg.contains("shop") || pkg.contains("shopping") ||
                pkg.contains("amazon") || pkg.contains("trendyol") || pkg.contains("hepsiburada") ||
                pkg.contains("temu") || pkg.contains("ebay") || pkg.contains("alibaba") ||
                pkg.contains("shein") || pkg.contains("delivery") || pkg.contains("getir") ||
                pkg.contains("yemeksepeti") || pkg.contains("mcdonalds") || pkg.contains("starbucks") ||
                pkg.contains("a101") || pkg.contains("algida") || pkg.contains("dahadaha") ||
                pkg.contains("erikli") || pkg.contains("hopi") || pkg.contains("letgo") ||
                pkg.contains("mediamarkt") || pkg.contains("n11") || pkg.contains("neapp") ||
                pkg.contains("nutella") || pkg.contains("pazarama") || pkg.contains("pttavm") ||
                pkg.contains("sahibinden") || pkg.contains("teknosa") || pkg.contains("tiklagelsin") ||
                pkg.contains("zubizu") || pkg.contains("kofteciyusuf") ||
                name.contains("a101") || name.contains("algida") || name.contains("daha daha") ||
                name.contains("dahadaha") || name.contains("erikli") || name.contains("hopi") ||
                name.contains("köfteci yusuf") || name.contains("kofteci yusuf") || name.contains("letgo") ||
                name.contains("mediamarkt") || name.contains("n11") || name.contains("ne'app") ||
                name.contains("neapp") || name.contains("nutella") || name.contains("pazarama") ||
                name.contains("pttavm") || name.contains("sahibinden") || name.contains("teknosa") ||
                name.contains("tıklagelsin") || name.contains("tiklagelsin") || name.contains("zubizu") ->
                    market.add(app)

                // 6. Office: Office suites, PDF, Scanners, Docs, Sheets, Slides
                pkg.contains("docs") || pkg.contains("sheets") || pkg.contains("slides") ||
                pkg.contains("office") || pkg.contains("word") || pkg.contains("excel") ||
                pkg.contains("powerpoint") || pkg.contains("pdf") || pkg.contains("adobe") ||
                pkg.contains("scanner") || pkg.contains("printer") || pkg.contains("print") ||
                name.contains("doküman") || name.contains("tablo") || name.contains("slayt") ->
                    office.add(app)

                // 8. System: Settings, Security, Benchmarking, Local Files (WinRAR), Ecosystem/Smart Home, Arctis, Speedtest
                pkg.contains("settings") || pkg.contains("system") || pkg.contains("security") ||
                pkg.contains("benchmark") || pkg.contains("antutu") || pkg.contains("geekbench") ||
                pkg.contains("files") || pkg.contains("filemanager") || pkg.contains("explorer") ||
                pkg.contains("documentsui") || pkg.contains("winrar") || pkg.contains("zip") ||
                pkg.contains("rar") || pkg.contains("huawei.ailife") || pkg.contains("smartthings") ||
                pkg.contains("xiaomi.smarthome") || pkg.contains("wearable") || pkg.contains("watch") ||
                pkg.contains("arctis") || pkg.contains("speedtest") || pkg.contains("ookla") ||
                name.contains("arctis") || name.contains("speedtest") ||
                (pkg.contains("health") && pkg.contains("gear")) || pkg.contains("sim") ||
                pkg.contains(".stk") || pkg.contains("carrier") || pkg.contains("update") ||
                name.contains("sim toolkit") || name == "files" || name == "my files" ->
                    system.add(app)

                // --- Optional Categories (Check if enabled) ---

                // 9. Games: Everything related to games (CoC, SimCity, Emulators, Angry Birds, Chess, Sudoku, etc.)
                (pkg.contains("game") || name.contains("game") || pkg.contains("roblox") ||
                pkg.contains("pubg") || pkg.contains("freefire") || pkg.contains("clashofclans") ||
                pkg.contains("dominations") || pkg.contains("simcity") || pkg.contains("emulator") ||
                pkg.contains("retroarch") || pkg.contains("citra") || pkg.contains("dolphin") ||
                pkg.contains("steam") || pkg.contains("battlenet") || pkg.contains("xbox") ||
                pkg.contains("backbone") || pkg.contains("angrybirds") || pkg.contains("doodlejump") ||
                pkg.contains("cuttherope") || pkg.contains("fcmobile") || pkg.contains("fifamobile") ||
                pkg.contains("chess") || pkg.contains("sudoku") || pkg.contains("templerun") ||
                name.contains("backbone") || name.contains("angry") || name.contains("doodle") ||
                name.contains("cut the rope") || name.contains("battle.net") || name.contains("fc mobile") ||
                name.contains("satranç") || name.contains("chess") || name.contains("sudoku") ||
                name.contains("temple") || name.contains("snake") || name.contains("arcanoid") ||
                name.contains("mario") || name.contains("prince of persia")) && isEnabled("Games") ->
                    games.add(app)

                // 10. Entertainment: Streaming (Netflix, Disney+, HBO Max), Tickets, Angel
                (pkg.contains("netflix") || pkg.contains("disney") || pkg.contains("hbo") ||
                pkg.contains("primevideo") || pkg.contains("twitch") || pkg.contains("ticket") ||
                pkg.contains("bilet") || pkg.contains("cinema") || pkg.contains("theater") ||
                pkg.contains("angel") || name.contains("angel") || name.contains("hbo")) && isEnabled("Entertainment") ->
                    entertainment.add(app)

                // 11. Finance: Banking (GarantiBBVA, İşCep, YapıKredi, TOM Bank), Markets, Crypto, BonusFlaş, DövisApp, İstanbulKart, iyzico, Maximum, Yanımda
                (pkg.contains("bank") || pkg.contains("finance") || pkg.contains("wallet") ||
                pkg.contains("pay") || pkg.contains("papara") || pkg.contains("revolut") ||
                pkg.contains("binance") || pkg.contains("coinbase") || pkg.contains("crypto") ||
                pkg.contains("trading") || pkg.contains("bonusflas") || pkg.contains("doviz") ||
                pkg.contains("garanti") || pkg.contains("bbva") || pkg.contains("isbank") ||
                pkg.contains("iscep") || pkg.contains("istanbulkart") || pkg.contains("belbim") ||
                pkg.contains("iyzico") || pkg.contains("maximum") || pkg.contains("yapikredi") ||
                pkg.contains("ykb") || pkg.contains("tombank") || pkg.contains("yanimda") ||
                name.contains("garanti") || name.contains("bbva") || name.contains("işcep") ||
                name.contains("iscep") || name.contains("istanbulkart") || name.contains("iyzico") ||
                name.contains("maximum") || name.contains("yapı kredi") || name.contains("yapi kredi") ||
                name.contains("hadi") || name.contains("tom bank") || name.contains("yanımda") ||
                name.contains("yanimda") || name.contains("bonus") || name.contains("bonusfla") ||
                name.contains("döviz") || name.contains("doviz")) && isEnabled("Finance") ->
                    finance.add(app)

                // 12. Reference: e-devlet, e-nabız, MHRS, ÖSYM, Passwords (KeePass), Authenticators
                (pkg.contains("tr.gov") || pkg.contains("e_devlet") || pkg.contains("enabiz") ||
                pkg.contains("mhrs") || pkg.contains("osym") || pkg.contains("keepass") ||
                pkg.contains("bitwarden") || pkg.contains("lastpass") || pkg.contains("authenticator") ||
                name.contains("authenticator") || name.contains("psref")) && isEnabled("Reference") ->
                    reference.add(app)

                // 13. Books: Dictionaries, E-books, Religious apps
                (pkg.contains("kindle") || pkg.contains("books") || pkg.contains("audible") ||
                pkg.contains("dictionary") || pkg.contains("sozluk") || pkg.contains("bible") ||
                pkg.contains("quran") || pkg.contains("kuran") || pkg.contains("namaz") ||
                pkg.contains("kible") || name.contains("read") || name.contains("book")) && isEnabled("Books") ->
                    books.add(app)

                // 14. Mass Media: Newspapers, TV Channels
                (pkg.contains("newspaper") || pkg.contains("gazete") || pkg.contains("haber") ||
                pkg.contains("news") || pkg.contains("tv") || pkg.contains("channel") ||
                name.contains("gazete") || name.contains("haber")) && isEnabled("Mass Media") ->
                    massMedia.add(app)

                // 15. Education: Learning, Dev Tools (GitHub, StackOverflow)
                (pkg.contains("udemy") || pkg.contains("btk") || pkg.contains("linkedin.learning") ||
                pkg.contains("github") || pkg.contains("stackoverflow") || pkg.contains("duolingo") ||
                pkg.contains("education") || name.contains("learn") || name.contains("akademi")) && isEnabled("Education") ->
                    education.add(app)

                // 16. Business: Job Search, Hiring (Eleman.net, İŞKUR Mobil)
                (pkg.contains("linkedin") || pkg.contains("kariyernet") || pkg.contains("indeed") ||
                pkg.contains("job") || pkg.contains("armut") || pkg.contains("eleman") ||
                pkg.contains("iskur") || name.contains("eleman") || name.contains("işkur") ||
                name.contains("iskur") || name.contains("career") || name.contains("hiring")) && isEnabled("Business") ->
                    business.add(app)

                // 17. Social Media: Instagram, Facebook, X, YouTube, Threads
                (pkg.contains("instagram") || pkg.contains("facebook") || pkg.contains("twitter") ||
                pkg.contains("threads") || pkg.contains("youtube") && !pkg.contains("studio") ||
                pkg.contains("tiktok") || pkg.contains("reddit") || pkg.contains("pinterest") ||
                name.contains("social") || name.contains("twitter")) && isEnabled("Social Media") ->
                    socialMedia.add(app)

                // 18. Health: Fitness, Activity Tracking, Cycle Tracking, General Health
                (pkg.contains("fitness") || pkg.contains("workout") || pkg.contains("pedometer") ||
                pkg.contains("cycle") || pkg.contains("period") || pkg.contains("health") ||
                name.contains("health") || name.contains("fitness")) && isEnabled("Health") ->
                    health.add(app)

                // 19. Bet: League Tracking, Betting, Gambling
                (pkg.contains("bet") || pkg.contains("lig") || pkg.contains("league") ||
                pkg.contains("mackolik") || pkg.contains("nesine") || pkg.contains("misli") ||
                pkg.contains("bilyoner") || name.contains("iddaa") || name.contains("bet")) && isEnabled("Bet") ->
                    bet.add(app)

                // 20. Productivity: Gen AI (ChatGPT, Gemini, Claude), Creative Tools, YT Studio
                (pkg.contains("chatgpt") || pkg.contains("openai") || pkg.contains("gemini") ||
                pkg.contains("claude") || pkg.contains("editor") || pkg.contains("editing") ||
                pkg.contains("drawing") || pkg.contains("sketch") || pkg.contains("studio") ||
                pkg.contains("canva") || pkg.contains("capcut") || pkg.contains("photoshop") ||
                name.contains("ai") || name.contains("agent")) && isEnabled("Productivity") ->
                    productivity.add(app)

                else -> uncategorized.add(app)
            }
        }

    // 🔤 alphabetic sorting
    val collator = Collator.getInstance(Locale.getDefault())

    fun smartSorted(list: List<AppItem>): List<AppItem> {
        val usedApps = list
            .filter { (usages[it.packageName] ?: 0) > 0 }
            .sortedWith(
                compareByDescending<AppItem> {
                    usages[it.packageName] ?: 0
                }.thenBy(collator) {
                    it.name.trim()
                }
            )
            .take(2)

        val remainingApps = list
            .filterNot { app -> usedApps.any { it.packageName == app.packageName } }
            .sortedWith(compareBy(collator) { it.name.trim() })

        return usedApps + remainingApps
    }

    val result = mutableListOf<Category>()
    
    // Always add Default Categories
    result.add(Category("Communicator", smartSorted(communicator), getCategoryColor("Communicator")))
    result.add(Category("Databank", smartSorted(databank), getCategoryColor("Databank")))
    result.add(Category("X (Devices)", smartSorted(xDevices), getCategoryColor("X (Devices)")))
    result.add(Category("WWW", smartSorted(www), getCategoryColor("WWW")))
    result.add(Category("Market", smartSorted(market), getCategoryColor("Market")))
    result.add(Category("Office", smartSorted(office), getCategoryColor("Office")))
    result.add(Category("Uncategorized", smartSorted(uncategorized), getCategoryColor("Uncategorized")))
    result.add(Category("System", smartSorted(system), getCategoryColor("System")))

    // Add Optional Categories if enabled in the new requested order
    if (isEnabled("Social Media")) result.add(Category("Social Media", smartSorted(socialMedia), getCategoryColor("Social Media")))
    if (isEnabled("Mass Media")) result.add(Category("Mass Media", smartSorted(massMedia), getCategoryColor("Mass Media")))
    if (isEnabled("Games")) result.add(Category("Games", smartSorted(games), getCategoryColor("Games")))
    if (isEnabled("Entertainment")) result.add(Category("Entertainment", smartSorted(entertainment), getCategoryColor("Entertainment")))
    if (isEnabled("Reference")) result.add(Category("Reference", smartSorted(reference), getCategoryColor("Reference")))
    if (isEnabled("Books")) result.add(Category("Books", smartSorted(books), getCategoryColor("Books")))
    if (isEnabled("Education")) result.add(Category("Education", smartSorted(education), getCategoryColor("Education")))
    if (isEnabled("Business")) result.add(Category("Business", smartSorted(business), getCategoryColor("Business")))
    if (isEnabled("Finance")) result.add(Category("Finance", smartSorted(finance), getCategoryColor("Finance")))
    if (isEnabled("Health")) result.add(Category("Health", smartSorted(health), getCategoryColor("Health")))
    if (isEnabled("Productivity")) result.add(Category("Productivity", smartSorted(productivity), getCategoryColor("Productivity")))
    if (isEnabled("Bet")) result.add(Category("Bet", smartSorted(bet), getCategoryColor("Bet")))

    // Add Custom Categories
    customCategories.forEach { custom ->
        val appsInCustom = customAppsMap[custom.name] ?: emptyList<AppItem>()
        result.add(Category(custom.name, smartSorted(appsInCustom), parseColor(custom.colorHex)))
    }

    return result
}
