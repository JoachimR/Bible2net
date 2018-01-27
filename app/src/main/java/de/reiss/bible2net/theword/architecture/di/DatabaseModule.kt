package de.reiss.bible2net.theword.architecture.di

import android.app.Application
import dagger.Module
import dagger.Provides
import de.reiss.bible2net.theword.database.BibleItemDao
import de.reiss.bible2net.theword.database.NoteItemDao
import de.reiss.bible2net.theword.database.TheWordDatabase
import de.reiss.bible2net.theword.database.TheWordItemDao

@Module
open class DatabaseModule(private val application: Application) {

    open fun getDatabase(): TheWordDatabase = TheWordDatabase.create(application)

    @Provides
    @ApplicationScope
    open fun theWordDatabase() = getDatabase()

    @Provides
    @ApplicationScope
    open fun theWordItemDao(): TheWordItemDao = getDatabase().theWordItemDao()

    @Provides
    @ApplicationScope
    open fun bibleItemDao(): BibleItemDao = getDatabase().bibleItemDao()

    @Provides
    @ApplicationScope
    open fun noteItemDao(): NoteItemDao = getDatabase().noteItemDao()

}
