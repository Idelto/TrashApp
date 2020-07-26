package com.trash2money.trashapp.KClasses

class Location(
    val street: String,
    val zip: String,
    val neighbord: String?,
    val city: String,
    val State: String,
    val num: String,
    val comp: String,
    val refPoint: String,
    val lat: Double,
    val lng: Double
) {
    constructor() : this("", "", "", "", "", "", "", "", 0.0, 0.0)
}