package com.e.app_namebattler

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_top_screen.*


class TopScreenActivity : AppCompatActivity() {

    lateinit var mp0: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_screen)

        mp0= MediaPlayer.create(this,R.raw.neighofwar)
        mp0.isLooping=true
        mp0.start()

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
        mp0.release()
        super.onDestroy()
    }

    //戻るボタンの禁止
    override fun onBackPressed() {}

}