package com.trash2money.trashapp.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.trash2money.trashapp.KClasses.OpenDialog
import com.trash2money.trashapp.KClasses.Populate
import com.trash2money.trashapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FragmentConteinerReciclarProcurar : Fragment() {

    lateinit var atSearchProduto: AutoCompleteTextView
    lateinit var imgProdutos: ImageButton
    lateinit var itemDialog: String
    lateinit var auth: FirebaseAuth

    private val REQUEST_CODE_SPEECH_INPUT = 100

    var listaBusca = arrayListOf<String>("")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v: View
        v = inflater.inflate(R.layout.fragment_conteiner_reciclar_procurar, container, false)
        val adpter =
            activity?.let { ArrayAdapter(it, R.layout.resource_dropdown_color, listaBusca) }

        getProducts()

        atSearchProduto = v.findViewById(R.id.at_CompleteTextViewProduto)
        imgProdutos = v.findViewById(R.id.btn_produtos_geral)

        atSearchProduto.setAdapter(adpter)
        atSearchProduto.threshold = 0

        atSearchProduto.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedItem = parent.getItemAtPosition(position).toString()

                val docsRef = FirebaseFirestore.getInstance().collection("trash_products")
                docsRef.get().addOnSuccessListener { docs ->
                    for (doc in docs) {
                        val obj = doc.toObject<Populate>(Populate::class.java)
                        if (selectedItem == obj.shortDesc) {
                            Log.d("tag2", "$selectedItem")
                            Log.d("tag2", "${obj.shortDesc}")
                            imgProdutos.visibility = View.VISIBLE
                            val strDrawable = "ic_a_${doc.id}"
                            Log.d("tag1", "$strDrawable")
                            itemDialog = obj.name
                            imgProdutos.setImageResource(context!!.resources.getIdentifier(
                                strDrawable,
                                "drawable",
                                context!!.packageName))
                            Toast.makeText(context,
                                "Selected : $selectedItem banco : ${obj.shortDesc}",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }

        // Set a dismiss listener for auto complete text view

        // Set a focus change listener for auto complete text view
        atSearchProduto.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b) {

            }
        }

        imgProdutos.setOnClickListener {
            OpenDialog(context!!, itemDialog).openDialog(v)
            imgProdutos.visibility = View.GONE

        }

        return v
    }

    private fun getProducts() {

        val db = FirebaseFirestore.getInstance().collection("trash_products")

        db.get().addOnSuccessListener { docs ->

            for (doc in docs) {

                db.document(doc.id).get().addOnSuccessListener { doc ->

                    val str = doc["shortDesc"].toString()
                    listaBusca.add(str)
                    Log.d("tag150", "$str")

                }

            }

        }

    }

}