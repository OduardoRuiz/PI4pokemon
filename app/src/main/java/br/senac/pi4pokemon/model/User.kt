package br.senac.pi4pokemon.model

data class User(
	val isAdmin: Int?=null,
	val updated_at: String?=null,
	val name: String?=null,
	val created_at: String?=null,
	val emailVerified_at: Any?=null,
	val id: Int?=null,
	val email: String?=null,
	val password: String?=null,
	val avatar: String?=null,

)
