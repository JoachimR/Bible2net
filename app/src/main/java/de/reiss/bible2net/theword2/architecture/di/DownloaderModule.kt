package de.reiss.bible2net.theword2.architecture.di

import dagger.Module
import dagger.Provides
import de.reiss.bible2net.theword2.downloader.file.FileDownloader
import de.reiss.bible2net.theword2.downloader.list.ListDownloader
import de.reiss.bible2net.theword2.downloader.list.TwdService
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
