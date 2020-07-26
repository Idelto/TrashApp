package com.trash2money.trashapp.KClasses

import android.content.Context

class InfoIntro(val context: Context) {

    fun updateIntroStatus(status: Boolean) {
        context.getSharedPreferences("PREF", Context.MODE_PRIVATE).edit()
            .putBoolean("status", status).apply()
    }

    fun isIntroActivityShow() =
        context.getSharedPreferences("PREF", Context.MODE_PRIVATE).getBoolean("status", false)
}