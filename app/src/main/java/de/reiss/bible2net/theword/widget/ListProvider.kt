package de.reiss.bible2net.theword.widget

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import de.reiss.bible2net.theword.App
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.formattedDate
import de.reiss.bible2net.theword.logger.logErrorWithCrashlytics
import de.reiss.bible2net.theword.logger.logWarnWithCrashlytics
import de.reiss.bible2net.theword.model.TheWord
import de.reiss.bible2net.theword.preferences.AppPreferences
import de.reiss.bible2net.theword.util.htmlize
import java.util.*

class ListProvider(private val context: Context) : RemoteViewsService.RemoteViewsFactory {

    private val list = ArrayList<CharSequence>()

    private val repository: WidgetRepository by lazy {
        App.component.widgetRepository
    }

    private val appPreferences: AppPreferences by lazy {
        App.component.appPreferences
    }

    init {
        refreshTheWordForWidgets()
    }

    private fun refreshTheWordForWidgets() {
        repository.loadWordForToday(
                onWordLoaded = {
                    applyLoadedText(
                            if (it == null) {
                                context.getString(R.string.no_content)
                            } else {
                                widgetText(it, includeDate = appPreferences.widgetShowDate())
                            }
                    )
                })
    }

    private fun applyLoadedText(it: String) {
        list.clear()
        list.add(htmlize(it))
    }

    override fun onCreate() {
        refreshTheWordForWidgets()
    }

    override fun onDestroy() {
    }

    override fun onDataSetChanged() {
        refreshTheWordForWidgets()
    }

    override fun getCount() = list.size

    override fun getItemId(position: Int) = position.toLong()

    override fun hasStableIds() = false

    override fun getLoadingView() = null

    override fun getViewTypeCount() = 1

    override fun getViewAt(position: Int): RemoteViews {
        val remoteViewRow = RemoteViews(context.packageName, R.layout.widget_layout_list_row)
        try {
            applyUi(remoteViewRow)
        } catch (e: Exception) {
            logErrorWithCrashlytics(e) { "Error when trying to set widget" }
        }
        return remoteViewRow
    }

    private fun applyUi(remoteViewRow: RemoteViews) {
        val list = list
        val item = if (list.isNotEmpty()) list.first() else {
            logWarnWithCrashlytics { "widget list is empty when trying to apply" }
            return
        }

        remoteViewRow.apply {

            val widgetCentered = appPreferences.widgetCentered()

            val textView = if (widgetCentered)
                R.id.tv_widget_content_centered
            else
                R.id.tv_widget_content_uncentered

            setViewVisibility(R.id.tv_widget_content_centered,
                    if (widgetCentered) View.VISIBLE else View.GONE)
            setViewVisibility(R.id.tv_widget_content_uncentered,
                    if (widgetCentered) View.GONE else View.VISIBLE)

            setTextViewText(textView, item)

            setTextColor(textView, appPreferences.widgetFontColor())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setTextViewTextSize(textView, TypedValue.COMPLEX_UNIT_SP,
                        appPreferences.widgetFontSize())
            } else {
                setFloat(textView, "setTextSize", appPreferences.widgetFontSize())
            }

            // widget click event
            setOnClickFillInIntent(R.id.widget_row_root, Intent())

            // background color set in WidgetProvider
        }
    }

    private fun widgetText(theWord: TheWord, includeDate: Boolean): String {
        return StringBuilder().apply {
            if (includeDate) {
                append(formattedDate(context = context, time = theWord.date.time))
                append("<br><br>")
            }

            if (theWord.content.intro1.isNotEmpty()) {
                append(theWord.content.intro1)
                append("<br><br>")
            }
            append(theWord.content.text1)
            append("<br>")
            append(theWord.content.ref1)
            append("<br><br>")

            if (theWord.content.intro2.isNotEmpty()) {
                append(theWord.content.intro2)
                append("<br><br>")
            }
            append(theWord.content.text2)
            append("<br>")
            append(theWord.content.ref2)
        }.toString()
    }


}