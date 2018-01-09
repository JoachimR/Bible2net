package de.reiss.bible2net.theword.architecture.di

import android.app.SearchManager
import android.content.ClipboardManager
import android.content.Context
import dagger.Component
import de.reiss.bible2net.theword.bible.BibleRepository
import de.reiss.bible2net.theword.main.BibleListUpdater
import de.reiss.bible2net.theword.main.content.TheWordRepository
import de.reiss.bible2net.theword.main.viewpager.ViewPagerRepository
import de.reiss.bible2net.theword.migration.MigrateTo127
import de.reiss.bible2net.theword.note.details.NoteDetailsRepository
import de.reiss.bible2net.theword.note.edit.EditNoteRepository
import de.reiss.bible2net.theword.note.export.NoteExportRepository
import de.reiss.bible2net.theword.note.list.NoteListRepository
import de.reiss.bible2net.theword.notification.NotificationHelper
import de.reiss.bible2net.theword.preferences.AppPreferences
import de.reiss.bible2net.theword.preferences.AppPreferencesRepository
import de.reiss.bible2net.theword.widget.WidgetRefresher

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

    val migrateTo127: MigrateTo127

    val searchManager: SearchManager

}
