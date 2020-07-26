package com.trash2money.trashapp.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.trash2money.trashapp.KClasses.OpenDialog
import com.trash2money.trashapp.R

class FragmentConteinerReciclarPlastico : Fragment() {

    lateinit var btnAchocolatado: ImageButton
    lateinit var btnAlcool: ImageButton
    lateinit var btnAmacianteSabaoLiquido: ImageButton
    lateinit var btnAparelhoBarbear: ImageButton
    lateinit var btnBandejasIsopor: ImageButton
    lateinit var btnCanudos: ImageButton
    lateinit var btnCapsulasCafe: ImageButton
    lateinit var btnCoposPlasticos: ImageButton
    lateinit var btnDetergente: ImageButton
    lateinit var btnEmbalagemGalao: ImageButton
    lateinit var btnEmbalagemSabonete: ImageButton
    lateinit var btnEmbalagemBiscoito: ImageButton
    lateinit var btnEmbalagensCafe: ImageButton
    lateinit var btnEmbalagensCopo: ImageButton
    lateinit var btnEmbalagensGatilho: ImageButton
    lateinit var btnEmbalagensGeral: ImageButton
    lateinit var btnEmbalagensGeral1: ImageButton
    lateinit var btnEmbalagensSaches: ImageButton
    lateinit var btnEscovaDentes: ImageButton
    lateinit var btnExaguanteBucal: ImageButton
    lateinit var btnFermentado: ImageButton
    lateinit var btnFrascosHidratante: ImageButton
    lateinit var btnFrascosMostKet: ImageButton
    lateinit var btnGarrafasPlasticasCor: ImageButton
    lateinit var btnGarrafasPlasticasTransp: ImageButton
    lateinit var btnMarmitasIsopor: ImageButton
    lateinit var btnEmbalagemBicoPato: ImageButton
    lateinit var btnOutrosFrascos: ImageButton
    lateinit var btnPacotesGeral: ImageButton
    lateinit var btnPet: ImageButton
    lateinit var btnPlasticoIsopor: ImageButton
    lateinit var btnPotesCreme: ImageButton
    lateinit var btnPotesMaio: ImageButton
    lateinit var btnPotesManteiga: ImageButton
    lateinit var btnPotesSorvete: ImageButton
    lateinit var btnPratosDescartaveis: ImageButton
    lateinit var btnPratosIsopor: ImageButton
    lateinit var btnRefilAlimento: ImageButton
    lateinit var btnRefisGatilho: ImageButton
    lateinit var btnRefisLiquido: ImageButton
    lateinit var btnSacosAlimentosGeral: ImageButton
    lateinit var btnShampooCond: ImageButton
    lateinit var btnTalheresPlastico: ImageButton
    lateinit var btnSacolaSupermercado: ImageButton
    lateinit var btnSacoLixo: ImageButton
    lateinit var btnCaixaFruta: ImageButton
    lateinit var btnEmbOvo: ImageButton
    lateinit var btnPackLata: ImageButton
    lateinit var btnTubosPastaDente: ImageButton
    lateinit var btnCopoIorgute : ImageButton
    lateinit var btnCopoRequeijao : ImageButton
    lateinit var btnCopoCha : ImageButton
    lateinit var btnFrascoMolho : ImageButton

    @Nullable
    @Override
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v: View
        v = inflater.inflate(R.layout.fragment_conteiner_reciclar_plastico, container, false)

        btnAchocolatado = v.findViewById(R.id.btn_pl_achocolatado)
        btnAlcool = v.findViewById(R.id.btn_pl_alcool)
        btnAmacianteSabaoLiquido = v.findViewById(R.id.btn_pl_amaciante_sabao_liquido)
        btnAparelhoBarbear = v.findViewById(R.id.btn_pl_aparelho_barbear)
        btnBandejasIsopor = v.findViewById(R.id.btn_pl_bandejas_isopor)
        btnCanudos = v.findViewById(R.id.btn_pl_canudos)
        btnCapsulasCafe = v.findViewById(R.id.btn_pl_capsulas_cafe)
        btnCoposPlasticos = v.findViewById(R.id.btn_pl_copos_plasticos)
        btnDetergente = v.findViewById(R.id.btn_pl_detergente)
        btnEmbalagemGalao = v.findViewById(R.id.btn_pl_embalagem_galao)
        btnEmbalagemSabonete = v.findViewById(R.id.btn_pl_embalagem_sabonete)
        btnEmbalagemBiscoito = v.findViewById(R.id.btn_pl_embalagem_biscoito)
        btnEmbalagensCafe = v.findViewById(R.id.btn_pl_embalagem_cafe)
        btnEmbalagensCopo = v.findViewById(R.id.btn_pl_embalagem_copo)
        btnEmbalagensGatilho = v.findViewById(R.id.btn_pl_embalagem_gatilho)
        btnEmbalagensGeral = v.findViewById(R.id.btn_pl_embalagem_geral)
        btnEmbalagensGeral1 = v.findViewById(R.id.btn_pl_embalagem_geral_1)
        btnEmbalagensSaches = v.findViewById(R.id.btn_pl_embalagem_saches)
        btnEscovaDentes = v.findViewById(R.id.btn_pl_escova_dentes)
        btnExaguanteBucal = v.findViewById(R.id.btn_pl_exaguante_bucal)
        btnFermentado = v.findViewById(R.id.btn_pl_fermentado)
        btnFrascosHidratante = v.findViewById(R.id.btn_pl_frascos_hidratante)
        btnFrascosMostKet = v.findViewById(R.id.btn_pl_frascos_most_ket)
        btnGarrafasPlasticasCor = v.findViewById(R.id.btn_pl_garrafas_plastico_cor)
        btnGarrafasPlasticasTransp = v.findViewById(R.id.btn_pl_garrafas_plastico_transparente)
        btnMarmitasIsopor = v.findViewById(R.id.btn_pl_marmitas_isopor)
        btnEmbalagemBicoPato = v.findViewById(R.id.btn_pl_mbalagem_bico_de_pato)
        btnOutrosFrascos = v.findViewById(R.id.btn_pl_outros_frascos)
        btnPacotesGeral = v.findViewById(R.id.btn_pl_pacotes_geral)
        btnPet = v.findViewById(R.id.btn_pl_pet)
        btnPlasticoIsopor = v.findViewById(R.id.btn_pl_plasticos_isopor)
        btnPotesCreme = v.findViewById(R.id.btn_pl_potes_cremes)
        btnPotesMaio = v.findViewById(R.id.btn_pl_potes_maio)
        btnPotesManteiga = v.findViewById(R.id.btn_pl_potes_manteiga)
        btnPotesSorvete = v.findViewById(R.id.btn_pl_potes_sorvete)
        btnPratosDescartaveis = v.findViewById(R.id.btn_pl_pratos_descartaveis)
        btnPratosIsopor = v.findViewById(R.id.btn_pl_pratos_isopor)
        btnRefilAlimento = v.findViewById(R.id.btn_pl_refil_alimentos)
        btnRefisGatilho = v.findViewById(R.id.btn_pl_refis_gatilho)
        btnRefisLiquido = v.findViewById(R.id.btn_pl_refis_liquido)
        btnSacosAlimentosGeral = v.findViewById(R.id.btn_pl_sacos_alimentos_geral)
        btnShampooCond = v.findViewById(R.id.btn_pl_shampoo_cond)
        btnTalheresPlastico = v.findViewById(R.id.btn_pl_talheres_plasticos)
        btnTubosPastaDente = v.findViewById(R.id.btn_pl_tubos_pasta_dente)
        btnSacolaSupermercado = v.findViewById(R.id.btn_pl_sacola_supermercado)
        btnSacoLixo = v.findViewById(R.id.pl_saco_lixo)
        btnCaixaFruta = v.findViewById(R.id.pl_cx_frutas)
        btnEmbOvo = v.findViewById(R.id.pl_emb_ovo)
        btnPackLata = v.findViewById(R.id.pl_cx_pack_lata)
        btnCopoIorgute = v.findViewById(R.id.pl_pote_iorgute)
        btnCopoRequeijao = v.findViewById(R.id.pl_pote_requeijao)
        btnCopoCha = v.findViewById(R.id.pl_copo_cha)
        btnFrascoMolho = v.findViewById(R.id.pl_frasco_molho)


        btnSacolaSupermercado.setOnClickListener() {

            OpenDialog(context!!, "pl_sacola_supermercado").openDialog(v)
        }

        btnSacoLixo.setOnClickListener() {

            OpenDialog(context!!, "pl_saco_lixo").openDialog(v)
        }

        btnCaixaFruta.setOnClickListener() {

            OpenDialog(context!!, "pl_cx_frutas_verduras").openDialog(v)
        }

        btnEmbOvo.setOnClickListener() {

            OpenDialog(context!!, "pl_embalagem_ovo").openDialog(v)
        }

        btnPackLata.setOnClickListener() {

            OpenDialog(context!!, "pl_pack_latinhas").openDialog(v)
        }

        btnAchocolatado.setOnClickListener() {

            OpenDialog(context!!, "pl_achocolatado").openDialog(v)
        }

        btnAlcool.setOnClickListener() {

            OpenDialog(context!!, "pl_alcool").openDialog(v)
        }

        btnAmacianteSabaoLiquido.setOnClickListener() {

            OpenDialog(context!!, "pl_amaciante_sabao_liquido").openDialog(v)
        }

        btnAparelhoBarbear.setOnClickListener() {

            OpenDialog(context!!, "pl_aparelho_barbear").openDialog(v)
        }

        btnBandejasIsopor.setOnClickListener() {

            Toast.makeText(context!!,"O isopor é um material reciclável, mas com pouco interresse comercial. Estamos trabalhando para resolver esse problema.", Toast.LENGTH_LONG).show()
            //OpenDialog(context!!, "pl_bandejas_isopor").openDialog(v)
        }

        btnCanudos.setOnClickListener() {

            OpenDialog(context!!, "pl_canudos").openDialog(v)
        }

        btnCapsulasCafe.setOnClickListener() {

            OpenDialog(context!!, "pl_capsulas_cafe").openDialog(v)
        }

        btnCoposPlasticos.setOnClickListener() {

            OpenDialog(context!!, "pl_copos_plastico").openDialog(v)
        }

        btnDetergente.setOnClickListener() {

            OpenDialog(context!!, "pl_detergente").openDialog(v)
        }

        btnEmbalagemGalao.setOnClickListener() {

            OpenDialog(context!!, "pl_embalagem_galao").openDialog(v)
        }

        btnEmbalagemSabonete.setOnClickListener() {

            OpenDialog(context!!, "pl_embalagem_sabonete").openDialog(v)
        }

        btnEmbalagemBiscoito.setOnClickListener() {

            OpenDialog(context!!, "pl_embalagem_biscoito").openDialog(v)
        }

        btnEmbalagensCafe.setOnClickListener() {

            OpenDialog(context!!, "pl_embalagem_cafe").openDialog(v)
        }

        btnEmbalagensCopo.setOnClickListener() {

            OpenDialog(context!!, "pl_embalagens_copo").openDialog(v)
        }

        btnEmbalagensGatilho.setOnClickListener() {

            OpenDialog(context!!, "pl_embalagens_gatilho").openDialog(v)
        }

        btnEmbalagensGeral.setOnClickListener() {

            OpenDialog(context!!, "pl_sacos_alimentos_geral").openDialog(v)
        }

        btnEmbalagensGeral1.setOnClickListener() {

            OpenDialog(context!!, "pl_embalagens_geral").openDialog(v)
        }

        btnEmbalagensSaches.setOnClickListener() {

            OpenDialog(context!!, "pl_embalagens_saches").openDialog(v)
        }

        btnEscovaDentes.setOnClickListener() {

            OpenDialog(context!!, "pl_escova_dentes").openDialog(v)
        }

        btnExaguanteBucal.setOnClickListener() {

            OpenDialog(context!!, "pl_enxaguante_bucal").openDialog(v)
        }

        btnFermentado.setOnClickListener() {

            OpenDialog(context!!, "pl_fermentado").openDialog(v)
        }

        btnFrascosHidratante.setOnClickListener() {

            OpenDialog(context!!, "pl_frascos_hidratantes").openDialog(v)
        }

        btnFrascosMostKet.setOnClickListener() {

            OpenDialog(context!!, "pl_frascos_most_ket").openDialog(v)
        }

        btnGarrafasPlasticasCor.setOnClickListener() {

            OpenDialog(context!!, "pl_garrafas_plasticas_cor").openDialog(v)
        }

        btnGarrafasPlasticasTransp.setOnClickListener() {

            OpenDialog(context!!, "pl_garrafas_plasticas_transp").openDialog(v)
        }

        btnMarmitasIsopor.setOnClickListener() {

            Toast.makeText(context!!,"O isopor é um material reciclável, mas com pouco interresse comercial. Estamos trabalhando para resolver esse problema.", Toast.LENGTH_LONG).show()
            //OpenDialog(context!!, "pl_marmitas_isopor").openDialog(v)
        }

        btnEmbalagemBicoPato.setOnClickListener() {

            OpenDialog(context!!, "pl_mbalagem_bico_de_pato").openDialog(v)
        }

        btnOutrosFrascos.setOnClickListener() {

            OpenDialog(context!!, "pl_outros_frascos").openDialog(v)
        }

        btnPacotesGeral.setOnClickListener() {

            OpenDialog(context!!, "pl_pacotes_geral").openDialog(v)
        }

        btnPet.setOnClickListener() {

            OpenDialog(context!!, "pl_pet").openDialog(v)
        }

        btnPlasticoIsopor.setOnClickListener() {

            Toast.makeText(context!!,"O isopor é um material reciclável, mas com pouco interresse comercial. Estamos trabalhando para resolver esse problema.", Toast.LENGTH_LONG).show()
            //OpenDialog(context!!, "pl_plasticos_isopor").openDialog(v)
        }

        btnPotesCreme.setOnClickListener() {

            OpenDialog(context!!, "pl_potes_creme").openDialog(v)
        }

        btnPotesMaio.setOnClickListener() {

            OpenDialog(context!!, "pl_potes_maio").openDialog(v)
        }

        btnPotesManteiga.setOnClickListener() {

            OpenDialog(context!!, "pl_potes_manteiga").openDialog(v)
        }

        btnPotesSorvete.setOnClickListener() {

            OpenDialog(context!!, "pl_potes_sorvete").openDialog(v)
        }

        btnPratosDescartaveis.setOnClickListener() {

            OpenDialog(context!!, "pl_pratos_descartaveis").openDialog(v)
        }

        btnPratosIsopor.setOnClickListener() {

            Toast.makeText(context!!,"O isopor é um material reciclável, mas com pouco interresse comercial. Estamos trabalhando para resolver esse problema.", Toast.LENGTH_LONG).show()
            //OpenDialog(context!!, "pl_pratos_isoporp").openDialog(v)
        }

        btnRefilAlimento.setOnClickListener() {

            OpenDialog(context!!, "pl_refil_alimentos").openDialog(v)
        }

        btnRefisGatilho.setOnClickListener() {

            OpenDialog(context!!, "pl_refis_gatilho").openDialog(v)
        }

        btnRefisLiquido.setOnClickListener() {

            OpenDialog(context!!, "pl_refil_liquidos").openDialog(v)
        }

        btnSacosAlimentosGeral.setOnClickListener() {

            OpenDialog(context!!, "pl_sacos_alimentos_geral").openDialog(v)
        }

        btnShampooCond.setOnClickListener() {

            OpenDialog(context!!, "pl_shampoo_cond").openDialog(v)
        }

        btnTalheresPlastico.setOnClickListener() {

            OpenDialog(context!!, "pl_talheres_plasticos").openDialog(v)
        }

        btnTubosPastaDente.setOnClickListener() {

            OpenDialog(context!!, "pl_tubos_pasta_dente").openDialog(v)
        }

        btnCopoIorgute.setOnClickListener() {

            OpenDialog(context!!, "pl_pote_iorgute").openDialog(v)
        }

        btnCopoRequeijao.setOnClickListener() {

            OpenDialog(context!!, "pl_pote_requeijao").openDialog(v)
        }

        btnCopoCha.setOnClickListener() {

            OpenDialog(context!!, "pl_copo_cha").openDialog(v)
        }

        btnFrascoMolho.setOnClickListener() {

            OpenDialog(context!!, "pl_frasco_molho").openDialog(v)
        }



        return v
    }

}
