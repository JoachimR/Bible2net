package de.reiss.bible2net.theword.architecture.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
open class ContextModule(context: Context) {

    private val applicationContext: Context = context.applicationContext

    @Provides
    @ApplicationScope
    open fun context(): Context = applicationContext
}
