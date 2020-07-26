package com.trash2money.trashapp.Adapters

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.trash2money.trashapp.Fragments.*

class SelectedCouponAdapter(fm: FragmentManager, coupon: String) : FragmentPagerAdapter(fm) {

    var coupon = coupon

    override fun getItem(position: Int): Fragment {
        Log.d("POSITION", position.toString())
        return when (position) {
            0 -> FragmentSelectedCouponGeral(coupon)
            1 -> FragmentSelectedCouponDetail(coupon)
            2 -> FragmentSelectedCouponRules(coupon)
            else -> {
                return FragmentSelectedCouponHow(coupon)
            }
        }
    }

    override fun getCount(): Int {
        return 4
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Cupom"
            1 -> "Detalhes"
            2 -> "Regras"
            else -> {
                return "Como Usar"
            }
        }
    }

}