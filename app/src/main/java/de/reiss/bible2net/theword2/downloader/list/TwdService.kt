package de.reiss.bible2net.theword2.downloader.list

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface TwdService {

    @GET("twd11/current?format=json")
    fun list(): Call<List<Twd11>>

    @GET
    fun downloadTwd(@Url fileUrl: String): Call<ResponseBody>
}
