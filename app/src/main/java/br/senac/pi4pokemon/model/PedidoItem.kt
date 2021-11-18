package br.senac.pi4pokemon.model

data class PedidoItem(
	val preco: String? = null,
	val updated_at: String? = null,
	val imagem: String? = null,
	val created_at: String? = null,
	val nome: String? = null,
	val id: Int? = null,
	val produto_id: Int? = null,
	val pedido_id: Int? = null,
	val quantidade: Int? = null,
	val descricao: String? = null
)

