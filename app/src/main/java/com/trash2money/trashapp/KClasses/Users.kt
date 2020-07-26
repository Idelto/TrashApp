package com.trash2money.trashapp.KClasses

class Users(
    val name: String,
    val uId: String,
    val email: String,
    val celular: String,
    val fotoPerfil: String,
    val primeiroLogin: String,
    val urifoto: String
) {

    constructor() : this("", "", "", "", "", "", "")
}