package de.reiss.bible2net.theword.util.view

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class ListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bindViews(item: StableListItem, isLastItem: Boolean)

}