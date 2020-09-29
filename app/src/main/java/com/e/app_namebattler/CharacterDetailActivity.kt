package com.e.app_namebattler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_character_detail.*

// キャラクター詳細画面のクラス
class CharacterDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_detail)

        // 戻るボタン
        character_detail_back_button.setOnClickListener {
            finish()
        }
    }
}