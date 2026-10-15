package com.example.categorysym.utils

import androidx.compose.ui.graphics.Color
import com.example.categorysym.data.Category
import com.example.categorysym.data.CategorySection

val categoryColors = listOf(
    Color(0xFF4CAF50), // 1. Communicator - Green
    Color(0xFF5D4037), // 2. Databank - Rich Brown
    Color(0xFFB71C1C), // 3. X (Devices) - Strong Red
    Color(0xFF2196F3), // 4. WWW - Blue
    Color(0xFFFF9800), // 5. Market - Orange
    Color(0xFFB0BEC5), // 6. Office - Metallic Gray
    Color(0xFF616161), // 7. Uncategorized - Smoke
    Color(0xFFC0C0C0), // 8. System - Win95 Gray
    Color(0xFFFFEB3B), // 9. Games - Yellow
    Color(0xFFFFF3E0), // 10. Entertainment - Cream
    Color(0xFF2E7D32), // 11. Finance - Bank Emerald Green
    Color(0xFF009688), // 12. Reference - Teal
    Color(0xFF3F51B5), // 13. Books - Indigo
    Color(0xFF00BCD4), // 14. Mass Media - Cyan
    Color(0xFFCDDC39), // 15. Education - Lime
    Color(0xFF1A237E), // 16. Business - Navy
    Color(0xFF673AB7), // 17. Social Media - Deep Purple
    Color(0xFFFF5252), // 18. Health - Red Accent
    Color(0xFF1B5E20), // 19. Bet - Dark Green
    Color(0xFFFFD600), // 20. Productivity - Gold
)

fun buildSections(categories: List<Category>): List<CategorySection> {
    return categories.map { category ->
        val color = getCategoryColor(category.name)
        CategorySection(
            name = category.name,
            color = color,
            apps = category.apps
        )
    }
}

fun getCategoryColor(name: String): Color {
    return when (name) {
        "Communicator" -> categoryColors[0]
        "Databank" -> categoryColors[1]
        "X (Devices)" -> categoryColors[2]
        "WWW" -> categoryColors[3]
        "Market" -> categoryColors[4]
        "Office" -> categoryColors[5]
        "Uncategorized" -> categoryColors[6]
        "System" -> categoryColors[7]
        "Social Media" -> categoryColors[8]
        "Mass Media" -> categoryColors[9]
        "Games" -> categoryColors[10]
        "Entertainment" -> categoryColors[11]
        "Finance" -> categoryColors[12]
        "Reference" -> categoryColors[13]
        "Books" -> categoryColors[14]
        "Education" -> categoryColors[15]
        "Business" -> categoryColors[16]
        "Health" -> categoryColors[17]
        "Bet" -> categoryColors[18]
        "Productivity" -> categoryColors[19]
        else -> categoryColors[6] // Uncategorized
    }
}

fun parseColor(hex: String): Color {
    return try {
        val sanitizedHex = if (!hex.startsWith("#")) "#$hex" else hex
        Color(android.graphics.Color.parseColor(sanitizedHex))
    } catch (_: Exception) {
        Color.Gray
    }
}
