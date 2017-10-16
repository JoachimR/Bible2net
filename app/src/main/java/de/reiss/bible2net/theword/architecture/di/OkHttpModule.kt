package de.reiss.bible2net.theword.architecture.di

import android.content.Context
import com.grapesnberries.curllogger.CurlLoggerInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File

@Module(includes = arrayOf(ContextModule::class))
open class OkHttpModule() {

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
    open fun okHttpClient(cache: Cache): OkHttpClient =
            OkHttpClient.Builder()
                    .cache(cache)
                    .addInterceptor(CurlLoggerInterceptor("TheWord"))
                    .build()

}