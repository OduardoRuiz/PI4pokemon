package br.senac.pi4pokemon.services

import br.senac.pi4pokemon.model.Pedidos


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PedidoService {
    @GET("/api/pedidos/show/")
    fun listarPedidosUser(): Call<List<Pedidos>>

    @GET("/api/pedidos/ultimo")
    fun listarUltimoPedido(): Call<List<Pedidos>>

}