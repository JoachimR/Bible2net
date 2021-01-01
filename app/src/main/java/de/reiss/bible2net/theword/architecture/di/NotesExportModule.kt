package de.reiss.bible2net.theword.architecture.di

import dagger.Module
import dagger.Provides
import de.reiss.bible2net.theword.note.export.FileProvider
import de.reiss.bible2net.theword.note.export.NotesExporter

@Module
open class NotesExportModule {

    @Provides
    @ApplicationScope
    open fun fileProvider() = FileProvider()

    @Provides
    @ApplicationScope
    open fun notesExporter(fileProvider: FileProvider): NotesExporter = NotesExporter(fileProvider)
}
