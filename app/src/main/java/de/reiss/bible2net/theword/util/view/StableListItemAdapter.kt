package de.reiss.bible2net.theword.util.view

import android.support.annotation.LayoutRes
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.lang.IllegalArgumentException


abstract class StableListItemAdapter : RecyclerView.Adapter<ListItemViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override final fun setHasStableIds(hasStableIds: Boolean) {
        if (!hasStableIds) {
            throw IllegalArgumentException("StableListItemAdapter does not allow unstable ids")
        }
        super.setHasStableIds(hasStableIds)
    }

    protected var list = emptyList<StableListItem>()

    fun updateContent(content: List<StableListItem>) {
        val newList = content.toList()
        val diffResult = DiffUtil.calculateDiff(StableDiffCallback(list, newList))
        list = newList
        diffResult.dispatchUpdatesTo(this)
    }

    protected fun inflate(viewGroup: ViewGroup, @LayoutRes layoutResId: Int): View =
            LayoutInflater.from(viewGroup.context).inflate(layoutResId, viewGroup, false)

    override fun getItemId(position: Int): Long = getItem(position).stableId()

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        holder.bindViews(item = getItem(position), isLastItem = position == itemCount - 1)
    }

    override fun getItemCount(): Int = list.size

    protected fun getItem(position: Int): StableListItem = list[position]

    private class StableDiffCallback(private val oldList: List<StableListItem>,
                                     private val newList: List<StableListItem>)
        : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                oldList[oldItemPosition].stableId() == newList[newItemPosition].stableId()

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                oldList[oldItemPosition] == newList[newItemPosition]

    }

}