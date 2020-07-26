package com.trash2money.trashapp.Adapters

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.trash2money.trashapp.Fragments.*

class CouponDetailsAdapter(fm: FragmentManager, coupon: String) : FragmentPagerAdapter(fm) {

    var coupon = coupon

    override fun getItem(position: Int): Fragment {
        Log.d("POSITION", position.toString())
        return when (position) {
            0 -> FragmentCouponDetailDetails(coupon)
            1 -> FragmentCouponDetailRules(coupon)
            2 -> FragmentCouponDetailHow(coupon)
            else -> {
                return FragmentCouponDetailHow(coupon)
            }
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Detalhes"
            1 -> "Regras"
            2 -> "Como Usar"
            else -> {
                return "Como Usar"
            }
        }
    }
}