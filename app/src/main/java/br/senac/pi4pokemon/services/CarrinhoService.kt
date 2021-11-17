package br.senac.pi4pokemon.services

import br.senac.pi4pokemon.model.Carrinho
import br.senac.pi4pokemon.model.Produto


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface CarrinhoService {


    //lista todos os produtos
    @GET("/api/carrinho/show")
    fun mostraCarrinho(): Call<List<Carrinho>>

    //Add produto ao carrinho
    @POST("/api/carrinho/add/{id}")
    fun addProdutoCarrinho(@Path("id")id: Int): Call<Void>

    //Remove produto carrinho
    @POST("/api/carrinho/remove/{id}")
    fun removeProdutoCarrinho(@Path("id")id: Int): Call<Void>


    //Finalizar pedido
    @POST(" /api/pedidos/add/")
    fun FinalizaPedido(): Call<Void>


}

