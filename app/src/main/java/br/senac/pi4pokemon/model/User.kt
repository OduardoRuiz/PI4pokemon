package br.senac.pi4pokemon.model

data class User(
	val isAdmin: Int?=null,
	val updatedAt: String?=null,
	val name: String?=null,
	val createdAt: String?=null,
	val emailVerifiedAt: Any?=null,
	val id: Int?=null,
	val email: String?=null,
	val password: String?=null,
)
