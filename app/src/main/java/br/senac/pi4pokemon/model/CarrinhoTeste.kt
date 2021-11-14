package br.senac.pi4pokemon.model

data class CarrinhoTeste(
	val carrinhoTeste: List<CarrinhoTesteItem>
)
data class CarrinhoTesteItem(
	val nome: String,
	val preco: String
)