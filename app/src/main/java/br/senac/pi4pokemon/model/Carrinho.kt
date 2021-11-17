package br.senac.pi4pokemon.model

data class Carrinho(
    val preco: String,
    val updated_at: String,
    val user_id: Int,
    val imagem: String,
    val created_at: String,
    val nome: String,
    val id: Int,
    val produto_id: Int,
    val quantidade: Int,
    val descricao: String

)
