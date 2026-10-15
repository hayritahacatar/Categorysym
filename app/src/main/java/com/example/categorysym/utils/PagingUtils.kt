package com.example.categorysym.utils

import com.example.categorysym.data.*

fun chunkRow(apps: List<AppItem>, pageSize: Int = 6): List<List<AppItem>> {
    return apps.chunked(pageSize)
}

fun buildPages(categories: List<Category>): List<DrawerPage> {
    val maxPageCount = categories.maxOfOrNull {
        chunkRow(it.apps).size
    } ?: 0

    if (maxPageCount == 0) {
        return listOf(
            DrawerPage(
                sections = categories.map { category ->
                    CategorySection(
                        name = category.name,
                        color = category.color ?: getCategoryColor(category.name),
                        apps = emptyList()
                    )
                }
            )
        )
    }

    val pages = mutableListOf<DrawerPage>()

    for (pageIndex in 0 until maxPageCount) {
        val sections = categories.map { category ->
            val chunks = chunkRow(category.apps)
            val apps = chunks.getOrNull(pageIndex) ?: emptyList()

            CategorySection(
                name = category.name,
                color = category.color ?: getCategoryColor(category.name),
                apps = apps
            )
        }

        pages.add(DrawerPage(sections))
    }

    return pages
}