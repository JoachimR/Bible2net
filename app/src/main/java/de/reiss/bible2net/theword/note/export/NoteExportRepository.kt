package de.reiss.bible2net.theword.note.export

import androidx.lifecycle.MutableLiveData
import de.reiss.bible2net.theword.database.NoteItemDao
import de.reiss.bible2net.theword.note.export.NoteExportStatus.ExportError
import de.reiss.bible2net.theword.note.export.NoteExportStatus.ExportSuccess
import de.reiss.bible2net.theword.note.export.NoteExportStatus.Exporting
import de.reiss.bible2net.theword.note.export.NoteExportStatus.NoNotes
import de.reiss.bible2net.theword.note.export.NoteExportStatus.NoPermission
import java.util.concurrent.Executor
import javax.inject.Inject

open class NoteExportRepository @Inject constructor(
    private val executor: Executor,
    private val noteItemDao: NoteItemDao,
    private val notesExporter: NotesExporter
) {

    open fun exportNotes(liveData: MutableLiveData<NoteExportStatus>) {
        if (notesExporter.isExternalStorageWritable().not()) {
            liveData.postValue(NoPermission)
            return
        }

        liveData.postValue(Exporting)

        executor.execute {

            val allNotes = noteItemDao.all()

            if (allNotes.isEmpty()) {
                liveData.postValue(NoNotes)
            } else {
                val exportResult = notesExporter.exportNotes(notes = allNotes)

                liveData.postValue(
                    if (exportResult) {
                        ExportSuccess(notesExporter.fileName)
                    } else {
                        ExportError(notesExporter.fileName)
                    }
                )
            }
        }
    }
}
