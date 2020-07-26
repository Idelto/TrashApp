package com.trash2money.trashapp.KClasses

class TrashData(
    val finalUnity: String,
    val gperUnit: Double,
    val longDesc: String,
    val material: String,
    val material_desc: String,
    val name: String,
    val shortDesc: String,
    val unity: String
) {

    constructor() : this("", 0.0, "", "", "", "", "", "")

}