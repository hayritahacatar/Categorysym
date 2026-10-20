package com.example.categorysym.ui.components.screens

import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.TextRange
import com.example.categorysym.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.categorysym.data.AppItem
import com.example.categorysym.data.DrawerPage
import com.example.categorysym.utils.*
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.categorysym.ui.MainViewModel
import kotlinx.coroutines.launch
import androidx.biometric.BiometricPrompt
import com.example.categorysym.data.LauncherDataStore
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.categorysym.ui.components.AppCardWithBackground
import androidx.biometric.BiometricManager.Authenticators
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.widget.Toast
import androidx.biometric.BiometricManager
import android.webkit.WebView
import androidx.compose.material.icons.filled.Settings

@Composable
fun DrawerScreen(viewModel: MainViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    
    val visibleCategoryNames = remember(uiState.enabledCategories, uiState.customCategories) {
        val allPossible = listOf(
            "Communicator", "Databank", "X (Devices)", "WWW", "Market", "Office", "Uncategorized", "System"
        ) + LauncherDataStore.OPTIONAL_CATEGORIES
        
        val enabledStandard = allPossible.filter { it in uiState.enabledCategories }
        val customNames = uiState.customCategories.map { it.name }
        
        enabledStandard + customNames
    }

    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = { uiState.pages.size + 2 }
    )

    // Create a map of states for each potential page to avoid sharing a single instance
    val listStates = remember { mutableStateMapOf<Int, LazyListState>() }
    
    val currentPage = pagerState.currentPage
    val isLauncherPage = currentPage in 1..uiState.pages.size
    val activeLauncherPage = if (isLauncherPage) currentPage else 1

    val currentListState = listStates.getOrPut(activeLauncherPage) {
        val existingState = listStates.values.firstOrNull()
        LazyListState(
            firstVisibleItemIndex = existingState?.firstVisibleItemIndex ?: 0,
            firstVisibleItemScrollOffset = existingState?.firstVisibleItemScrollOffset ?: 0
        )
    }
    
    if (isLauncherPage) {
        LaunchedEffect(currentListState.firstVisibleItemIndex, currentListState.firstVisibleItemScrollOffset) {
            listStates.forEach { entry ->
                val index = entry.key
                val state = entry.value
                if (index in 1..uiState.pages.size && index != activeLauncherPage) {
                    state.scrollToItem(
                        currentListState.firstVisibleItemIndex,
                        currentListState.firstVisibleItemScrollOffset
                    )
                }
            }
        }
    }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var isUnlocked by remember { mutableStateOf(false) }
    var isSearching by remember { mutableStateOf(false) }
    var showSettings by remember { mutableStateOf(false) }
    var feedWebView by remember { mutableStateOf<WebView?>(null) }

    // Lock the hidden section and clear search when swiped away from launcher pages
    LaunchedEffect(pagerState.currentPage) {
        val isLauncherPage = pagerState.currentPage in 1..uiState.pages.size
        
        if (!isLauncherPage) {
            // Left launcher pages, clear search
            if (isSearching) {
                viewModel.onSearchQueryChange("")
                isSearching = false
            }
            
            // If not on hidden page, lock it (though it locks anyway when swiped away)
            if (pagerState.currentPage != uiState.pages.size + 1) {
                isUnlocked = false
            }
        }
    }

    // Ensure we snap to page 1 once pages are loaded for the first time
    var hasSnappedToInitialPage by remember { mutableStateOf(false) }
    LaunchedEffect(uiState.pages) {
        if (uiState.pages.isNotEmpty() && !hasSnappedToInitialPage) {
            pagerState.scrollToPage(1)
            hasSnappedToInitialPage = true
        }
    }

    var lastTargetCategoryIndex by remember { mutableIntStateOf(-1) }

    // When search mode is opened, switch to page 1 immediately
    LaunchedEffect(isSearching) {
        if (isSearching) {
            if (pagerState.currentPage != 1) {
                pagerState.scrollToPage(1)
            }
            lastTargetCategoryIndex = -1
        }
    }

    // Auto-scroll to category ONLY when the target category changes, avoiding repeated scroll animations while typing
    LaunchedEffect(uiState.searchQuery) {
        if (uiState.searchQuery.isNotBlank()) {
            if (pagerState.currentPage != 1) {
                pagerState.scrollToPage(1)
            }

            val page1 = uiState.pages.firstOrNull()
            if (page1 != null) {
                val matchingSectionIndex = page1.sections.indexOfFirst { it.apps.isNotEmpty() }
                if (matchingSectionIndex >= 0 && matchingSectionIndex != lastTargetCategoryIndex) {
                    lastTargetCategoryIndex = matchingSectionIndex
                    val activeListState = listStates.getOrPut(1) { LazyListState(0, 0) }
                    val isVisible = activeListState.layoutInfo.visibleItemsInfo.any { it.index == matchingSectionIndex }
                    if (!isVisible) {
                        activeListState.animateScrollToItem(matchingSectionIndex)
                    }
                }
            }
        } else {
            lastTargetCategoryIndex = -1
        }
    }

    BackHandler {
        when {
            pagerState.currentPage == 0 && feedWebView?.canGoBack() == true -> {
                feedWebView?.goBack()
            }

            isSearching -> {
                viewModel.onSearchQueryChange("")
                isSearching = false
            }

            pagerState.currentPage != 1 -> {
                scope.launch {
                    pagerState.animateScrollToPage(1)
                }
            }

            else -> {
                // Launcher ana ekrandaysa geri tuşu hiçbir şey yapmasın
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F9))
            .safeDrawingPadding()
    ) {
        val isLauncherPage = pagerState.currentPage in 1..uiState.pages.size

        if (isLauncherPage) {
            if (isSearching) {
                SearchBar(
                    query = uiState.searchQuery,
                    onQueryChange = { viewModel.onSearchQueryChange(it) },
                    onClose = {
                        viewModel.onSearchQueryChange("")
                        isSearching = false
                    }
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { isSearching = true }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }

                    IconButton(onClick = { showSettings = true }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            }
        } else {
            // Spacer to keep layout consistent when search is hidden on non-launcher pages
            Spacer(modifier = Modifier.height(44.dp))
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            flingBehavior = PagerDefaults.flingBehavior(state = pagerState),
            pageSpacing = 8.dp
        ) { index ->
            when {
                index == 0 -> {
                    DiscoverScreen(onWebViewCreated = { feedWebView = it })
                }
                index <= uiState.pages.size -> {
                    val pageListState = listStates.getOrPut(index) { LazyListState(0, 0) }
                    DrawerPageScreen(
                        page = uiState.pages[index - 1],
                        categoryNames = visibleCategoryNames,
                        listState = pageListState,
                        onAppClick = { viewModel.incrementUsage(it.packageName) },
                        onCategoryMove = { app, category ->
                            viewModel.saveUserCategory(app.packageName, category)
                        },
                        onToggleHide = { viewModel.toggleHideApp(it.packageName) }
                    )
                }
                else -> {
                    HiddenAppsScreen(
                        apps = uiState.hiddenApps,
                        categoryNames = visibleCategoryNames,
                        isUnlocked = isUnlocked,
                        onUnlockRequest = {
                            val activity = context.findFragmentActivity()
                            if (activity != null) {
                                authenticate(activity) {
                                    isUnlocked = true
                                }
                            } else {
                                Log.e("DrawerScreen", "Could not find FragmentActivity")
                            }
                        },
                        onAppClick = { viewModel.incrementUsage(it.packageName) },
                        onToggleHide = { viewModel.toggleHideApp(it.packageName) }
                    )
                }
            }
        }

        PageIndicator(
            pageCount = uiState.pages.size + 2,
            currentPage = pagerState.currentPage
        )
    }

    if (showSettings) {
        CategorySettingsScreen(
            enabledCategories = uiState.enabledCategories,
            customCategories = uiState.customCategories,
            purchasedSlots = uiState.purchasedSlots,
            onToggleCategory = { viewModel.toggleCategory(it) },
            onPurchaseSlot = { viewModel.purchaseSlot() },
            onAddCustomCategory = { name, color, note, examples ->
                viewModel.addCustomCategory(name, color, note, examples)
            },
            onUpdateCustomCategory = { id, name, color, note, examples ->
                viewModel.updateCustomCategory(id, name, color, note, examples)
            },
            onDeleteCustomCategory = { viewModel.deleteCustomCategory(it) },
            onResetManualCategories = { viewModel.resetManualCategories() },
            onDismiss = { showSettings = false }
        )
    }

    if (uiState.isFirstRun) {
        OnboardingScreen(onDismiss = { viewModel.completeOnboarding() })
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClose: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = query,
                selection = TextRange(query.length)
            )
        )
    }

    LaunchedEffect(query) {
        if (query != textFieldValue.text) {
            textFieldValue = TextFieldValue(
                text = query,
                selection = TextRange(query.length)
            )
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    OutlinedTextField(
        value = textFieldValue,
        onValueChange = { newValue ->
            textFieldValue = newValue
            onQueryChange(newValue.text)
        },
        placeholder = {
            Text(
                text = stringResource(R.string.search_apps_placeholder),
                color = Color.Gray
            )
        },
        textStyle = LocalTextStyle.current.copy(
            color = Color.Black,
            fontSize = 14.sp
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search),
                tint = Color.Black
            )
        },
        trailingIcon = {
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.close),
                    tint = Color.Black
                )
            }
        },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .height(52.dp)
            .focusRequester(focusRequester),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            cursorColor = Color.Black,
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Gray
        )
    )
}

@Composable
fun PageIndicator(
    pageCount: Int,
    currentPage: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (pageCount <= 10) {
            repeat(maxOf(pageCount, 1)) { index ->
                PageIndicatorItem(index, pageCount, currentPage)
            }
        } else {
            // Smart pagination for > 10 pages
            val lastIndex = pageCount - 1

            // Always show News Feed (index 0)
            PageIndicatorItem(0, pageCount, currentPage)

            when {
                currentPage <= 3 -> {
                    // Near start: News 1 2 3 4 ... LastPageNum Lock
                    for (i in 1..4) {
                        PageIndicatorItem(i, pageCount, currentPage)
                    }
                    EllipsisItem()
                    PageIndicatorItem(lastIndex - 1, pageCount, currentPage)
                }
                currentPage >= lastIndex - 3 -> {
                    // Near end: News 1 ... Last-3 Last-2 Last-1 Lock
                    PageIndicatorItem(1, pageCount, currentPage)
                    EllipsisItem()
                    for (i in (lastIndex - 4) until lastIndex) {
                        PageIndicatorItem(i, pageCount, currentPage)
                    }
                }
                else -> {
                    // Middle: News 1 ... Current ... LastPageNum Lock
                    PageIndicatorItem(1, pageCount, currentPage)
                    EllipsisItem()
                    PageIndicatorItem(currentPage, pageCount, currentPage)
                    EllipsisItem()
                    PageIndicatorItem(lastIndex - 1, pageCount, currentPage)
                }
            }

            // Always show Hidden Apps (last index)
            PageIndicatorItem(lastIndex, pageCount, currentPage)
        }
    }
}

@Composable
private fun PageIndicatorItem(index: Int, pageCount: Int, currentPage: Int) {
    val selected = index == currentPage
    when {
        index == 0 -> {
            Icon(
                imageVector = Icons.Default.Newspaper,
                contentDescription = stringResource(R.string.news_feed),
                tint = if (selected) Color.Black else Color.Gray.copy(alpha = 0.5f),
                modifier = Modifier
                    .size(if (selected) 20.dp else 16.dp)
                    .padding(horizontal = 4.dp)
            )
        }
        index == pageCount - 1 -> {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = stringResource(R.string.hidden_apps),
                tint = if (selected) Color.Black else Color.Gray.copy(alpha = 0.5f),
                modifier = Modifier
                    .size(if (selected) 20.dp else 16.dp)
                    .padding(horizontal = 4.dp)
            )
        }
        else -> {
            Text(
                text = index.toString(),
                color = if (selected) Color.Black else Color.Gray.copy(alpha = 0.5f),
                fontSize = if (selected) 16.sp else 12.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .width(if (selected) 20.dp else 16.dp)
            )
        }
    }
}

@Composable
private fun EllipsisItem() {
    Text(
        text = "...",
        color = Color.Gray.copy(alpha = 0.5f),
        fontSize = 12.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 4.dp)
    )
}

@Composable
fun HiddenAppsScreen(
    apps: List<AppItem>,
    categoryNames: List<String>,
    isUnlocked: Boolean,
    onUnlockRequest: () -> Unit,
    onAppClick: (AppItem) -> Unit,
    onToggleHide: (AppItem) -> Unit
) {
    if (!isUnlocked) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onUnlockRequest) {
                    Text(stringResource(R.string.unlock_hidden_apps))
                }
            }
        }
    } else {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                text = stringResource(R.string.hidden_apps),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (apps.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_hidden_apps_yet),
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(apps) { app ->
                        AppCardWithBackground(
                            app = app,
                            color = Color.Gray,
                            categoryNames = categoryNames,
                            isMoveEnabled = false,
                            onAppClick = onAppClick,
                            onCategoryMove = { }, 
                            onToggleHide = { onToggleHide(app) }
                        )
                    }
                }
            }
        }
    }
}

private fun Context.findFragmentActivity(): FragmentActivity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is FragmentActivity) return context
        context = context.baseContext
    }
    return null
}

private fun authenticate(activity: FragmentActivity, onSuccess: () -> Unit) {
    val biometricManager = BiometricManager.from(activity)
    val authenticators = Authenticators.BIOMETRIC_STRONG or Authenticators.DEVICE_CREDENTIAL

    when (biometricManager.canAuthenticate(authenticators)) {
        BiometricManager.BIOMETRIC_SUCCESS -> {
            val executor = ContextCompat.getMainExecutor(activity)
            val biometricPrompt = BiometricPrompt(activity, executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        onSuccess()
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        // Show error unless user canceled
                        if (errorCode != BiometricPrompt.ERROR_USER_CANCELED) {
                            Toast.makeText(activity, activity.getString(R.string.auth_error, errString), Toast.LENGTH_SHORT).show()
                        }
                    }
                })

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(activity.getString(R.string.hidden_section_title))
                .setSubtitle(activity.getString(R.string.hidden_section_subtitle))
                .setAllowedAuthenticators(authenticators)
                .build()

            biometricPrompt.authenticate(promptInfo)
        }
        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
            // No security method enrolled
            Toast.makeText(
                activity,
                activity.getString(R.string.no_security_enrolled),
                Toast.LENGTH_LONG
            ).show()
        }
        else -> {
            // Feature not supported
            Toast.makeText(
                activity,
                activity.getString(R.string.device_not_supported),
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
