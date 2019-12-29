package de.reiss.bible2net.theword.testutil

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import de.reiss.bible2net.theword.TestApp


class AppTestRunner : AndroidJUnitRunner() {

    @Throws(InstantiationException::class, IllegalAccessException::class, ClassNotFoundException::class)
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application =
            super.newApplication(cl, TestApp::class.java.canonicalName, context)

}
