package de.reiss.bible2net.theword.preferences

import android.os.Bundle
import androidx.preference.ListPreference
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompatDividers
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.model.Bible
import de.reiss.bible2net.theword.util.extensions.isPlayServiceAvailable


class AppPreferencesFragment : PreferenceFragmentCompatDividers() {

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

    override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        try {
            return super.onCreateView(inflater, container, savedInstanceState)
        } finally {
            setDividerPreferences(PreferenceFragmentCompatDividers.DIVIDER_PADDING_CHILD
                    or PreferenceFragmentCompatDividers.DIVIDER_CATEGORY_AFTER_LAST
                    or PreferenceFragmentCompatDividers.DIVIDER_CATEGORY_BETWEEN)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (findPreference(getString(R.string.pref_language_key)) as ListPreference).apply {
            val bibleList = bibles.toList().sortedBy { it.languageCode }
            entries = bibleList.map { """[${it.languageCode}]   ${it.bibleName}""" }.toTypedArray()
            entryValues = bibleList.map { it.key }.toTypedArray()
        }

        findPreference(getString(R.string.pref_show_daily_notification_key)).apply {
            val playServiceAvailable = context.isPlayServiceAvailable()
            isVisible = playServiceAvailable
            setDefaultValue(playServiceAvailable)
        }
    }


}
