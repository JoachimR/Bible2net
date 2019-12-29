package de.reiss.bible2net.theword.util.view

import androidx.recyclerview.widget.RecyclerView
import android.view.View

abstract class ListItemViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

    abstract fun bindViews(item: StableListItem, isLastItem: Boolean)

}