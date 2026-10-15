package com.example.categorysym.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.categorysym.data.AppItem
import com.example.categorysym.data.DrawerPage

@Composable
fun DrawerPageScreen(
    page: DrawerPage,
    categoryNames: List<String>,
    listState: LazyListState = rememberLazyListState(),
    onAppClick: (AppItem) -> Unit,
    onCategoryMove: (AppItem, String) -> Unit,
    onToggleHide: (AppItem) -> Unit = {}
) {
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(
            start = 6.dp,
            end = 6.dp,
            top = 0.dp,
            bottom = 2.dp
        )
    ) {
        items(page.sections) { section ->
            CategoryRow(
                section = section,
                categoryNames = categoryNames,
                onAppClick = onAppClick,
                onCategoryMove = onCategoryMove,
                onToggleHide = onToggleHide
            )
        }
    }
}