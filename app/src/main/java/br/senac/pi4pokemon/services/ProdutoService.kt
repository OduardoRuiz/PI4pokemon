package br.senac.pi4pokemon.services

import br.senac.pi4pokemon.model.Produto
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.http.*


interface ProdutoService {

   //lista todos os produtos
   @GET("/api/produtos")
   fun listarProdutos(): Call<List<Produto>>

   //Busca produtos por nome
   @GET("/api/produtos/buscar/{nome}")
   fun pesquisarProdutosNome(@Path("nome") nome: String): Call<List<Produto>>

   //Busca produtos por id
    @GET("/api/produto/{id}")
   fun pesquisarProdutos(@Path("id")id: Int): Call<Produto>


   //Busca produto por categoria
   @GET("/api/produtos/categoria/buscar/{categoria}")
   fun pesquisarCategoria(@Path("categoria")  categoria: String) : Call<List<Produto>>

   //obert um produto par ao qrcode

   @GET("/api/produto/{id}")
   fun obterProdutoId(@Path("id")id: Int): Call<Produto>

   @GET("/api/destaque")
   fun produtosDestaques(): Call<List<Produto>>







}

