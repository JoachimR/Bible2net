package de.reiss.bible2net.theword.util.view


import android.animation.Animator
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ProgressBar

class FadingProgressBar : ProgressBar {

    companion object {

        var isTestRunning = false

    }

    var loading: Boolean = false
        set(value) {
            if (value) {
                show()
            } else {
                hide()
            }
        }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /**
     * avoid animated loading icon in UI tests
     */
    override fun setIndeterminateDrawable(drawable: Drawable?) {
        super.setIndeterminateDrawable(
                if (isTestRunning)
                    @Suppress("DEPRECATION")
                    context.resources.getDrawable(android.R.drawable.ic_media_play)
                else drawable)
    }

    private fun show() {
        this.alpha = 1f
        this.visibility = View.VISIBLE
    }

    private fun hide() {
        if (isTestRunning) {  // avoid animate() in UI tests
            this.alpha = 0f
            this.visibility = View.GONE
            return
        }

        this.animate()?.alpha(0f)?.setListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                this@FadingProgressBar.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })
    }

}
