package de.reiss.bible2net.theword2.architecture.di

import android.content.Context
import dagger.Module
import dagger.Provides
import de.reiss.bible2net.theword2.architecture.UserAgentInterceptor
import de.reiss.bible2net.theword2.util.appVersion
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File

@Module(
    includes = [
        ContextModule::class
    ]
)
open class OkHttpModule {

    @Provides
    @ApplicationScope
    open fun cacheFile(context: Context): File {
        return File(context.cacheDir, "okhttp").apply {
            mkdirs()
        }
    }

    @Provides
    @ApplicationScope
    open fun cache(cacheFile: File): Cache =
        Cache(cacheFile, (16 * 1024 * 1024).toLong())

    @Provides
    @ApplicationScope
    open fun okHttpClient(context: Context, cache: Cache): OkHttpClient =
        OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .addInterceptor(UserAgentInterceptor("The Word", appVersion(context)))
            .build()
}
