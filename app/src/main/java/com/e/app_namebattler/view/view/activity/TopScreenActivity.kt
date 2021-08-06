package com.e.app_namebattler.view.view.activity

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.e.app_namebattler.R
import com.e.app_namebattler.view.party.player.AllyFighterImageData
import com.e.app_namebattler.view.party.player.AllyNinjaImageData
import com.e.app_namebattler.view.party.player.AllyPriestImageData
import com.e.app_namebattler.view.party.player.AllyWizardImageData
import com.e.app_namebattler.view.view.music.MusicData
import kotlinx.android.synthetic.main.activity_top_screen.*
import java.util.*
import kotlin.concurrent.timer

class TopScreenActivity : AppCompatActivity() {

    lateinit var mp0: MediaPlayer
    private val allyFighterImageList = ArrayList<AllyFighterImageData>()
    private val allyWizardImageList = ArrayList<AllyWizardImageData>()
    private val allyPriestImageList = ArrayList<AllyPriestImageData>()
    private val allyNinjaImageList = ArrayList<AllyNinjaImageData>()
    private val enemyFighterImageList = ArrayList<AllyFighterImageData>()
    private val enemyWizardImageList = ArrayList<AllyWizardImageData>()
    private val enemyPriestImageList = ArrayList<AllyPriestImageData>()
    private val enemyNinjaImageList = ArrayList<AllyNinjaImageData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_screen)

        mp0 = MediaPlayer.create(this, MusicData.BGM02.getBgm())
        mp0.isLooping = true
        mp0.start()

        val imageView: ImageView = findViewById(R.id.top_screen_imageView_id)
        val handler = Handler()

        for (image in AllyFighterImageData.values()) {
            allyFighterImageList.add(image)
        }

        for (image in AllyWizardImageData.values()) {
            allyWizardImageList.add(image)
        }

        for (image in AllyPriestImageData.values()) {
            allyPriestImageList.add(image)
        }

        for (image in AllyNinjaImageData.values()) {
            allyNinjaImageList.add(image)
        }

        for (image in AllyFighterImageData.values()) {
            enemyFighterImageList.add(image)
        }

        for (image in AllyWizardImageData.values()) {
            enemyWizardImageList.add(image)
        }

        for (image in AllyPriestImageData.values()) {
            enemyPriestImageList.add(image)
        }

        for (image in AllyNinjaImageData.values()) {
            enemyNinjaImageList.add(image)
        }

        var image: Int
        var imageValue = 0

        timer(period = 5000) {

            handler.post {

                image = (0..7).random()

                when (image) {

                    0 -> imageValue =
                        allyFighterImageList[(1..allyFighterImageList.size).random() - 1].getCharacterImage()
                    1 -> imageValue =
                        allyWizardImageList[(1..allyWizardImageList.size).random() - 1].getCharacterImage()
                    2 -> imageValue =
                        allyPriestImageList[(1..allyPriestImageList.size).random() - 1].getCharacterImage()
                    3 -> imageValue =
                        allyNinjaImageList[(1..allyNinjaImageList.size).random() - 1].getCharacterImage()
                    4 -> imageValue =
                        enemyFighterImageList[(1..enemyFighterImageList.size).random() - 1].getCharacterImage()
                    5 -> imageValue =
                        enemyWizardImageList[(1..enemyWizardImageList.size).random() - 1].getCharacterImage()
                    6 -> imageValue =
                        enemyPriestImageList[(1..enemyPriestImageList.size).random() - 1].getCharacterImage()
                    7 -> imageValue =
                        enemyNinjaImageList[(1..enemyNinjaImageList.size).random() - 1].getCharacterImage()
                }

//                findViewById<View>(imageValue).startAnimation(AnimationUtils.loadAnimation(this,
//                    R.anim.anime01))

                imageView.y = (-300..0).random().toFloat()
                imageView.setImageResource(imageValue)
            }
        }

        // キャラ一覧のボタン
        top_screen_character_list_button_id.setOnClickListener {
            val intent = Intent(this, CharacterListActivity::class.java)
            mp0.reset()
            startActivity(intent)
        }
        // バトル開始ボタン
        top_screen_battle_start_button_id.setOnClickListener {
            val intent = Intent(this, PartyOrganizationActivity::class.java)
            mp0.reset()
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        mp0.release() // リソースの開放
        super.onDestroy()
    }

    override fun onPause() {
        mp0.pause()
        super.onPause()
    }

    override fun onResume() {
        mp0.start()
        super.onResume()
    }

    //戻るボタンの禁止
    override fun onBackPressed() {}
}

