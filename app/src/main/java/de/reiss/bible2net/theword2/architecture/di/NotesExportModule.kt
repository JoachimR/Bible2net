package de.reiss.bible2net.theword2.architecture.di

import android.content.Context
import dagger.Module
import dagger.Provides
import de.reiss.bible2net.theword2.note.export.FileProvider
import de.reiss.bible2net.theword2.note.export.NotesExporter

@Module(
    includes = [
        ContextModule::class
    ]
)
open class NotesExportModule {

    @Provides
    @ApplicationScope
    open fun fileProvider(context: Context) = FileProvider(context)

    @Provides
    @ApplicationScope
    open fun notesExporter(fileProvider: FileProvider): NotesExporter = NotesExporter(fileProvider)
}
