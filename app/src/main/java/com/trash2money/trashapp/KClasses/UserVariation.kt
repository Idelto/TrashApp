package com.trash2money.trashapp.KClasses

class UserVariation(
    val saveTree: Float,
    val saveEnergy: Float,
    val saveWater: Float,
    val totalRecycled: Float
) {

    constructor() : this(0F, 0F, 0F, 0F)
}