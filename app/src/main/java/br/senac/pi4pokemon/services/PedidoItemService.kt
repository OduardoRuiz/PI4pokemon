package br.senac.pi4pokemon.services

import br.senac.pi4pokemon.model.PedidoItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PedidoItemService {
    //Busca itens do pedido
    @GET("/api/pedidos/itens/{id}")
    fun pesquisarItensDoPedido(@Path("id")id: Int): Call<List<PedidoItem>>
}