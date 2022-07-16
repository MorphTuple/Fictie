package io.morphtuple.fictie.data.preferences

import android.content.Context
import android.content.res.Configuration
import androidx.preference.PreferenceManager

class PreferencesHelper(val context: Context) {
    private val sp = PreferenceManager.getDefaultSharedPreferences(context)

    fun getReaderTheme(): ThemeType {
        return when (sp.getString("reader_theme", null)) {
            null, "follow_application" -> {
                when (context.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_YES -> ThemeType.DARK
                    Configuration.UI_MODE_NIGHT_NO -> ThemeType.LIGHT
                    else -> ThemeType.DARK
                }
            }
            "dark" -> ThemeType.DARK
            else -> ThemeType.LIGHT
        }
    }

    fun setReaderTheme(t: ReaderThemeType) {
        sp.edit().putString("reader_theme", t.name.lowercase())
    }
}