package com.example.categorysym.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.categorysym.data.AppItem
import com.example.categorysym.data.CategorySection
import com.example.categorysym.ui.components.AppCardWithBackground

@Composable
fun CategoryRow(
    section: CategorySection,
    categoryNames: List<String>,
    onAppClick: (AppItem) -> Unit,
    onCategoryMove: (AppItem, String) -> Unit,
    onToggleHide: (AppItem) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp) // 🔥 optimum yükseklik
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .background(
                color = section.color.copy(alpha = 0.2f),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 8.dp, vertical = 2.dp) // 🔥 iç padding (en önemli kısım)
    ) {

        // 🔹 Kategori başlığı
        Text(
            text = section.name,
            color = Color.Black,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 2.dp)
        )

        // 🔹 Uygulama satırı (6 ikon sabit)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly, // 🔥 dengeli dağılım
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(6) { index ->
                val app = section.apps.getOrNull(index)

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    if (app != null) {
                        AppCardWithBackground(
                            app = app,
                            color = section.color,
                            categoryNames = categoryNames,
                            onAppClick = { onAppClick(app) },
                            onCategoryMove = { category -> onCategoryMove(app, category) },
                            onToggleHide = { onToggleHide(app) }
                        )
                    }
                }
            }
        }
    }
}
