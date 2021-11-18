package br.senac.pi4pokemon.services

import br.senac.pi4pokemon.model.Endereco

import retrofit2.Call
import retrofit2.http.GET

interface EnderecoService {

        @GET("/api/endereco/show")
        fun listraEndereco(): Call<List<Endereco>>


}