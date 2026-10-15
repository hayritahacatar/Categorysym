package com.example.categorysym.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.categorysym.data.AppItem
import com.example.categorysym.data.CustomCategory
import com.example.categorysym.data.DrawerPage
import com.example.categorysym.data.LauncherDataStore
import com.example.categorysym.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class LauncherUiState(
    val pages: List<DrawerPage> = emptyList(),
    val hiddenApps: List<AppItem> = emptyList(),
    val enabledCategories: Set<String> = emptySet(),
    val customCategories: List<CustomCategory> = emptyList(),
    val purchasedSlots: Int = 0,
    val isFirstRun: Boolean = false,
    val isLoading: Boolean = true,
    val searchQuery: String = "",
    val filteredApps: List<AppItem> = emptyList()
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = LauncherDataStore(application)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _allApps = getAppsFlow(application)
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    val uiState: StateFlow<LauncherUiState> = combine(
        _allApps,
        _searchQuery,
        dataStore.allUsagesFlow,
        dataStore.allCategoriesFlow,
        dataStore.hiddenPackagesFlow,
        dataStore.enabledCategoriesFlow,
        dataStore.isFirstRunFlow,
        dataStore.customCategoriesFlow,
        dataStore.purchasedSlotsFlow
    ) { args ->
        @Suppress("UNCHECKED_CAST")
        val apps = args[0] as List<AppItem>
        val query = args[1] as String
        @Suppress("UNCHECKED_CAST")
        val usages = args[2] as Map<String, Int>
        @Suppress("UNCHECKED_CAST")
        val learnedCategories = args[3] as Map<String, String>
        @Suppress("UNCHECKED_CAST")
        val hiddenPackages = args[4] as Set<String>
        @Suppress("UNCHECKED_CAST")
        val enabledCategories = args[5] as Set<String>
        val isFirstRun = args[6] as Boolean
        @Suppress("UNCHECKED_CAST")
        val customCategories = args[7] as List<CustomCategory>
        val purchasedSlots = args[8] as Int

        val filteredApps = if (query.isBlank()) {
            apps
        } else {
            apps.filter { it.name.contains(query, ignoreCase = true) }
        }

        val nonHiddenApps = filteredApps.filterNot { hiddenPackages.contains(it.packageName) }
        val hiddenAppsList = apps.filter { hiddenPackages.contains(it.packageName) }

        val categories = categorize(
            apps = nonHiddenApps,
            learnedCategories = learnedCategories,
            usages = usages,
            enabledCategories = enabledCategories,
            customCategories = customCategories
        )
        val pages = buildPages(categories)

        LauncherUiState(
            pages = pages,
            hiddenApps = hiddenAppsList,
            enabledCategories = enabledCategories,
            customCategories = customCategories,
            purchasedSlots = purchasedSlots,
            isFirstRun = isFirstRun,
            isLoading = apps.isEmpty(),
            searchQuery = query,
            filteredApps = filteredApps
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LauncherUiState()
    )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun incrementUsage(packageName: String) {
        viewModelScope.launch {
            dataStore.incrementUsage(packageName)
        }
    }

    fun saveUserCategory(packageName: String, categoryName: String) {
        viewModelScope.launch {
            dataStore.saveCategory(packageName, categoryName)
        }
    }

    fun resetManualCategories() {
        viewModelScope.launch {
            dataStore.clearManualCategories()
        }
    }

    fun toggleHideApp(packageName: String) {
        viewModelScope.launch {
            dataStore.toggleHideApp(packageName)
        }
    }

    fun toggleCategory(categoryName: String) {
        viewModelScope.launch {
            dataStore.toggleCategory(categoryName)
        }
    }

    fun purchaseSlot() {
        viewModelScope.launch {
            dataStore.purchaseSlot()
        }
    }

    fun addCustomCategory(name: String, color: String, note: String, examples: String = "") {
        viewModelScope.launch {
            val id = System.currentTimeMillis().toString()
            dataStore.addCustomCategory(CustomCategory(id, name, color, note, examples))
        }
    }

    fun updateCustomCategory(id: String, name: String, color: String, note: String, examples: String) {
        viewModelScope.launch {
            dataStore.updateCustomCategory(CustomCategory(id, name, color, note, examples))
        }
    }

    fun deleteCustomCategory(id: String) {
        viewModelScope.launch {
            dataStore.deleteCustomCategory(id)
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            dataStore.setFirstRunCompleted()
        }
    }
}
