package de.reiss.bible2net.theword2.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.Html
import android.text.Spanned
import de.reiss.bible2net.theword2.App
import de.reiss.bible2net.theword2.R
import de.reiss.bible2net.theword2.formattedDate
import de.reiss.bible2net.theword2.model.TheWordContent

fun appVersion(context: Context): String {
    val version = context.packageManager.getPackageInfo(context.packageName, 0).versionName
    return context.getString(R.string.app_version, version)
}

fun htmlize(text: String): Spanned =
    Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)

fun contentAsString(context: Context, time: Long, theWordContent: TheWordContent, note: String) =
    StringBuilder().apply {
        append(formattedDate(context, time))
        append("\n\n")

        if (theWordContent.intro1.isNotEmpty()) {
            append(theWordContent.intro1)
            append("\n\n")
        }
        append(theWordContent.text1)
        append("\n")
        append(theWordContent.ref1)
        append("\n\n")

        if (theWordContent.intro2.isNotEmpty()) {
            append(theWordContent.intro2)
            append("\n\n")
        }
        append(theWordContent.text2)
        append("\n")
        append(theWordContent.ref2)
        if (note.isNotEmpty()) {
            append("\n\n")
            append(note)
        }
    }.toString()

fun copyToClipboard(context: Context, text: String) {
    clipboardManager.setPrimaryClip(
        ClipData.newPlainText(context.getString(R.string.app_name), text)
    )
}

private val clipboardManager: ClipboardManager by lazy {
    App.component.clipboardManager
}
