package br.senac.pi4pokemon.model

data class Produto(

	val preco: String,
	val updatedAt: String,
	val imagem: String,
	val createdAt: String,
	val nome: String,
	val id: Int,
	val categoria_id: Int,
	val quantidade: String,
	val deletedAt: String,
	val descricao: String,

)
