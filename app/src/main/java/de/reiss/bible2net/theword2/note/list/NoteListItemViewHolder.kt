package de.reiss.bible2net.theword2.note.list

import android.view.View
import android.widget.TextView
import de.reiss.bible2net.theword2.R
import de.reiss.bible2net.theword2.formattedDate
import de.reiss.bible2net.theword2.util.extensions.onClick
import de.reiss.bible2net.theword2.util.view.ListItemViewHolder
import de.reiss.bible2net.theword2.util.view.StableListItem

class NoteListItemViewHolder(
    layout: View,
    private val noteClickListener: NoteClickListener
) :
    ListItemViewHolder(layout) {

    private val context = layout.context
    private val noteDate = layout.findViewById<TextView>(R.id.note_list_item_date)
    private val noteText = layout.findViewById<TextView>(R.id.note_list_item_text)

    private var item: NoteListItem? = null

    init {
        layout.onClick {
            item?.let {
                noteClickListener.onNoteClicked(it.note)
            }
        }
    }

    override fun bindViews(item: StableListItem, isLastItem: Boolean) {
        if (item is NoteListItem) {
            this.item = item
            noteDate.text = formattedDate(context, item.note.date.time)
            noteText.text = item.note.noteText
        }
    }
}
