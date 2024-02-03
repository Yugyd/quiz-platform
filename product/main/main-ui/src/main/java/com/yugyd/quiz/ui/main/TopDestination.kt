package com.yugyd.quiz.ui.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.yugyd.quiz.uikit.R

enum class TopDestination(
    @StringRes val titleResId: Int,
    @DrawableRes val iconResId: Int,
) {

    THEME(
        R.string.title_theme,
        com.yugyd.quiz.commonui.R.drawable.ic_menu_book,
    ),

    CORRECT(
        R.string.title_correct,
        com.yugyd.quiz.commonui.R.drawable.ic_check_circle,
    ),

    PROGRESS(
        R.string.title_progress,
        com.yugyd.quiz.commonui.R.drawable.ic_pie_chart,
    ),

    PROFILE(
        R.string.title_profile,
        R.drawable.ic_account_circle,
    ),
}
