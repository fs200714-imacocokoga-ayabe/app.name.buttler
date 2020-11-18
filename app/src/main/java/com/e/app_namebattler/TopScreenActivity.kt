package com.e.app_namebattler

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_top_screen.*

class TopScreenActivity : AppCompatActivity() {

    lateinit var helper: MyOpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_screen)

        // キャラ一覧のボタン
        top_screen_character_list_button_id.setOnClickListener {
            val intent = Intent(this, CharacterListActivity::class.java)
            startActivity(intent)
        }
        // バトル開始ボタン
        top_screen_battle_start_button_id.setOnClickListener {
            val intent = Intent(this, PartyOrgnizationActivity::class.java)
            startActivity(intent)
        }

    }
}