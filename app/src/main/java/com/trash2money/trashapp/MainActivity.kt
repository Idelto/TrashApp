package com.trash2money.trashapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        Log.d("anal", mFirebaseAnalytics.firebaseInstanceId)

    }
}