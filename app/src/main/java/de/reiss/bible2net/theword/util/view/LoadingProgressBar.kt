package de.reiss.bible2net.theword.util.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ProgressBar

class LoadingProgressBar : ProgressBar {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun setLoading(loading: Boolean) {
        if (loading) {
            this.visibility = VISIBLE
        } else {
            this.visibility = GONE
        }
    }
}
