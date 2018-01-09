package de.reiss.bible2net.theword.architecture.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.Rfc3339DateJsonAdapter
import dagger.Module
import dagger.Provides
import de.reiss.bible2net.theword.downloader.BASE_URL
import de.reiss.bible2net.theword.downloader.list.TwdService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*

@Module(
        includes = [
            OkHttpModule::class
        ]
)
open class RetrofitModule {

    @Provides
    @ApplicationScope
    open fun moshi(): Moshi = Moshi.Builder()
            .add(Date::class.java, Rfc3339DateJsonAdapter())
            .build()

    @Provides
    @ApplicationScope
    open fun retrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder().
            baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @ApplicationScope
    open fun twdService(okHttpClient: OkHttpClient, retrofit: Retrofit): TwdService =
            retrofit.create(TwdService::class.java)

}