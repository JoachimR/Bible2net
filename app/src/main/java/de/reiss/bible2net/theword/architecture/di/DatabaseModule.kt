package de.reiss.bible2net.theword.architecture.di

import android.app.Application
import android.arch.persistence.room.Room
import dagger.Module
import dagger.Provides
import de.reiss.bible2net.theword.database.TheWordDatabase

@Module
open class DatabaseModule(val application: Application) {

    open fun getDatabase(): TheWordDatabase = Room.databaseBuilder(
            application,
            TheWordDatabase::class.java,
            "TheWordDatabase.db"
    ).build()

    @Provides
    @ApplicationScope
    open fun theWordDatabase() = getDatabase()

    @Provides
    @ApplicationScope
    open fun theWordItemDao() = getDatabase().theWordItemDao()

    @Provides
    @ApplicationScope
    open fun bibleItemDao() = getDatabase().bibleItemDao()

    @Provides
    @ApplicationScope
    open fun noteItemDao() = getDatabase().noteItemDao()

}
