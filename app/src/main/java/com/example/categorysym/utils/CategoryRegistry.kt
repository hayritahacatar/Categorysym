package com.example.categorysym.utils

data class CategoryInfo(
    val description: String,
    val examples: String
)

object CategoryRegistry {
    val infoMap = mapOf(
        "Communicator" to CategoryInfo(
            description = "The hub for human connection and real-time dialogue. It prioritizes direct communication and community bonding.",
            examples = "WhatsApp, Telegram, Discord, Signal, Phone, Contacts, Zoom, Teams."
        ),
        "Databank" to CategoryInfo(
            description = "Essential personal utilities derived from the heritage of mobile tools. These are the basic functions that transformed phones into personal digital assistants.",
            examples = "Calendar, Clock, Calculator, Voice Recorder, Keep, Notes."
        ),
        "X (Devices)" to CategoryInfo(
            description = "Digital replacements for standalone physical hardware. This category honors the devices that smartphones have successfully unified into a single experience.",
            examples = "Music Players (Walkman/iPod), Camera, Gallery, Google Maps, Yandex Maps, Moovit."
        ),
        "WWW" to CategoryInfo(
            description = "Your gateway to the global network and decentralized storage. It encompasses web exploration, cloud accessibility, and secure connections.",
            examples = "Chrome, Firefox, Google App, Google Drive, OneDrive, Dropbox, VPN, Email clients."
        ),
        "Market" to CategoryInfo(
            description = "The commerce and service hub. It represents the transition of the physical marketplace into a digital, on-demand ecosystem.",
            examples = "Play Store, Amazon, Temu, Getir, Yemeksepeti, Brand Loyalty Apps."
        ),
        "Office" to CategoryInfo(
            description = "Professional efficiency and document integrity. A workspace dedicated to formal creation, viewing, and management of business assets.",
            examples = "Word, Excel, Google Docs, PDF Viewers, Adobe Acrobat, Document Scanners."
        ),
        "Uncategorized" to CategoryInfo(
            description = "A temporary space for applications yet to be assigned a specific purpose within the launcher's organizational philosophy.",
            examples = "Newly installed apps, experimental tools."
        ),
        "System" to CategoryInfo(
            description = "The engine room and ecosystem manager. It controls the device's internal logic and the external hardware connected to your digital life.",
            examples = "Settings, Security, Local File Explorers (WinRAR), Smart Home (Huawei AI Life, SmartThings), Wearable Management."
        ),
        "Social Media" to CategoryInfo(
            description = "High-engagement community interaction and shared digital experiences. These platforms focus on social validation and content sharing.",
            examples = "YouTube, Instagram, Facebook, X (Twitter), Threads, Reddit."
        ),
        "Mass Media" to CategoryInfo(
            description = "Real-time global reporting and broadcast culture. It brings the traditional press and television into the palm of your hand.",
            examples = "Digital Newspapers, News Portals, TV Channels, Haberler, Gazeteler."
        ),
        "Games" to CategoryInfo(
            description = "The digital playground. Covers the entire spectrum of interactive entertainment, from nostalgic emulations to modern mobile masterpieces.",
            examples = "Steam, Xbox, PlayStation App, PSP/PS2 Emulators, Clash of Clans, SimCity, Roblox."
        ),
        "Entertainment" to CategoryInfo(
            description = "Passive media consumption and cultural access. It focuses on the enjoyment of art, cinema, and live events.",
            examples = "Netflix, Disney+, HBO Max, Amazon Prime Video, Cinema/Theater Tickets."
        ),
        "Finance" to CategoryInfo(
            description = "Personal and corporate wealth management. A secure environment for monitoring and managing your economic potential.",
            examples = "Banking Apps, Stock Market Trackers, Crypto Wallets, Binance, PayPal."
        ),
        "Reference" to CategoryInfo(
            description = "Critical information and official gateways. These are the trusted sources for administrative and security-critical data.",
            examples = "e-Devlet, e-Nabız, MHRS, ÖSYM AİS, Password Managers (KeePass, Bitwarden)."
        ),
        "Books" to CategoryInfo(
            description = "The literary and spiritual library. Dedicated to long-form reading, linguistic tools, and religious guidance.",
            examples = "Kindle, Google Books, Dictionaries, Namaz Vakti, Quran, Holy Bible."
        ),
        "Education" to CategoryInfo(
            description = "The journey of lifelong learning. It includes platforms that facilitate the acquisition of new skills and knowledge.",
            examples = "Udemy, BTK Akademi, LinkedIn Learning, StackOverflow, GitHub, Duolingo."
        ),
        "Business" to CategoryInfo(
            description = "Professional growth and service acquisition. It bridges the gap between labor supply and professional opportunities.",
            examples = "LinkedIn, Kariyer.net, Indeed, Armut, Professional Hiring Tools."
        ),
        "Health" to CategoryInfo(
            description = "Holistic well-being monitoring. It transforms the device into a companion for tracking physical and biological health.",
            examples = "Fitness Coaching, Activity Trackers, Cycle Trackers, Medical Records."
        ),
        "Bet" to CategoryInfo(
            description = "Strategic gaming and competitive league tracking. Focuses on the analysis and participation in sports-based prediction markets.",
            examples = "League Trackers (Maçkolik), Nesine, Misli, Betting Apps."
        ),
        "Productivity" to CategoryInfo(
            description = "The center for generative creation. It empowers the user to produce new value using AI and advanced editing tools.",
            examples = "ChatGPT, Gemini, Claude, Video/Photo Editors (CapCut, Canva), YT Studio, Sketching Tools."
        )
    )
}
