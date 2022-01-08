package br.senac.pi4pokemon.services

import br.senac.pi4pokemon.model.Categoria
import br.senac.pi4pokemon.model.Produto

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CategoriaService {

    //Mostra todas Categorias
    @GET("/api/categorias/")
    fun listarCategoria(): Call<List<Categoria>>

    @GET("/api/categorias/{id}")
    fun obterCategoriaId(@Path("id")id: Int): Call<List<Produto>>



}