package de.reiss.bible2net.theword.architecture.di

import dagger.Module
import dagger.Provides
import de.reiss.bible2net.theword.downloader.file.FileDownloader
import de.reiss.bible2net.theword.downloader.list.ListDownloader
import de.reiss.bible2net.theword.downloader.list.TwdService
import okhttp3.OkHttpClient

@Module(
        includes = [
            OkHttpModule::class,
            RetrofitModule::class
        ]
)
open class DownloaderModule {

    @Provides
    @ApplicationScope
    open fun fileDownloader(okHttpClient: OkHttpClient): FileDownloader =
            FileDownloader(okHttpClient)

    @Provides
    @ApplicationScope
    open fun listDownloader(twdService: TwdService): ListDownloader =
            ListDownloader(twdService)

}