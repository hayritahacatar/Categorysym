package com.example.categorysym.data

import androidx.compose.ui.graphics.Color

data class Category(
    val name: String,
    val apps: List<AppItem>,
    val color: Color? = null
)