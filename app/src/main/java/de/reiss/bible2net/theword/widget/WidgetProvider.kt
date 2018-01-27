package de.reiss.bible2net.theword.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import de.reiss.bible2net.theword.App
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.SplashScreenActivity
import de.reiss.bible2net.theword.logger.logErrorWithCrashlytics
import de.reiss.bible2net.theword.preferences.AppPreferences

class WidgetProvider : AppWidgetProvider() {

    companion object {

        private const val REQUEST_CODE_CLICK_WIDGET = 13

        fun refresh() {
            val context = App.component.context
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                    ComponentName(context, WidgetProvider::class.java))

            // send layout change update
            context.sendBroadcast(Intent(context, WidgetProvider::class.java)
                    .putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
                    .setAction("android.appwidget.action.APPWIDGET_UPDATE"))

            // send data change update
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view)
        }
    }

    private val appPreferences: AppPreferences by lazy {
        App.component.appPreferences
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        AppWidgetManager.getInstance(context).apply {
            notifyAppWidgetViewDataChanged(
                    getAppWidgetIds(ComponentName(context, WidgetProvider::class.java)),
                    R.id.widget_list_view)
        }
    }

    override fun onUpdate(context: Context,
                          appWidgetManager: AppWidgetManager,
                          appWidgetIds: IntArray) {
        try {
            for (id in appWidgetIds) {
                appWidgetManager.updateAppWidget(id, updateWidgetListView(context, id))
            }
            super.onUpdate(context, appWidgetManager, appWidgetIds)
        } catch (e: Exception) {
            logErrorWithCrashlytics(e) { "Error when updating widget" }
        }
    }

    private fun updateWidgetListView(context: Context, appWidgetId: Int): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, R.layout.widget_layout)

        // RemoteViews Service needed to provide adapter for ListView
        val serviceIntent = Intent(context, WidgetService::class.java)
                .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        serviceIntent.data = Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME))

        // setting adapter to listView of the widget
        remoteViews.setRemoteAdapter(R.id.widget_list_view, serviceIntent)

        setClickMethod(context, remoteViews)
        setBackground(context, remoteViews)
        return remoteViews
    }

    private fun setClickMethod(context: Context, remoteViews: RemoteViews) {
        val pendingIntent = PendingIntent.getActivity(context, REQUEST_CODE_CLICK_WIDGET,
                SplashScreenActivity.createIntent(context), PendingIntent.FLAG_UPDATE_CURRENT)
        remoteViews.setPendingIntentTemplate(R.id.widget_list_view, pendingIntent)
    }

    private fun setBackground(context: Context, remoteViews: RemoteViews) {
        val identifier = context.resources.getIdentifier(appPreferences.widgetBackground(),
                "drawable", context.packageName)
        remoteViews.setImageViewResource(R.id.widget_background, identifier)
    }

}