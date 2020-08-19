package com.e.app_namebattler

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // キャラ一覧のボタン
        character_list_button.setOnClickListener {
            val intent = Intent(this, CharacterListActivity::class.java)
            startActivity(intent)
        }
        // バトル開始ボタン
        battle_start_button.setOnClickListener {
            val intent = Intent(this, PartyOrgnizationActivity::class.java)
            startActivity(intent)
        }

    }
}