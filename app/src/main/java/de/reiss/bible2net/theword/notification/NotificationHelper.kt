package de.reiss.bible2net.theword.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.SplashScreenActivity
import de.reiss.bible2net.theword.database.BibleItemDao
import de.reiss.bible2net.theword.database.TheWordItemDao
import de.reiss.bible2net.theword.database.converter.Converter
import de.reiss.bible2net.theword.formattedDate
import de.reiss.bible2net.theword.model.TheWord
import de.reiss.bible2net.theword.preferences.AppPreferences
import de.reiss.bible2net.theword.util.extensions.withZeroDayTime
import java.util.Date
import java.util.Random
import java.util.concurrent.Executor
import javax.inject.Inject

open class NotificationHelper @Inject constructor(
    private val context: Context,
    private val notificationManager: NotificationManager,
    private val executor: Executor,
    private val appPreferences: AppPreferences,
    private val theWordItemDao: TheWordItemDao,
    private val bibleItemDao: BibleItemDao
) {

    companion object {

        val NOTIFICATION_CHANNEL_ID = "DailyTheWord"
        val NOTIFICATION_CHANNEL_NAME = "Daily Word"

        val NOTIFICATION_ID = 7
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }
    }

    fun tryShowNotification() {
        if (appPreferences.shouldShowDailyNotification().not()) {
            return
        }
        val chosenBible = appPreferences.chosenBible ?: return

        executor.execute {

            bibleItemDao.find(chosenBible)?.let { bibleItem ->

                val item = theWordItemDao.byDate(bibleItem.id, Date().withZeroDayTime())
                Converter.theWordItemToTheWord(bibleItem.bible, item)?.let {
                    showNotification(it)
                }
            }
        }
    }

    private fun showNotification(theWord: TheWord) {
        notificationManager.notify(NOTIFICATION_ID, createNotification(context, theWord))
    }

    private fun createNotification(context: Context, theWord: TheWord) =
        NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_daily_word)
            .setContentTitle(formattedDate(context, theWord.date.time))
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(wordToText(theWord))
            )
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.mipmap.ic_launcher
                )
            )
            .setAutoCancel(true)
            .setContentIntent(pendingIntent(context))
            .build()

    private fun pendingIntent(context: Context) =
        PendingIntent.getActivity(
            context,
            createUniqueRequestCode(),
            SplashScreenActivity.createIntent(context)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

    private fun createUniqueRequestCode() = Random().nextInt(100)

    private fun wordToText(theWord: TheWord) =
        StringBuilder().apply {
            append(theWord.content.text1)
            append(" ")
            append(theWord.content.ref1)
            append("\n")
            append(theWord.content.text2)
            append(" ")
            append(theWord.content.ref2)
        }.toString()

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        notificationManager.createNotificationChannel(
            NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                enableLights(false)
                enableVibration(false)
                setShowBadge(false)
            }
        )
    }
}
