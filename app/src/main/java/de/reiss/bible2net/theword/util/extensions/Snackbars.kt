package de.reiss.bible2net.theword.util.extensions

import android.app.Activity
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import de.reiss.bible2net.theword.R

fun Activity.showShortSnackbar(
    message: String,
    action: (() -> Unit)? = null,
    actionLabel: String? = null,
    callback: (() -> Unit)? = null
) =
    doShowSnackbar(this, message, Snackbar.LENGTH_SHORT, action, actionLabel, callback)

fun Activity.showLongSnackbar(
    message: String,
    action: (() -> Unit)? = null,
    actionLabel: String? = null,
    callback: (() -> Unit)? = null
) =
    doShowSnackbar(this, message, Snackbar.LENGTH_LONG, action, actionLabel, callback)

fun Activity.showIndefiniteSnackbar(
    message: String,
    action: (() -> Unit)? = null,
    actionLabel: String? = null,
    callback: (() -> Unit)? = null
) =
    doShowSnackbar(this, message, Snackbar.LENGTH_INDEFINITE, action, actionLabel, callback)

fun Fragment.showShortSnackbar(
    message: String,
    action: (() -> Unit)? = null,
    actionLabel: String? = null,
    callback: (() -> Unit)? = null
) {
    activity?.let {
        doShowSnackbar(it, message, Snackbar.LENGTH_SHORT, action, actionLabel, callback)
    }
}

fun Fragment.showLongSnackbar(
    message: String,
    action: (() -> Unit)? = null,
    actionLabel: String? = null,
    callback: (() -> Unit)? = null
) {
    activity?.let {
        doShowSnackbar(it, message, Snackbar.LENGTH_LONG, action, actionLabel, callback)
    }
}

fun Activity.showShortSnackbar(
    @StringRes message: Int,
    action: (() -> Unit)? = null,
    actionLabel: String? = null,
    callback: (() -> Unit)? = null
) =
    doShowSnackbar(
        this,
        getString(message),
        Snackbar.LENGTH_SHORT,
        action,
        actionLabel,
        callback
    )

fun Activity.showLongSnackbar(
    @StringRes message: Int,
    action: (() -> Unit)? = null,
    actionLabel: String? = null,
    callback: (() -> Unit)? = null
) =
    doShowSnackbar(
        this,
        getString(message),
        Snackbar.LENGTH_LONG,
        action,
        actionLabel,
        callback
    )

fun Activity.showIndefiniteSnackbar(
    @StringRes message: Int,
    action: (() -> Unit)? = null,
    actionLabel: String? = null,
    callback: (() -> Unit)? = null
) =
    doShowSnackbar(
        this,
        getString(message),
        Snackbar.LENGTH_INDEFINITE,
        action,
        actionLabel,
        callback
    )

fun Fragment.showShortSnackbar(
    @StringRes message: Int,
    action: (() -> Unit)? = null,
    actionLabel: String? = null,
    callback: (() -> Unit)? = null
) = activity?.let {
    doShowSnackbar(it, getString(message), Snackbar.LENGTH_SHORT, action, actionLabel, callback)
}

fun Fragment.showLongSnackbar(
    @StringRes message: Int,
    action: (() -> Unit)? = null,
    actionLabel: String? = null,
    callback: (() -> Unit)? = null
) {
    activity?.let {
        doShowSnackbar(it, getString(message), Snackbar.LENGTH_LONG, action, actionLabel, callback)
    }
}

fun Fragment.showIndefiniteSnackbar(
    @StringRes message: Int,
    action: (() -> Unit)? = null,
    actionLabel: String? = null,
    callback: (() -> Unit)? = null
) {
    activity?.let {
        doShowSnackbar(
            it,
            getString(message),
            Snackbar.LENGTH_INDEFINITE,
            action,
            actionLabel,
            callback
        )
    }
}

private fun doShowSnackbar(
    activity: Activity,
    message: String,
    duration: Int,
    action: (() -> Unit)?,
    actionLabel: String? = null,
    callback: (() -> Unit)?
) {

    return Snackbar.make(
        activity.findViewById(android.R.id.content),
        message,
        duration
    ).apply {

        if (action != null) {
            if (actionLabel == null) {
                setAction(
                    R.string.snackbar_retry,
                    {
                        action()
                    }
                )
            } else {
                setAction(
                    actionLabel,
                    {
                        action()
                    }
                )
            }
        }

        if (callback != null) {
            addCallback(
                object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        super.onDismissed(transientBottomBar, event)
                        callback()
                    }
                }
            )
        }
    }.show()
}
