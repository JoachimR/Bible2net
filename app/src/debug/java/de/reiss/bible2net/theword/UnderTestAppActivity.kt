package de.reiss.bible2net.theword

import android.os.Bundle
import android.widget.FrameLayout
import de.reiss.bible2net.theword.architecture.AppActivity

class UnderTestAppActivity : AppActivity() {

    lateinit var contentView: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contentView = FrameLayout(this)
        contentView.id = R.id.under_test_content_view
        setContentView(contentView)
        this.contentView = contentView
    }

}
