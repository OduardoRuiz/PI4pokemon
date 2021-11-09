package br.senac.pi4pokemon.services

import br.senac.pi4pokemon.model.Produto
import retrofit2.Call
import retrofit2.http.*


interface ProdutoService {

   //lista todos os produtos
   @GET("/api/produtos")
   fun listarProdutos(): Call<List<Produto>>

   //Busca produtos por nome
  // @GET("/api/produtos/buscar/{nome}")
   //fun pesquisarProdutos(@Path("nome") nome: String): Call<List<Produto>>

   //Busca produtos por nome
    @GET("/api/produto/2")
   fun pesquisarProdutos(): Call<List<Produto>>



   //Busca produto por categoria
   @GET("/api/produtos/categoria/buscar/Terra")
   fun pesquisarCategoria() : Call<List<Produto>>





}

