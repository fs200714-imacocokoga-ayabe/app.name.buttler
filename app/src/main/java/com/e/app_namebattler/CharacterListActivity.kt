package com.e.app_namebattler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_character_list.*

//キャラクター一覧画面のクラス
class CharacterListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_list)

        // テキストの表示
        val textView = findViewById(R.id.text_characterlist) as TextView
        textView.text = "キャラ一覧"

        // 戻るボタン
        characterlist_back_button.setOnClickListener{
            finish()
        }




    }
}