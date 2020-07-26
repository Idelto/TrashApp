package com.trash2money.trashapp.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trash2money.trashapp.Activities.HomeActivity
import com.trash2money.trashapp.Activities.Maps.MapsActivity
import com.trash2money.trashapp.Adapters.TCoinsKartAdapter
import com.trash2money.trashapp.KClasses.*
import com.trash2money.trashapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.text.DecimalFormat


class FragmentMyTrashCoinsCan : Fragment() {

    lateinit var btnRecycle: Button
    lateinit var txtPesoTotal: TextView
    lateinit var txtQtyTotal: TextView
    lateinit var txtTCoinsTotal: TextView
    lateinit var txtEmptyTrashCan: TextView
    lateinit var txtBrief: TextView
    lateinit var txtQtyTotalDesc: TextView
    lateinit var txtTCoinsDesc: TextView
    lateinit var txtPesoTotalDesc: TextView
    lateinit var txtMyTrashPointCanTitle: TextView
    lateinit var imgRecycleSign: ImageView
    lateinit var simpleCallback: ItemTouchHelper.SimpleCallback

    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_my_trash_point_can, container, false) as View
        val recyclerView = v.findViewById(R.id.shoRecyclerView) as RecyclerView

        var dialog = ProgressBarDialog(context!!)
        dialog.openDialog()

        txtPesoTotal = v.findViewById(R.id.txtPesoTotal)
        txtQtyTotal = v.findViewById(R.id.txtQtyTotal)
        txtTCoinsTotal = v.findViewById(R.id.txtTcoinsTotal)
        txtPesoTotalDesc = v.findViewById(R.id.txtPesoTotalDesc)
        txtQtyTotalDesc = v.findViewById(R.id.txtQtyTotalDesc)
        txtTCoinsDesc = v.findViewById(R.id.txtTCoinsTotalDesc)
        txtBrief = v.findViewById(R.id.txtBrief)
        txtMyTrashPointCanTitle = v.findViewById(R.id.txtMyTrashPointCanTitle)
        imgRecycleSign = v.findViewById(R.id.imgRecycleSign)
        btnRecycle = v.findViewById(R.id.btnRecycle)

        txtEmptyTrashCan = v.findViewById(R.id.txtemptyTrashCan)

        //Recycler view : 1 - Definição do gerenciador de layout
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)

        val fm: FragmentManager = activity!!.supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.bottom_bar_container, Fragment_bottom_bar()).commit()

        // criação da lista do conteúdo do seu recycler view
        val list = ArrayList<TCoinsKartItem>()

        var totQty = 0F
        var totTCoins = 0F
        var totPeso = 0F
        var querySize = 1

        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val docRef = db.collection("points_trash_can").document(currentUser?.uid.toString()).collection("trash_item")

        docRef.get().addOnSuccessListener { querySnapshot ->

            querySize = querySnapshot.size()
            Log.d("TAMANHO DA QUERY", querySize.toString())
        }

        // Buscar informações do trashcan
        GetCurrentUserData().readDataTrashCan(object : GetCurrentUserData.MyCallbackTrashCan {

            override fun onCallBack(trash: Trash) {

                txtEmptyTrashCan.visibility = View.INVISIBLE
                imgRecycleSign.visibility = View.INVISIBLE
                txtPesoTotal.visibility = 1
                txtQtyTotal.visibility = 1
                txtTCoinsTotal.visibility = 1
                txtPesoTotalDesc.visibility = 1
                txtQtyTotalDesc.visibility = 1
                txtTCoinsDesc.visibility = 1
                txtBrief.visibility = 1
                txtMyTrashPointCanTitle.visibility = 1

                Log.d("TAG ON CALL BACK", trash.toString())

                val df = DecimalFormat("0.00")

                Log.d("TAG", df.format(trash.quantity).toString())

                //Busca o ícone correto
                val docsRef = FirebaseFirestore.getInstance().collection("trash_products")
                docsRef.get().addOnSuccessListener { docs ->

                    for (doc in docs) {
                        val obj = doc.toObject<Populate>(Populate::class.java)
                        Log.d("Verificando", doc.id)
                        Log.d("Verificando2", obj.longDesc)
                        Log.d("Verificando3", trash.description)
                        if (obj.shortDesc.capitalize() == trash.description.capitalize()) {
                            Log.d("ShortDesc", "Entrou no Loop")
                            val strDrawable = "ic_b_${doc.id}"
                            val drawable = (context!!.resources.getIdentifier(strDrawable, "drawable", context!!.packageName))
                            Log.d("drawable_var", drawable.toString())
                            Log.d("drawable_caderno", context!!.resources.getDrawable(R.drawable.ic_b_pap_caderno).toString())
                            list.add(TCoinsKartItem(strDrawable, trash.description, df.format(trash.weight).toString(), df.format(trash.quantity).toString(), df.format(trash.trashcoins).toString(), trash.trashid))
                        }
                    }

                    Log.d("TAG TAMANHO LISTA", list.size.toString())
                    Log.d("TAG TAMANHO QUERY", list.size.toString())
                    //
                    if (querySize == list.size) {

                        Log.d("QUERY SIZE ", "QUERY SIZE : ${querySize.toString()} LIST SIZE: ${list.size.toString()}")

                        // alterar a de vírgula (,) para ponto (.) - antes de transformar para float e fazendo a conta para prenchiemento dos labels de total.
                        for (x in list) {
                            Log.d("ENTRANDO NO LOOP ", "ENTREI NO LOOP")
                            val qty = x.qty.replace(",", ".").toFloat()
                            val tC = x.tCoins.replace(",", ".").toFloat()
                            val tara = x.tara.replace(",", ".").toFloat()

                            Log.d("CONTAS QTY + XQTY: ", totQty.toString() + "+" + x.qty)
                            totQty += qty
                            Log.d(" CONTAS TOT_QTY: ", totQty.toString())

                            Log.d(" CONTAS TOT + XTOT: ", totTCoins.toString() + "+" + x.tCoins)
                            totTCoins += tC
                            Log.d(" CONTAS : TOT QTY ", totTCoins.toString())

                            Log.d(" CONTAS TOTPESO+XPESO: ", totPeso.toString() + "+" + x.tara)
                            totPeso += tara
                            Log.d(" CONTAS TOTPESO: ", totPeso.toString())

                        }

                        txtTCoinsTotal.setText(df.format(totTCoins).toString() + " T")
                        txtPesoTotal.setText(df.format(totPeso).toString() + " g")
                        txtQtyTotal.setText(df.format(totQty).toString() + " Un")


                    }else{

                    }

                }

            }

        })

        val adapter = TCoinsKartAdapter(list)
        recyclerView.adapter = adapter

        simpleCallback = (object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val position = viewHolder.adapterPosition

                val id = list[position].docId

                val db2 = FirebaseFirestore.getInstance()
                val currUser = FirebaseAuth.getInstance().currentUser

                val docRef = db2.collection("points_trash_can").document(currUser?.uid.toString()).collection("trash_item")
                docRef.get().addOnSuccessListener { docs ->

                    for (doc in docs) {
                        Log.d("DEL_DOC", doc.id)
                        val obj = doc.toObject<Trash>(Trash::class.java)
                        Log.d("DEL_OBJID", obj.trashid)
                        Log.d("DEL_ID", id)

                        if (obj.trashid == id) {
                            Log.d("DEL", "DELETANDO....")
                            docRef.document(doc.id).delete()
                            Toast.makeText(context, "Documento Deletado com sucesso", Toast.LENGTH_LONG).show()
                            recyclerView.adapter!!.notifyItemRemoved(position)
                            val intent = Intent(context, HomeActivity::class.java)
                            intent.putExtra("fragment", "FragmentMyTrashCoinsCan")
                            startActivity(intent)
                        }
                    }
                }
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

                RecyclerViewSwipeDecorator.Builder(context, c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive).addSwipeLeftBackgroundColor(ContextCompat.getColor(context!!, R.color.colormarsalla)).addSwipeLeftActionIcon(R.drawable.ic_delete_white_50dp).create().decorate()

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

        })

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        // Se a lista estiver vazia significa que o carrinho está vazio
        // Altero os parâmetros dos objetos para visível.

        if (list.size == 0) {


            txtEmptyTrashCan.visibility = 1
            imgRecycleSign.visibility = 1
            txtPesoTotal.visibility = View.INVISIBLE
            txtQtyTotal.visibility = View.INVISIBLE
            txtTCoinsTotal.visibility = View.INVISIBLE
            txtPesoTotalDesc.visibility = View.INVISIBLE
            txtQtyTotalDesc.visibility = View.INVISIBLE
            txtTCoinsDesc.visibility = View.INVISIBLE
            txtBrief.visibility = View.INVISIBLE
            txtMyTrashPointCanTitle.visibility = View.INVISIBLE

        }

        btnRecycle.setOnClickListener() {

            if (list.size > 0) {
                val intent = Intent(activity, MapsActivity::class.java)
                startActivity(intent)
                Log.d("TAG LIST", list.size.toString())
            } else {
                val intent = Intent(activity, HomeActivity::class.java)
                intent.putExtra("fragment", "FragmentRecycleMain")
                startActivity(intent)
                Log.d("TAG LIST", list.size.toString())

            }

        }

        dialog.closeDialog()

        return v

    }


}


