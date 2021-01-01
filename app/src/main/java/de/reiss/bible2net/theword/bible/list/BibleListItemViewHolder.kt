package de.reiss.bible2net.theword.bible.list

import android.view.View
import android.widget.TextView
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.bible.BibleClickListener
import de.reiss.bible2net.theword.util.extensions.onClick
import de.reiss.bible2net.theword.util.view.ListItemViewHolder
import de.reiss.bible2net.theword.util.view.StableListItem

class BibleListItemViewHolder(
    layout: View,
    private val bibleClickListener: BibleClickListener
) :
    ListItemViewHolder(layout) {

    private val context = layout.context
    private val language = layout.findViewById<TextView>(R.id.bible_item_language)
    private val bibleName = layout.findViewById<TextView>(R.id.bible_item_bible_name)

    private var item: BibleListItem? = null

    init {
        layout.onClick {
            item?.let {
                bibleClickListener.onBibleClicked(it.bible)
            }
        }
    }

    override fun bindViews(item: StableListItem, isLastItem: Boolean) {
        if (item is BibleListItem) {
            this.item = item
            language.text = context.getString(R.string.bible_item_language, item.bible.languageCode)
            bibleName.text = item.bible.bibleName
        }
    }
}
