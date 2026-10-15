package com.example.categorysym.data

import androidx.compose.ui.graphics.Color

data class CategorySection(
    val name: String,
    val color: Color,
    val apps: List<AppItem>
)