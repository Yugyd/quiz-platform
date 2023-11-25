package com.yugyd.quiz.domain.content.data

import android.content.Context
import androidx.core.content.edit
import com.yugyd.quiz.domain.content.ContentPreferencesSource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class ContentPreferencesSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : ContentPreferencesSource {

    private val preferences by lazy {
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    override var databaseMarker: String?
        get() = preferences.getString(PREF_DATABASE_MARKER, null)
        set(value) = preferences.edit { putString(PREF_DATABASE_MARKER, value) }

    companion object {
        private const val PREFERENCES_NAME = "options"
        private const val PREF_DATABASE_MARKER = "com.yugyd.quiz.PREF_DATABASE_MARKER"
    }
}
