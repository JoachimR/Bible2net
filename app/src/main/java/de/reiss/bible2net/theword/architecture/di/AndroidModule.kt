package de.reiss.bible2net.theword.architecture.di

import android.app.NotificationManager
import android.content.Context
import dagger.Module
import dagger.Provides


@Module(includes = arrayOf(ContextModule::class))
class AndroidModule {

    @Provides
    @ApplicationScope
    fun notificationManager(context: Context) =
            (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)

}