package com.example.categorysym.data

data class CustomCategory(
    val id: String,
    val name: String,
    val colorHex: String,
    val note: String,
    val examples: String = ""
)
