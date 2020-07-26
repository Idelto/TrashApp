package com.trash2money.trashapp.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.trash2money.trashapp.KClasses.OpenDialog
import com.trash2money.trashapp.R

class FragmentConteinerReciclarPapel : Fragment() {

    @Nullable
    @Override
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        lateinit var btnCaderno: ImageButton
        lateinit var btnConta: ImageButton
        lateinit var btnCopoPapel: ImageButton
        lateinit var btncxAlimento: ImageButton
        lateinit var btnCxCeral: ImageButton
        lateinit var btnCxCha: ImageButton
        lateinit var btnCxCotonete: ImageButton
        lateinit var btnCxCafe: ImageButton
        lateinit var btnCxOvo: ImageButton
        lateinit var btnCxSabaoPo: ImageButton
        lateinit var btnCxSabonete: ImageButton
        lateinit var btnCXGeral: ImageButton
        lateinit var btnEmbLongVidaLiq: ImageButton
        lateinit var btnEmbLongVidaSol: ImageButton
        lateinit var btnEmbSacoPapel: ImageButton
        lateinit var btnEmbBiscoito: ImageButton
        lateinit var btnEnvelope: ImageButton
        lateinit var btnJornais: ImageButton
        lateinit var btnLivros: ImageButton
        lateinit var btnPapeisAvulso: ImageButton
        lateinit var btnRevistas: ImageButton
        lateinit var btnRoloGuardanapo: ImageButton
        lateinit var btnRoloPapelHigienico: ImageButton
        lateinit var btnTampaPizza: ImageButton
        lateinit var btnCxCapsula: ImageButton
        lateinit var btnPackLatinhas: ImageButton
        lateinit var btnCxGrande: ImageButton
        lateinit var btnCxMedia: ImageButton
        lateinit var btnCxPequena: ImageButton
        lateinit var btnCxRemedioHigiene: ImageButton

        val v: View
        v = inflater.inflate(R.layout.fragment_conteiner_reciclar_papel, container, false)

        btnCaderno = v.findViewById<ImageButton>(R.id.btn_caderno)
        btnConta = v.findViewById<ImageButton>(R.id.btn_contas)
        btnCxCapsula = v.findViewById<ImageButton>(R.id.btn_cx_capsula)
        btnCopoPapel = v.findViewById<ImageButton>(R.id.btn_copo_papel)
        btncxAlimento = v.findViewById<ImageButton>(R.id.btn_cx_alimentos)
        btnCxCeral = v.findViewById<ImageButton>(R.id.btn_emb_cereal)
        btnCxCha = v.findViewById<ImageButton>(R.id.btn_cx_cha)
        btnCxCotonete = v.findViewById<ImageButton>(R.id.btn_cx_cotonete)
        btnCxCafe = v.findViewById<ImageButton>(R.id.btn_emb_cafe)
        btnCxOvo = v.findViewById<ImageButton>(R.id.btn_cx_ovo)
        btnCxSabaoPo = v.findViewById<ImageButton>(R.id.btn_cx_sabao_po)
        btnCxSabonete = v.findViewById<ImageButton>(R.id.btn_cx_sabonete)
        btnCXGeral = v.findViewById(R.id.btn_cx_geral)
        btnEmbLongVidaLiq = v.findViewById<ImageButton>(R.id.btn_long_vida_liq)
        btnEmbLongVidaSol = v.findViewById<ImageButton>(R.id.btn_long_vida_sol)
        btnEmbSacoPapel = v.findViewById<ImageButton>(R.id.btn_emb_saco_papel)
        btnEmbBiscoito = v.findViewById<ImageButton>(R.id.btn_emb_biscoito)
        btnEnvelope = v.findViewById<ImageButton>(R.id.btn_envelope)
        btnJornais = v.findViewById<ImageButton>(R.id.btn_jornais)
        btnLivros = v.findViewById<ImageButton>(R.id.btn_livros)
        btnPackLatinhas = v.findViewById<ImageButton>(R.id.btn_pack_latinha)
        btnPapeisAvulso = v.findViewById<ImageButton>(R.id.btn_papel_avulso)
        btnRevistas = v.findViewById<ImageButton>(R.id.btn_revistas)
        btnRoloGuardanapo = v.findViewById<ImageButton>(R.id.btn_rolo_guardanapo)
        btnRoloPapelHigienico = v.findViewById<ImageButton>(R.id.btn_rolo_papel_higienico)
        btnTampaPizza = v.findViewById<ImageButton>(R.id.btn_tampa_pizza)
        btnCxGrande = v.findViewById<ImageButton>(R.id.btn_cxs_grande)
        btnCxMedia = v.findViewById<ImageButton>(R.id.btn_cxs_media)
        btnCxPequena = v.findViewById<ImageButton>(R.id.btn_cxs_pequena)
        btnCxRemedioHigiene = v.findViewById<ImageButton>(R.id.btn_cxs_remedioehigiene)


        btnPackLatinhas.setOnClickListener() {

            OpenDialog(context!!, "pap_pack_latinhas").openDialog(v)
        }

        btnCaderno.setOnClickListener() {

            OpenDialog(context!!, "pap_caderno").openDialog(v)
        }

        btnCopoPapel.setOnClickListener() {

            OpenDialog(context!!, "pap_copo_papel").openDialog(v)

        }

        btnConta.setOnClickListener() {

            OpenDialog(context!!, "pap_contas").openDialog(v)

        }

        btnCxCapsula.setOnClickListener() {

            OpenDialog(context!!, "pap_cx_capsula_cafe").openDialog(v)

        }

        btncxAlimento.setOnClickListener() {

            OpenDialog(context!!, "pap_cx_alimentos").openDialog(v)
        }

        btnCxCafe.setOnClickListener() {

            OpenDialog(context!!, "pap_cx_cafe").openDialog(v)
        }

        btnCxCeral.setOnClickListener() {

            OpenDialog(context!!, "pap_cx_cereal").openDialog(v)
        }

        btnCxCha.setOnClickListener() {

            OpenDialog(context!!, "pap_cx_cha").openDialog(v)
        }

        btnCxCotonete.setOnClickListener() {

            OpenDialog(context!!, "pap_cx_cotonetes").openDialog(v)
        }

        btnCXGeral.setOnClickListener() {

            OpenDialog(context!!, "pap_cxs_geral").openDialog(v)
        }

        btnEmbSacoPapel.setOnClickListener() {

            OpenDialog(context!!, "pap_emb_papel").openDialog(v)
        }

        btnCxCafe.setOnClickListener() {

            OpenDialog(context!!, "pap_cx_cafe").openDialog(v)
        }

        btnEmbLongVidaSol.setOnClickListener() {

            OpenDialog(context!!, "pap_longa_vida_sol").openDialog(v)
        }

        btnCxOvo.setOnClickListener() {

            OpenDialog(context!!, "pap_cx_ovo").openDialog(v)
        }

        btnCxSabaoPo.setOnClickListener() {

            OpenDialog(context!!, "pap_cx_sabao_em_po").openDialog(v)
        }

        btnCxSabonete.setOnClickListener() {

            OpenDialog(context!!, "pap_cx_sabonete").openDialog(v)
        }

        btnEmbLongVidaLiq.setOnClickListener() {

            OpenDialog(context!!, "pap_emb_longa_vida").openDialog(v)
        }

        btnEmbSacoPapel.setOnClickListener() {

            OpenDialog(context!!, "pap_emb_papel").openDialog(v)
        }

        btnEmbBiscoito.setOnClickListener() {

            OpenDialog(context!!, "pap_embalagens_biscoito").openDialog(v)
        }

        btnEnvelope.setOnClickListener() {

            OpenDialog(context!!, "pap_envelope").openDialog(v)
        }

        btnEmbLongVidaSol.setOnClickListener() {

            OpenDialog(context!!, "pap_longa_vida_sol").openDialog(v)
        }

        btnJornais.setOnClickListener() {

            OpenDialog(context!!, "pap_jornais").openDialog(v)
        }

        btnLivros.setOnClickListener() {

            OpenDialog(context!!, "pap_livros").openDialog(v)
        }

        btnPapeisAvulso.setOnClickListener() {

            OpenDialog(context!!, "pap_papeis_avulsos").openDialog(v)
        }

        btnRevistas.setOnClickListener() {

            OpenDialog(context!!, "pap_revistas").openDialog(v)
        }

        btnRoloGuardanapo.setOnClickListener() {

            OpenDialog(context!!, "pap_rolo_guardanapo").openDialog(v)
        }

        btnRoloPapelHigienico.setOnClickListener() {

            OpenDialog(context!!, "pap_rolo_papel_higienico").openDialog(v)
        }

        btnTampaPizza.setOnClickListener() {

            OpenDialog(context!!, "pap_tampa_pizza").openDialog(v)
        }

        btnCxGrande.setOnClickListener() {

            OpenDialog(context!!, "pap_cxs_grande").openDialog(v)
        }

        btnCxMedia.setOnClickListener() {

            OpenDialog(context!!, "pap_cxs_media").openDialog(v)
        }

        btnCxPequena.setOnClickListener() {

            OpenDialog(context!!, "pap_cxs_pequena").openDialog(v)
        }

        btnCxRemedioHigiene.setOnClickListener() {

            OpenDialog(context!!, "pap_cxs_remedioehigiene").openDialog(v)
        }


        return v
    }

}
