package br.senac.pi4pokemon.services


import android.media.Image
import br.senac.pi4pokemon.model.User
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface UserService {

    @GET("/api/user")
    fun listarPerfil(): Call<User>

    @GET("/api/logoff/")
    fun logoff(): Call<Void>

    @POST("/api/registrar")
    fun registrarUser(@Body user: User): Call<User>

    @Multipart
    @POST("/api/user/avatar")
    fun uploadfoto(@Part("imagem") image: MultipartBody.Part): Call<Void>






}