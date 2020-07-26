package com.trash2money.trashapp.KClasses

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TAG = "FCM"

class SendNotification {

    fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {

        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if (response.isSuccessful) {
                Log.d(TAG, "Response: ${Gson().toJson(response)}")
            } else {
                Log.e(TAG, response.errorBody().toString())
            }

        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }

    }

}