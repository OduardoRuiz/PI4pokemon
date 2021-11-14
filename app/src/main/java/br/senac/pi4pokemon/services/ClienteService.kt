package br.senac.pi4pokemon.services

import br.senac.pi4pokemon.model.User
import retrofit2.Call
import retrofit2.http.GET

interface ClienteService {

    @GET("/api/login")
    fun listar(): Call<List<User>>
}