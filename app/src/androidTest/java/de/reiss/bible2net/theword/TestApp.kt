package de.reiss.bible2net.theword

import de.reiss.bible2net.theword.util.view.FadingProgressBar


class TestApp : App() {

    override fun initApp() {
        FadingProgressBar.isTestRunning = true
    }

}
