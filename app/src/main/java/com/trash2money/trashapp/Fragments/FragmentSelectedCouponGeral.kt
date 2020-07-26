package com.trash2money.trashapp.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.trash2money.trashapp.KClasses.CouponItem
import com.trash2money.trashapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class FragmentSelectedCouponGeral(coupon: String) : Fragment() {

    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var desc: TextView
    lateinit var imgCouponimg: ImageView
    lateinit var shortDesc: TextView
    lateinit var imgCouponImg2: ImageView
    lateinit var shortDescripition: TextView
    lateinit var codCodigoCupom: TextView
    lateinit var txt_validate: TextView
    val coupon = coupon

    @Nullable
    @Override
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.activity_selected_coupon, container, false) as View

        shortDesc = v.findViewById(R.id.txt_short_desc)
        imgCouponImg2 = v.findViewById(R.id.imgCouponImg2)
        shortDescripition = v.findViewById(R.id.txt_long_desc)
        codCodigoCupom = v.findViewById(R.id.txt_CodigoCupom_cod)
        txt_validate = v.findViewById(R.id.txt_validate)

        val couponId = coupon
        Log.d("tabGeral", " $couponId ")

        val currUser = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance().collection("user_activity")
            .document(currUser?.uid.toString()).collection("coupons")
        db.whereEqualTo("id", coupon).get().addOnSuccessListener { docs ->

            for (doc in docs) {

                FirebaseFirestore.getInstance().collection("user_activity")
                    .document(currUser?.uid.toString()).collection("coupons").document(doc.id).get()
                    .addOnSuccessListener { d ->

                        val item = d.toObject<CouponItem>(CouponItem::class.java)

                        shortDesc.setText(item?.brand)
                        shortDescripition.setText(item?.description)
                        codCodigoCupom.setText(item?.codcoupon)
                        txt_validate.setText(item?.validate)
                        Log.d("TagValidate", item?.validate)

                        val myStorage = FirebaseStorage.getInstance().reference
                        val targetUrl = myStorage.toString() + "coupon_logo/" + item?.img
                        val storageRef = myStorage.storage.getReferenceFromUrl(targetUrl)

                        storageRef.downloadUrl.addOnSuccessListener { url ->
                            Log.d("URL", url.toString())
                            val imageUrl = url
                            Glide.with(context!!).load(imageUrl).into(imgCouponImg2)

                        }

                    }
            }

        }

        return v
    }

}