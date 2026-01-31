package de.reiss.bible2net.theword2

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import de.reiss.bible2net.theword2.architecture.di.ApplicationComponent
import de.reiss.bible2net.theword2.architecture.di.ContextModule
import de.reiss.bible2net.theword2.architecture.di.DaggerApplicationComponent
import de.reiss.bible2net.theword2.architecture.di.DatabaseModule
import de.reiss.bible2net.theword2.notification.NotificationWorker

open class App : Application() {

    companion object {

        @JvmStatic
        lateinit var component: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        component = createComponent()
        initApp()
    }

    open fun initApp() {
        NotificationWorker.schedule(this)
        initPrefs()
        initNightMode()
    }

    private fun initPrefs() {
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }

    private fun initNightMode() {
        val nightMode = component.appPreferences.currentDesign().nightMode
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }

    open fun createComponent(): ApplicationComponent =
        DaggerApplicationComponent.builder()
            .contextModule(ContextModule(this))
            .databaseModule(DatabaseModule(this))
            .build()
}
