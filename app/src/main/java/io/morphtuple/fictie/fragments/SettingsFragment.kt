package io.morphtuple.fictie.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import io.morphtuple.fictie.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.app_settings, rootKey)

        val readerTheme = findPreference<ListPreference>("pref_reader_theme")
        val prefTheme = findPreference<ListPreference>("pref_theme")

        // TODO Move to a helper class
        val sp = PreferenceManager.getDefaultSharedPreferences(context!!)

        readerTheme?.setOnPreferenceChangeListener { _, newValue ->
            sp.edit().putString("reader_theme", newValue as String).commit()
        }

//        prefTheme?.setOnPreferenceChangeListener { _, newValue ->
//            true
//        }
    }
}