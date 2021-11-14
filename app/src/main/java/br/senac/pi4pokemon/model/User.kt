package br.senac.pi4pokemon.model

data class User(
	val isAdmin: Int,
	val updatedAt: String,
	val name: String,
	val createdAt: String,
	val emailVerifiedAt: Any,
	val id: Int,
	val email: String
)
