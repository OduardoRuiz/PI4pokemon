package br.senac.pi4pokemon.services


import br.senac.pi4pokemon.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {

    @GET("/api/user")
    fun listarPerfil(): Call<User>

    @GET("/api/logoff/")
    fun logoff(): Call<Void>

    @POST("/api/registrar")
    fun registrarUser(@Body user: User): Call<User>
}