package de.reiss.bible2net.theword2.architecture.di

import android.app.SearchManager
import android.content.ClipboardManager
import android.content.Context
import dagger.Component
import de.reiss.bible2net.theword2.bible.BibleRepository
import de.reiss.bible2net.theword2.main.BibleListUpdater
import de.reiss.bible2net.theword2.main.content.TheWordRepository
import de.reiss.bible2net.theword2.main.viewpager.ViewPagerRepository
import de.reiss.bible2net.theword2.note.details.NoteDetailsRepository
import de.reiss.bible2net.theword2.note.edit.EditNoteRepository
import de.reiss.bible2net.theword2.note.export.NoteExportRepository
import de.reiss.bible2net.theword2.note.list.NoteListRepository
import de.reiss.bible2net.theword2.notification.NotificationHelper
import de.reiss.bible2net.theword2.preferences.AppPreferences
import de.reiss.bible2net.theword2.preferences.AppPreferencesRepository
import de.reiss.bible2net.theword2.widget.WidgetRefresher

@ApplicationScope
@Component(
    modules = [
        ContextModule::class,
        AndroidModule::class,
        DatabaseModule::class,
        PreferenceModule::class,
        ExecutorModule::class,
        OkHttpModule::class,
        RetrofitModule::class,
        DownloaderModule::class,
        NotesExportModule::class
    ]
)
interface ApplicationComponent {

    val context: Context
    val clipboardManager: ClipboardManager

    val theWordRepository: TheWordRepository
    val viewPagerRepository: ViewPagerRepository
    val bibleRepository: BibleRepository
    val appPreferencesRepository: AppPreferencesRepository
    val editNoteRepository: EditNoteRepository
    val noteListRepository: NoteListRepository
    val noteDetailsRepository: NoteDetailsRepository
    val noteExportRepository: NoteExportRepository

    val widgetRefresher: WidgetRefresher

    val bibleListUpdater: BibleListUpdater

    val notificationHelper: NotificationHelper
    val appPreferences: AppPreferences

    val searchManager: SearchManager
}
