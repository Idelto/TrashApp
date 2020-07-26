package com.trash2money.trashapp.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.trash2money.trashapp.KClasses.CouponItem
import com.trash2money.trashapp.KClasses.GetCoupon
import com.trash2money.trashapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FragmentCouponDetailHow(coupon: String) : Fragment() {

    lateinit var recycler_onGoing: RecyclerView
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var how: TextView
    val coupon = coupon

    @Nullable
    @Override
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_coupon_detail_tab_how, container, false) as View
        how = v.findViewById(R.id.txt_how)

        GetCoupon().readCoupon(object : GetCoupon.MyCallbackCoupon {
            override fun onCallBack(coupon: CouponItem) {

                val html = coupon.how
                how.setText(Html.fromHtml(html))

                val content = coupon.how
                val urlRef =
                    FirebaseFirestore.getInstance().collection("coupons").document(coupon.id)
                        .collection("how_uri").document("uri")

                urlRef.get().addOnSuccessListener { url ->

                    val urlLink = url["link"]
                    val urlText = url["text"].toString()

                    val a_init = urlText.toRegex()
                    val a_end = "</a>".toRegex()

                    val ss = SpannableString(Html.fromHtml(content))
                    val init = a_init.find(ss)!!.range.first
                    val end = a_init.find(ss)!!.range.last + 1

                    Log.d("a_init", init.toString())
                    Log.d("a_end", end.toString())

                    val clickableSpan: ClickableSpan = object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse(urlLink.toString())
                            startActivity(intent)
                        }
                    }

                    ss.setSpan(clickableSpan, init, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    how.setText(ss)
                    how.movementMethod = LinkMovementMethod.getInstance()

                }
            }

        }, coupon)

        return v
    }

}