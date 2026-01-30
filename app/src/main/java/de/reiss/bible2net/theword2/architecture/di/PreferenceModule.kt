package de.reiss.bible2net.theword2.architecture.di

import android.content.Context
import dagger.Module
import dagger.Provides
import de.reiss.bible2net.theword2.preferences.AppPreferences

@Module(
    includes = [
        ContextModule::class
    ]
)
class PreferenceModule {

    @Provides
    @ApplicationScope
    fun appPreferences(context: Context): AppPreferences = AppPreferences(context)
}
