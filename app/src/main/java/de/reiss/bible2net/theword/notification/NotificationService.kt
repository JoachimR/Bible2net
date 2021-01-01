package de.reiss.bible2net.theword.notification

import android.content.Context
import com.google.android.gms.gcm.GcmNetworkManager
import com.google.android.gms.gcm.GcmNetworkManager.RESULT_SUCCESS
import com.google.android.gms.gcm.GcmTaskService
import com.google.android.gms.gcm.PeriodicTask
import com.google.android.gms.gcm.TaskParams
import de.reiss.bible2net.theword.App

class NotificationService : GcmTaskService() {

    companion object {

        private const val NOTIFICATION_TAG = "DailyTheWord"

        fun schedule(context: Context) {
            GcmNetworkManager.getInstance(context)
                .schedule(
                    PeriodicTask.Builder()
                        .setService(NotificationService::class.java)
                        .setTag(NOTIFICATION_TAG)
                        .setPeriod(86400) // 24 hours
                        .setFlex(3600) // 1 hour
                        .build()
                )
        }
    }

    private lateinit var helper: NotificationHelper

    override fun onCreate() {
        super.onCreate()
        helper = App.component.notificationHelper
    }

    override fun onRunTask(taskParams: TaskParams): Int {
        helper.tryShowNotification()
        return RESULT_SUCCESS // avoids rescheduling
    }
}
