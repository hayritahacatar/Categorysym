package com.example.categorysym.utils

import androidx.annotation.StringRes
import com.example.categorysym.R

data class CategoryInfo(
    @StringRes val descriptionRes: Int,
    @StringRes val examplesRes: Int,
)

object CategoryRegistry {
    val infoMap = mapOf(
        "Communicator" to CategoryInfo(
            descriptionRes = R.string.desc_communicator,
            examplesRes = R.string.ex_communicator,
        ),
        "Databank" to CategoryInfo(
            descriptionRes = R.string.desc_databank,
            examplesRes = R.string.ex_databank,
        ),
        "X (Devices)" to CategoryInfo(
            descriptionRes = R.string.desc_x_devices,
            examplesRes = R.string.ex_x_devices,
        ),
        "WWW" to CategoryInfo(
            descriptionRes = R.string.desc_www,
            examplesRes = R.string.ex_www,
        ),
        "Market" to CategoryInfo(
            descriptionRes = R.string.desc_market,
            examplesRes = R.string.ex_market,
        ),
        "Office" to CategoryInfo(
            descriptionRes = R.string.desc_office,
            examplesRes = R.string.ex_office,
        ),
        "Uncategorized" to CategoryInfo(
            descriptionRes = R.string.desc_uncategorized,
            examplesRes = R.string.ex_uncategorized,
        ),
        "System" to CategoryInfo(
            descriptionRes = R.string.desc_system,
            examplesRes = R.string.ex_system,
        ),
        "Social Media" to CategoryInfo(
            descriptionRes = R.string.desc_social_media,
            examplesRes = R.string.ex_social_media,
        ),
        "Mass Media" to CategoryInfo(
            descriptionRes = R.string.desc_mass_media,
            examplesRes = R.string.ex_mass_media,
        ),
        "Games" to CategoryInfo(
            descriptionRes = R.string.desc_games,
            examplesRes = R.string.ex_games,
        ),
        "Entertainment" to CategoryInfo(
            descriptionRes = R.string.desc_entertainment,
            examplesRes = R.string.ex_entertainment,
        ),
        "Finance" to CategoryInfo(
            descriptionRes = R.string.desc_finance,
            examplesRes = R.string.ex_finance,
        ),
        "Reference" to CategoryInfo(
            descriptionRes = R.string.desc_reference,
            examplesRes = R.string.ex_reference,
        ),
        "Books" to CategoryInfo(
            descriptionRes = R.string.desc_books,
            examplesRes = R.string.ex_books,
        ),
        "Education" to CategoryInfo(
            descriptionRes = R.string.desc_education,
            examplesRes = R.string.ex_education,
        ),
        "Business" to CategoryInfo(
            descriptionRes = R.string.desc_business,
            examplesRes = R.string.ex_business,
        ),
        "Health" to CategoryInfo(
            descriptionRes = R.string.desc_health,
            examplesRes = R.string.ex_health,
        ),
        "Bet" to CategoryInfo(
            descriptionRes = R.string.desc_bet,
            examplesRes = R.string.ex_bet,
        ),
        "Productivity" to CategoryInfo(
            descriptionRes = R.string.desc_productivity,
            examplesRes = R.string.ex_productivity,
        ),
    )
}