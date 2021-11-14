package br.senac.pi4pokemon.services

import br.senac.pi4pokemon.model.Carrinho
import br.senac.pi4pokemon.model.CarrinhoTeste

import retrofit2.Call
import retrofit2.http.GET

interface CarrinhoService {


    //lista todos os produtos
    @GET("/api/carrinho/show")
    fun mostraCarrinho(): Call<List<Carrinho>>
}

