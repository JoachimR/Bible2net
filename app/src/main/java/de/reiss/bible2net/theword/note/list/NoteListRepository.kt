package de.reiss.bible2net.theword.note.list

import androidx.lifecycle.MutableLiveData
import de.reiss.bible2net.theword.architecture.AsyncLoad
import de.reiss.bible2net.theword.database.NoteItemDao
import de.reiss.bible2net.theword.database.converter.Converter
import de.reiss.bible2net.theword.model.Note
import java.util.concurrent.Executor
import javax.inject.Inject

open class NoteListRepository @Inject constructor(private val executor: Executor,
                                                  private val noteItemDao: NoteItemDao) {

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
                result.postValue(AsyncLoad.success(
                        FilteredNotes(
                                allItems = allItems,
                                filteredItems = allItems,
                                query = query)
                ))
            } else {
                val filteredItems = filter(query, allItems)
                result.postValue(AsyncLoad.success(
                        FilteredNotes(
                                allItems = allItems,
                                filteredItems = filteredItems,
                                query = query)
                ))
            }
        }
    }

    open fun applyNewFilter(query: String, result: MutableLiveData<AsyncLoad<FilteredNotes>>) {
        val unfiltered = result.value?.data ?: return
        if (query.isEmpty()) {
            result.postValue(AsyncLoad.success(
                    FilteredNotes(
                            allItems = unfiltered.allItems,
                            filteredItems = unfiltered.allItems,
                            query = query)
            ))
            return
        }

        result.postValue(AsyncLoad.loading(unfiltered))

        executor.execute {
            val filteredItems = filter(query, unfiltered.allItems)

            result.postValue(AsyncLoad.success(
                    FilteredNotes(
                            allItems = unfiltered.allItems,
                            filteredItems = filteredItems,
                            query = query)
            ))
        }


    }

    private fun filter(query: String, noteList: List<Note>) =
            query.toLowerCase().let { match ->
                noteList.filter {
                    it.noteText.toLowerCase().contains(match)
                            || it.theWordContent.chapter1.toLowerCase().contains(match)
                            || it.theWordContent.verse1.toLowerCase().contains(match)
                            || it.theWordContent.intro1.toLowerCase().contains(match)
                            || it.theWordContent.text1.toLowerCase().contains(match)
                            || it.theWordContent.ref1.toLowerCase().contains(match)
                            || it.theWordContent.book2.toLowerCase().contains(match)
                            || it.theWordContent.chapter2.toLowerCase().contains(match)
                            || it.theWordContent.verse2.toLowerCase().contains(match)
                            || it.theWordContent.intro2.toLowerCase().contains(match)
                            || it.theWordContent.text2.toLowerCase().contains(match)
                            || it.theWordContent.ref2.toLowerCase().contains(match)
                }
            }

}