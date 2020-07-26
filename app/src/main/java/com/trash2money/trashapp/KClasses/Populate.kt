package com.trash2money.trashapp.KClasses

import com.google.firebase.firestore.FirebaseFirestore

class Populate(
    val name: String,
    val gPerUnit: Double,
    val longDesc: String,
    val material: String,
    val shortDesc: String,
    val unity: String,
    val finalUnity: String,
    val material_desc: String
) {

    constructor() : this("", 0.0, "", "", "", "", "", "")

    fun poupulate(
        name: String,
        gPerUnit: Double,
        longDesc: String,
        material: String,
        shortDesc: String,
        unity: String,
        finalUnity: String,
        material_desc: String
    ) {

        val material = material
        var material_desc = material_desc

        when (material) {

            "lata_aco" -> material_desc = "aço"
            "aluminio" -> material_desc = "alumínio"
            "papel_branco" -> material_desc = "papel"
            "papelao" -> material_desc = "papelão"
            "plastico_rigido" -> material_desc = "plastico"
            "isopor" -> material_desc = "isopor"
            "plastico_filme" -> material_desc = "plastico"
            "vidro" -> material_desc = "vidro"

        }

        val db = FirebaseFirestore.getInstance().collection("trash_products")
        val obj = Populate(name,
            gPerUnit,
            longDesc,
            material,
            shortDesc,
            unity,
            finalUnity,
            material_desc)
        db.document(name).set(obj)

    }

}