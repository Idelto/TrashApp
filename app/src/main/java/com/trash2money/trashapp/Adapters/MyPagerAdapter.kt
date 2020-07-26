package com.trash2money.trashapp.Adapters

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.trash2money.trashapp.Fragments.FragmentMyRequestTabHistory
import com.trash2money.trashapp.Fragments.FragmentMyRequestTabOnGoing

class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        Log.d("POSITION", position.toString())
        return when (position) {
            0 -> FragmentMyRequestTabOnGoing()
            else -> {
                return FragmentMyRequestTabHistory()
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Em andamento"
            else -> {
                return "Finalizados"
            }
        }
    }
}