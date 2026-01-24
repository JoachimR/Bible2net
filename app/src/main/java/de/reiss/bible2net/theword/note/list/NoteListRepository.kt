package de.reiss.bible2net.theword.note.list

import androidx.lifecycle.MutableLiveData
import de.reiss.bible2net.theword.architecture.AsyncLoad
import de.reiss.bible2net.theword.database.NoteItemDao
import de.reiss.bible2net.theword.database.converter.Converter
import de.reiss.bible2net.theword.model.Note
import de.reiss.bible2net.theword.model.TheWordContent
import java.util.Locale
import java.util.concurrent.Executor
import javax.inject.Inject

open class NoteListRepository @Inject constructor(
    private val executor: Executor,
    private val noteItemDao: NoteItemDao
) {

    open fun getAllNotes(result: MutableLiveData<AsyncLoad<FilteredNotes>>) {
        val oldData = result.value?.data ?: return
        result.postValue(AsyncLoad.loading(oldData))

        executor.execute {
            val query = oldData.query
            val allItems = noteItemDao.all()
                .mapNotNull {
                    Converter.noteItemToNote(it)
                }

            if (query.isEmpty()) {
                result.postValue(
                    AsyncLoad.success(
                        FilteredNotes(
                            allItems = allItems,
                            filteredItems = allItems,
                            query = query
                        )
                    )
                )
            } else {
                val filteredItems = filter(query, allItems)
                result.postValue(
                    AsyncLoad.success(
                        FilteredNotes(
                            allItems = allItems,
                            filteredItems = filteredItems,
                            query = query
                        )
                    )
                )
            }
        }
    }

    open fun applyNewFilter(query: String, result: MutableLiveData<AsyncLoad<FilteredNotes>>) {
        val unfiltered = result.value?.data ?: return
        if (query.isEmpty()) {
            result.postValue(
                AsyncLoad.success(
                    FilteredNotes(
                        allItems = unfiltered.allItems,
                        filteredItems = unfiltered.allItems,
                        query = query
                    )
                )
            )
            return
        }

        result.postValue(AsyncLoad.loading(unfiltered))

        executor.execute {
            val filteredItems = filter(query, unfiltered.allItems)

            result.postValue(
                AsyncLoad.success(
                    FilteredNotes(
                        allItems = unfiltered.allItems,
                        filteredItems = filteredItems,
                        query = query
                    )
                )
            )
        }
    }

    private fun filter(query: String, noteList: List<Note>) =
        query.lowerCase().let { match ->
            noteList.filter {
                it.noteText.lowerCase().contains(match) || it.theWordContent.contains(match)
            }
        }

    private fun TheWordContent.contains(match: String) =
        this.chapter1.lowerCase().contains(match) ||
            this.verse1.lowerCase().contains(match) ||
            this.intro1.lowerCase().contains(match) ||
            this.text1.lowerCase().contains(match) ||
            this.ref1.lowerCase().contains(match) ||
            this.book2.lowerCase().contains(match) ||
            this.chapter2.lowerCase().contains(match) ||
            this.verse2.lowerCase().contains(match) ||
            this.intro2.lowerCase().contains(match) ||
            this.text2.lowerCase().contains(match) ||
            this.ref2.lowerCase().contains(match)

    private fun String.lowerCase() = this.lowercase(Locale.getDefault())
}
