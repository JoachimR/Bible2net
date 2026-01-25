package de.reiss.bible2net.theword.preferences

import android.os.Bundle
import android.view.View
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.model.Bible
import de.reiss.bible2net.theword.util.sortBibles

class AppPreferencesFragment : PreferenceFragmentCompat() {

    companion object {

        private const val LIST_BIBLES = "LIST_BIBLES"

        fun newInstance(bibles: List<Bible>) =
            AppPreferencesFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(
                        LIST_BIBLES,
                        arrayListOf<Bible>().apply { addAll(bibles) }
                    )
                }
            }
    }

    private lateinit var bibles: List<Bible>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bibles = arguments?.getParcelableArrayList(LIST_BIBLES) ?: arrayListOf()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (findPreference(getString(R.string.pref_language_key)) as ListPreference?)?.apply {
            val sortedList = sortBibles(bibles)
            entries = sortedList.map { """[${it.languageCode}]   ${it.bibleName}""" }.toTypedArray()
            entryValues = sortedList.map { it.key }.toTypedArray()
        }
        (
            findPreference(getString(R.string.pref_show_daily_notification_key))
                as SwitchPreferenceCompat?
            )?.apply {
            isVisible = true
            setDefaultValue(true)
        }
    }
}
