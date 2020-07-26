package com.trash2money.trashapp.Fragments

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.trash2money.trashapp.KClasses.CouponItem
import com.trash2money.trashapp.KClasses.GetMyCoupon
import com.trash2money.trashapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FragmentSelectedCouponRules(coupon: String) : Fragment() {

    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var rules: TextView
    val coupon = coupon

    @Nullable
    @Override
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val v =
            inflater.inflate(R.layout.fragment_coupon_detail_tab_rules, container, false) as View
        rules = v.findViewById(R.id.txt_rules)
        Log.d("tabRules", coupon)

        GetMyCoupon().readCoupon(object : GetMyCoupon.MyCallbackCoupon {
            override fun onCallBack(coupon: CouponItem) {

                val html = coupon.rules
                rules.setText(Html.fromHtml(html))

            }
        }, coupon)

        return v
    }

}