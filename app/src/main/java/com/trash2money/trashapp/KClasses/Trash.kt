package com.trash2money.trashapp.KClasses

class Trash(
    val type: String,
    val weight: Float,
    val quantity: Float,
    val volume: Float,
    val trashcoins: Float,
    val description: String,
    val trashid: String,
    val scheduleDate: Long
) {
    constructor() : this("", 0F, 0F, 0F, 0F, "", "", System.currentTimeMillis())
}