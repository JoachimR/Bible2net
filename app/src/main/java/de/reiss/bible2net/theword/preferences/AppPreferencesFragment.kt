package de.reiss.bible2net.theword.preferences

import android.os.Bundle
import android.view.View
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.model.Bible
import de.reiss.bible2net.theword.util.extensions.isPlayServiceAvailable


class AppPreferencesFragment : PreferenceFragmentCompat() {

    companion object {

        private const val LIST_BIBLES = "LIST_BIBLES"

        fun newInstance(bibles: List<Bible>) =
                AppPreferencesFragment().apply {
                    arguments = Bundle().apply {
                        putParcelableArrayList(LIST_BIBLES,
                                arrayListOf<Bible>().apply { addAll(bibles) })
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
            val bibleList = bibles.toList().sortedWith(compareBy({ it.languageCode }, { it.bibleName }))
            entries = bibleList.map { """[${it.languageCode}]   ${it.bibleName}""" }.toTypedArray()
            entryValues = bibleList.map { it.key }.toTypedArray()
        }
        (findPreference(getString(R.string.pref_show_daily_notification_key)) as SwitchPreferenceCompat?)?.apply {
            val playServiceAvailable = requireContext().isPlayServiceAvailable()
            isVisible = playServiceAvailable
            setDefaultValue(playServiceAvailable)
        }
    }


}
