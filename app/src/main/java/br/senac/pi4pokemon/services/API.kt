package br.senac.pi4pokemon.services

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object API {

    private const val baseUrl = "http://10.0.2.2:8000"
    private const val timeout = 30L

    private val retrofit: Retrofit
        get() {
             val okHttp = OkHttpClient.Builder()
                 .connectTimeout(timeout, TimeUnit.SECONDS)
                 .readTimeout(timeout, TimeUnit.SECONDS)
                 .build()

            return Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttp)
                .build()
        }
    val pokemon: ProdutoService
    get() {
        return retrofit.create(ProdutoService::class.java)
    }


}