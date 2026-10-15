package com.example.categorysym.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "launcher_prefs")

class LauncherDataStore(private val context: Context) {

    private val gson = Gson()

    // Usage counts: map package name to count
    fun getUsageFlow(packageName: String): Flow<Int> {
        return context.dataStore.data.map { preferences ->
            preferences[intKeyForUsage(packageName)] ?: 0
        }
    }

    suspend fun incrementUsage(packageName: String) {
        context.dataStore.edit { preferences ->
            val current = preferences[intKeyForUsage(packageName)] ?: 0
            preferences[intKeyForUsage(packageName)] = current + 1
        }
    }

    // Learned categories: map package name to category name
    fun getCategoryFlow(packageName: String): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[stringKeyForCategory(packageName)]
        }
    }

    suspend fun saveCategory(packageName: String, categoryName: String) {
        context.dataStore.edit { preferences ->
            preferences[stringKeyForCategory(packageName)] = categoryName
        }
    }

    suspend fun clearManualCategories() {
        context.dataStore.edit { preferences ->
            val keysToRemove = preferences.asMap().keys.filter { 
                it.name.startsWith("cat_") 
            }
            keysToRemove.forEach { preferences.remove(it) }
        }
    }

    // Helper to get all usages at once (useful for sorting)
    val allUsagesFlow: Flow<Map<String, Int>> = context.dataStore.data.map { preferences ->
        preferences.asMap().entries.asSequence()
            .filter { it.key.name.startsWith("usage_") }
            .associate { (it.key.name.removePrefix("usage_")) to (it.value as? Int ?: 0) }
    }

    // Helper to get all learned categories at once
    val allCategoriesFlow: Flow<Map<String, String>> = context.dataStore.data.map { preferences ->
        preferences.asMap().entries.asSequence()
            .filter { it.key.name.startsWith("cat_") }
            .associate { (it.key.name.removePrefix("cat_")) to (it.value as? String ?: "") }
    }

    // Hidden packages
    val hiddenPackagesFlow: Flow<Set<String>> = context.dataStore.data.map { preferences ->
        preferences[HIDDEN_PACKAGES_KEY] ?: emptySet()
    }

    suspend fun toggleHideApp(packageName: String) {
        context.dataStore.edit { preferences ->
            val current = preferences[HIDDEN_PACKAGES_KEY] ?: emptySet()
            if (current.contains(packageName)) {
                preferences[HIDDEN_PACKAGES_KEY] = current - packageName
            } else {
                preferences[HIDDEN_PACKAGES_KEY] = current + packageName
            }
        }
    }

    // Enabled categories (Optional ones)
    val enabledCategoriesFlow: Flow<Set<String>> = context.dataStore.data.map { preferences ->
        val saved = preferences[ENABLED_CATEGORIES_KEY] ?: emptySet()
        // Combine with always-enabled defaults
        DEFAULT_CATEGORIES + saved
    }

    suspend fun toggleCategory(categoryName: String) {
        context.dataStore.edit { preferences ->
            val current = preferences[ENABLED_CATEGORIES_KEY] ?: emptySet()
            if (current.contains(categoryName)) {
                preferences[ENABLED_CATEGORIES_KEY] = current - categoryName
            } else {
                preferences[ENABLED_CATEGORIES_KEY] = current + categoryName
            }
        }
    }

    // First Run flag
    val isFirstRunFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_FIRST_RUN_KEY] ?: true
    }

    suspend fun setFirstRunCompleted() {
        context.dataStore.edit { preferences ->
            preferences[IS_FIRST_RUN_KEY] = false
        }
    }

    // Custom Categories
    val customCategoriesFlow: Flow<List<CustomCategory>> = context.dataStore.data.map { preferences ->
        val json = preferences[CUSTOM_CATEGORIES_KEY] ?: ""
        if (json.isBlank()) emptyList()
        else {
            val type = object : TypeToken<List<CustomCategory>>() {}.type
            gson.fromJson(json, type)
        }
    }

    suspend fun addCustomCategory(category: CustomCategory) {
        context.dataStore.edit { preferences ->
            val json = preferences[CUSTOM_CATEGORIES_KEY] ?: ""
            val type = object : TypeToken<List<CustomCategory>>() {}.type
            val currentList = if (json.isBlank()) mutableListOf()
            else gson.fromJson<MutableList<CustomCategory>>(json, type)
            
            currentList.add(category)
            preferences[CUSTOM_CATEGORIES_KEY] = gson.toJson(currentList)
        }
    }

    suspend fun updateCustomCategory(category: CustomCategory) {
        context.dataStore.edit { preferences ->
            val json = preferences[CUSTOM_CATEGORIES_KEY] ?: ""
            val type = object : TypeToken<List<CustomCategory>>() {}.type
            val currentList = if (json.isBlank()) mutableListOf<CustomCategory>()
            else gson.fromJson<MutableList<CustomCategory>>(json, type)
            
            val index = currentList.indexOfFirst { it.id == category.id }
            if (index != -1) {
                currentList[index] = category
                preferences[CUSTOM_CATEGORIES_KEY] = gson.toJson(currentList)
            }
        }
    }

    suspend fun deleteCustomCategory(categoryId: String) {
        context.dataStore.edit { preferences ->
            val json = preferences[CUSTOM_CATEGORIES_KEY] ?: ""
            val type = object : TypeToken<List<CustomCategory>>() {}.type
            val currentList = if (json.isBlank()) mutableListOf<CustomCategory>()
            else gson.fromJson<MutableList<CustomCategory>>(json, type)
            
            currentList.removeAll { it.id == categoryId }
            preferences[CUSTOM_CATEGORIES_KEY] = gson.toJson(currentList)
        }
    }

    // Purchased Slots
    val purchasedSlotsFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[PURCHASED_SLOTS_KEY] ?: 0
    }

    suspend fun purchaseSlot() {
        context.dataStore.edit { preferences ->
            val current = preferences[PURCHASED_SLOTS_KEY] ?: 0
            if (current < 10) {
                preferences[PURCHASED_SLOTS_KEY] = current + 1
            }
        }
    }

    private fun intKeyForUsage(packageName: String) = intPreferencesKey("usage_$packageName")
    private fun stringKeyForCategory(packageName: String) = stringPreferencesKey("cat_$packageName")

    companion object {
        private val HIDDEN_PACKAGES_KEY = stringSetPreferencesKey("hidden_packages")
        private val ENABLED_CATEGORIES_KEY = stringSetPreferencesKey("enabled_categories")
        private val IS_FIRST_RUN_KEY = booleanPreferencesKey("is_first_run")
        private val CUSTOM_CATEGORIES_KEY = stringPreferencesKey("custom_categories")
        private val PURCHASED_SLOTS_KEY = intPreferencesKey("purchased_slots")
        
        val DEFAULT_CATEGORIES = setOf(
            "Communicator", "Databank", "X (Devices)", "WWW", "Market", "Office", "Uncategorized", "System"
        )
        
        val OPTIONAL_CATEGORIES = listOf(
            "Social Media", "Mass Media", "Games", "Entertainment", "Reference", "Books", 
            "Education", "Business", "Finance", "Health", "Productivity", "Bet"
        )
    }
}
