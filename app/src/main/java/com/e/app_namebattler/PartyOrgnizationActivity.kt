package com.e.app_namebattler

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_character_list.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_party_orgnization.*

class PartyOrgnizationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_party_orgnization)

        // このパーティで開始
        this_party_start.setOnClickListener {
            val intent = Intent(this, BattleStartActivity::class.java)
            startActivity(intent)
        }

        // 戻るボタン
        party_organizetion_back_button.setOnClickListener{
            finish()
        }

    }
}