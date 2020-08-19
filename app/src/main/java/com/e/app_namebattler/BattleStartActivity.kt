package com.e.app_namebattler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_battle_start.*

class BattleStartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle_start)

        // 戻るボタン
        battle_start_back_button.setOnClickListener{
            finish()
        }
    }

}