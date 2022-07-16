package io.morphtuple.fictie.ui.settings

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import io.morphtuple.fictie.R
import io.morphtuple.fictie.data.preferences.PreferencesHelper
import io.morphtuple.fictie.data.preferences.ReaderThemeType

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.app_settings, rootKey)

        val readerTheme = findPreference<ListPreference>("pref_reader_theme")
        val prefTheme = findPreference<ListPreference>("pref_theme")

        val ph = PreferencesHelper(requireContext())

        readerTheme?.setOnPreferenceChangeListener { _, newValue ->
            ph.setReaderTheme(ReaderThemeType.valueOf((newValue as String).uppercase()))
            true
        }

//        prefTheme?.setOnPreferenceChangeListener { _, newValue ->
//            true
//        }
    }
}