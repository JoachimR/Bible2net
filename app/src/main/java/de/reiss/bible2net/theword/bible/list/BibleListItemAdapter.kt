package de.reiss.bible2net.theword.bible.list

import android.view.ViewGroup
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.bible.BibleClickListener
import de.reiss.bible2net.theword.util.view.StableListItemAdapter

class BibleListItemAdapter(private val bibleClickListener: BibleClickListener)
    : StableListItemAdapter() {

    override fun getItemViewType(position: Int) =
            when (getItem(position)) {

                is BibleListItem -> R.layout.bible_item

                else -> -1
            }


    override fun onCreateViewHolder(group: ViewGroup, viewType: Int) =
            when (viewType) {

                R.layout.bible_item -> {
                    BibleListItemViewHolder(inflate(group, viewType), bibleClickListener)
                }

                else -> throw IllegalStateException("Invalid viewType " + viewType)
            }

}
