package br.senac.pi4pokemon.services

import br.senac.pi4pokemon.model.Token
import br.senac.pi4pokemon.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET


import retrofit2.http.POST


interface LoginService {

    @POST("/api/login")
    fun fazerLogin(@Body user: User): Call<Token>

}