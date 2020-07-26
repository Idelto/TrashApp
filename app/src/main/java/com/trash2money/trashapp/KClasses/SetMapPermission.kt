package com.trash2money.trashapp.KClasses

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.trash2money.trashapp.Activities.Maps.MapsActivity
import com.trash2money.trashapp.R


class SetMapPermission {

    private val REQUEST_COARSE_LOCATION_CODE = 1

    fun setPermission(context: Context, baseContext: Activity, targetActivity: Activity){

        var baseContext = baseContext

        val permission  = ContextCompat.checkSelfPermission(baseContext, android.Manifest.permission.ACCESS_FINE_LOCATION)

        if (permission != PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "Permission denied" )
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(baseContext,android.Manifest.permission.ACCESS_FINE_LOCATION)){
            val basecontext = baseContext

            var builder = AlertDialog.Builder(basecontext)

            val dialogView = LayoutInflater.from(baseContext).inflate(R.layout.dialog_permission,null)
            builder.setCancelable(false)
            builder.setView(dialogView)

            var btnPositive = dialogView.findViewById<Button>(R.id.btnDialogCouponYes)
            var dialog = builder.create()

            btnPositive.setOnClickListener(){
                Log.d(TAG, "Clicked")
                makeRequest(basecontext, targetActivity)
                val intent = Intent(baseContext, targetActivity::class.java)
                baseContext.startActivity(intent)
                dialog.cancel()
            }

            dialog.show()

        }
        else{
            makeRequest(baseContext, targetActivity)
        }
    }

    private fun makeRequest(baseContext: Activity, targetActivity: Activity){
        ActivityCompat.requestPermissions(baseContext, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),REQUEST_COARSE_LOCATION_CODE)
    }

}

