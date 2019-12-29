package de.reiss.bible2net.theword.note.export

import androidx.lifecycle.MutableLiveData
import de.reiss.bible2net.theword.database.NoteItemDao
import java.util.concurrent.Executor
import javax.inject.Inject

open class NoteExportRepository @Inject constructor(private val executor: Executor,
                                                    private val noteItemDao: NoteItemDao,
                                                    private val notesExporter: NotesExporter) {

    open fun exportNotes(liveData: MutableLiveData<NoteExportStatus>) {
        if (notesExporter.isExternalStorageWritable().not()) {
            liveData.postValue(NoPermissionStatus())
            return
        }

        liveData.postValue(ExportingStatus())

        executor.execute {

            val allNotes = noteItemDao.all()

            if (allNotes.isEmpty()) {
                liveData.postValue(NoNotesStatus())
            } else {
                val exportResult = notesExporter.exportNotes(notes = allNotes)

                liveData.postValue(if (exportResult) {
                    ExportSuccessStatus(notesExporter.directory, notesExporter.fileName)
                } else {
                    ExportErrorStatus(notesExporter.directory, notesExporter.fileName)
                })
            }
        }
    }

}