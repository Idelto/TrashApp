package com.trash2money.trashapp.Activities

import android.Manifest
import android.content.Intent
import android.os.Bundle
import com.trash2money.trashapp.Activities.Start_Login.LoginActivity
import com.trash2money.trashapp.Fragments.FragmentTermosCondicoesUso
import com.trash2money.trashapp.KClasses.InfoIntro
import com.trash2money.trashapp.R
import io.github.dreierf.materialintroscreen.MaterialIntroActivity
import io.github.dreierf.materialintroscreen.SlideFragmentBuilder

class IntroTrash : MaterialIntroActivity() {

    /*val auth = FirebaseAuth.getInstance() // define qual o usuário está autenticado
    val db = FirebaseFirestore.getInstance() // define caminho raiz do firebase
    val currentUser = auth.currentUser*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //verifyIntroActivity()

        val neededPermissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)

        addSlide(SlideFragmentBuilder().backgroundColor(R.color.colorText)
            .buttonsColor(R.color.quantum_black_secondary_text)
            .image(R.mipmap.ic_maps_intro_foreground).title("Permissões de Localização")
            .description(resources.getString(R.string.permissoes_localização))
            .neededPermissions(neededPermissions).build())

        addSlide(SlideFragmentBuilder().backgroundColor(R.color.colorText)
            .buttonsColor(R.color.quantum_black_secondary_text).image(R.drawable.ic_trash_reciclar)
            .title("Reciclagem").description(resources.getString(R.string.reciclagem_intro))
            .build())

        addSlide(SlideFragmentBuilder().backgroundColor(R.color.colorText)
            .buttonsColor(R.color.quantum_black_secondary_text).image(R.drawable.ic_trash_lixeira)
            .title("Lixeira").description(resources.getString(R.string.Lixeira_intro))
            .build())

        addSlide(SlideFragmentBuilder().backgroundColor(R.color.colorText)
            .buttonsColor(R.color.quantum_black_secondary_text).image(R.drawable.ic_trash_coins)
            .title("TrashCoins").description(resources.getString(R.string.Trashcoins_intro)).build())

        addSlide(SlideFragmentBuilder().backgroundColor(R.color.colorText)
            .buttonsColor(R.color.quantum_black_secondary_text).image(R.drawable.ic_coupon1)
            .title("Cupons").description(resources.getString(R.string.Coupons_intro)).build())

        addSlide(FragmentTermosCondicoesUso())
    }

    private fun verifyIntroActivity() {
        if (InfoIntro(this).isIntroActivityShow()) {

            startActivity(Intent(this, LoginActivity::class.java))

        }
        finish()
    }
}