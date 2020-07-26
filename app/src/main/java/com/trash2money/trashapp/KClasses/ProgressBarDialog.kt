package com.trash2money.trashapp.KClasses

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.trash2money.trashapp.R

class ProgressBarDialog(context: Context) {
    var vContext = context

    val mDialogView = LayoutInflater.from(vContext).inflate(R.layout.dialog_progressbar, null)
    val mBuilder = AlertDialog.Builder(vContext).setView(mDialogView).setCancelable(false)
    var mAlertDialog = mBuilder.create() as AlertDialog

    fun openDialog() {
        mAlertDialog.show()
    }

    fun closeDialog() {
        mAlertDialog.dismiss()
    }

}