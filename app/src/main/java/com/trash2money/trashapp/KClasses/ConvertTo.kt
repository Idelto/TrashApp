package com.trash2money.trashapp.KClasses

import java.math.RoundingMode
import java.text.DecimalFormat

class ConvertTo() {

    fun convert2Bigdecimal(numero : Double): Double {

        numero.toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toDouble()

        return numero
    }

    fun convert2casas(numero : Double): String {

        val decf = DecimalFormat("0.00")
        decf.roundingMode = RoundingMode.CEILING

        val convert =  decf.format(numero)

        return convert
    }

    fun convert2casasTrashCoins(numero : Double): String {

        val decf = DecimalFormat("0.00")
        decf.roundingMode = RoundingMode.CEILING

        val convert =  decf.format(numero)
        val final = convert.replace(",", ".")
        return final
    }


}