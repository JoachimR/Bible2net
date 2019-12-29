package de.reiss.bible2net.theword

import android.app.Application
import androidx.preference.PreferenceManager
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import de.reiss.bible2net.theword.architecture.di.ApplicationComponent
import de.reiss.bible2net.theword.architecture.di.ContextModule
import de.reiss.bible2net.theword.architecture.di.DaggerApplicationComponent
import de.reiss.bible2net.theword.architecture.di.DatabaseModule
import de.reiss.bible2net.theword.notification.NotificationService
import io.fabric.sdk.android.Fabric

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
        Fabric.with(this, Crashlytics.Builder()
                .core(CrashlyticsCore.Builder()
                        .disabled(BuildConfig.DEBUG)
                        .build())
                .build())
        NotificationService.schedule(this)
        initPrefs()
    }

    private fun initPrefs() {
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
        component.migrateTo127.migrateIfNeeded()
    }

    open fun createComponent(): ApplicationComponent =
            DaggerApplicationComponent.builder()
                    .contextModule(ContextModule(this))
                    .databaseModule(DatabaseModule(this))
                    .build()

}
