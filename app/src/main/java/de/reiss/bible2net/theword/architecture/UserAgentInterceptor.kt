package de.reiss.bible2net.theword.architecture

import android.os.Build
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.*

class UserAgentInterceptor(private val userAgent: String) : Interceptor {

    private val headerUserAgent = "User-Agent"

    constructor(appName: String, appVersion: String) : this(
            userAgent = String.format(Locale.US,
                    "%s/%s (Android %s; %s; %s %s; %s)",
                    appName,
                    appVersion,
                    Build.VERSION.RELEASE,
                    Build.MODEL,
                    Build.BRAND,
                    Build.DEVICE,
                    Locale.getDefault().language)
    )

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response =
            chain.proceed(chain.request()
                    .newBuilder()
                    .header(headerUserAgent, userAgent)
                    .build())

}