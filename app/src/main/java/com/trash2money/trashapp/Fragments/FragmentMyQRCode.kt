package com.trash2money.trashapp.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.trash2money.trashapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder

class FragmentMyQRCode : Fragment() {

    lateinit var qrCode: ImageView
    lateinit var txtTitle: TextView

    @Nullable
    @Override
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_my_qr, container, false)

        qrCode = v.findViewById<ImageView>(R.id.imgQRCode)
        txtTitle = v.findViewById<TextView>(R.id.txtMaterial)

        val user = FirebaseAuth.getInstance().currentUser?.uid
        val multiFormatWriter = MultiFormatWriter()
        val docRef =
            FirebaseFirestore.getInstance().collection("users").document(user.toString()).get()

        val fm: FragmentManager = activity!!.supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.bottom_bar_container, Fragment_bottom_bar()).commit()

        docRef.addOnSuccessListener { doc ->

            val name = doc["name"] as String
            txtTitle.setText("${name}, esse é o seu QRCode de identificação \uD83D\uDE09")

        }

        try {

            val bitMatrix = multiFormatWriter.encode(user, BarcodeFormat.QR_CODE, 2000, 2000)
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.createBitmap(bitMatrix)

            qrCode.setImageBitmap(bitmap)

        } catch (e: WriterException) {
            e.printStackTrace()

        }

        return (v)

    }

}
