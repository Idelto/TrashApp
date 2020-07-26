package com.trash2money.trashapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment

class FragmentEmpty : Fragment() {

    @SuppressLint("ResourceType")
    @Nullable
    @Override
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_cupons, container, false) as View

        Toast.makeText(context, "Isso é um Fragment!!!!", Toast.LENGTH_LONG).show()

        return v
    }

}

