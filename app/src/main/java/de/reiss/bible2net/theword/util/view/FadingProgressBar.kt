package de.reiss.bible2net.theword.util.view


import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.widget.ProgressBar

class FadingProgressBar : ProgressBar {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var loading: Boolean = false
        set(value) {
            if (value) {
                show()
            } else {
                hide()
            }
        }

    private fun show() {
        this.alpha = 1f
        this.visibility = VISIBLE
    }

    private fun hide() {
        this.animate()?.alpha(0f)?.setListener(
                object : Animator.AnimatorListener {

                    override fun onAnimationStart(animation: Animator) {}

                    override fun onAnimationEnd(animation: Animator) {
                        visibility = GONE
                    }

                    override fun onAnimationCancel(animation: Animator) {}

                    override fun onAnimationRepeat(animation: Animator) {}

                })
    }

}
