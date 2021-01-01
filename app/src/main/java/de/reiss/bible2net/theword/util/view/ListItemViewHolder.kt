package de.reiss.bible2net.theword.util.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class ListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(
    itemView
) {

    abstract fun bindViews(item: StableListItem, isLastItem: Boolean)
}
