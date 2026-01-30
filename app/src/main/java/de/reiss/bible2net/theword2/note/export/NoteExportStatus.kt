package de.reiss.bible2net.theword2.note.export

sealed class NoteExportStatus {
    object NoPermission : NoteExportStatus()
    object NoNotes : NoteExportStatus()
    class ExportError(val fileName: String) : NoteExportStatus()
    class ExportSuccess(val fileName: String) : NoteExportStatus()
    object Exporting : NoteExportStatus()
}
