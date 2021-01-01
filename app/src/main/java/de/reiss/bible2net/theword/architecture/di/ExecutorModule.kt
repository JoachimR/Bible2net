package de.reiss.bible2net.theword.architecture.di

import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Module
open class ExecutorModule {

    @Provides
    @ApplicationScope
    fun executor(): Executor = Executors.newFixedThreadPool(5)
}
