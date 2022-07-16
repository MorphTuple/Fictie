package io.morphtuple.fictie.data

import android.content.Context
import androidx.preference.PreferenceManager

class PreferencesHelper(val context: Context) {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)
}