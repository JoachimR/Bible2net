package de.reiss.bible2net.theword.architecture.di

import android.content.Context
import dagger.Module
import dagger.Provides
import de.reiss.bible2net.theword.preferences.AppPreferences

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