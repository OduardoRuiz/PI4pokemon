package br.senac.pi4pokemon.model

data class Carrinho(
    val preco: String,
    val updatedAt: String,
    val userId: Int,
    val imagem: String,
    val createdAt: String,
    val nome: String,
    val id: Int,
    val produtoId: Int,
    val quantidade: Int,
    val descricao: String

)
