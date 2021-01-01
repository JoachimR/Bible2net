package de.reiss.bible2net.theword.testutil

import java.util.concurrent.Executor

class TestExecutor : Executor {

    override fun execute(command: Runnable) {
        command.run()
    }
}
