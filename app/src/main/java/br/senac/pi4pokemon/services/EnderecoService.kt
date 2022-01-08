package br.senac.pi4pokemon.services

import br.senac.pi4pokemon.model.Endereco
import br.senac.pi4pokemon.model.Token
import br.senac.pi4pokemon.model.User

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface EnderecoService {

        @GET("/api/endereco/show")
        fun listaEndereco(): Call<List<Endereco>>

        @POST("/api/update/endereco")
        fun editaEndereco(@Body endereco: Endereco): Call<Endereco>

        @POST("/api/endereco/")
        fun cadastraEndereco(@Body endereco: Endereco): Call<Endereco>


}