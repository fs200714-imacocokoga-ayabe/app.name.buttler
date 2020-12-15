package com.e.app_namebattler

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_battle_main.*
import kotlinx.android.synthetic.main.activity_battle_result_screen.*

class BattleResultScreenActivity : AppCompatActivity() {

    val pt = Party()
    val gm = GameManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle_result_screen)

        // "you lose" を表示
        if (gm.getParty01().isEmpty()) {

            val imageView = findViewById<ImageView>(R.id.win_or_Loss_imageView_id)
            imageView.setImageResource(R.drawable.i_defeat)
            // "you win" を表示
        }else{

            val imageView = findViewById<ImageView>(R.id.win_or_Loss_imageView_id)
            imageView.setImageResource(R.drawable.i_victory)

        }

        // 次の対戦ボタンを押したときの処理
        next_battle_button_id.setOnClickListener {
            val intent = Intent(this, PartyOrgnizationActivity::class.java)
            startActivity(intent)
        }

        // 再挑戦ボタンを押したときの処理
        challenge_again_button_id.setOnClickListener {
            val intent = Intent(this, BattleStartActivity::class.java)
            startActivity(intent)
        }

        // 対戦を終了するボタンを押したときの処理
        end_battle_button_id.setOnClickListener {
            val intent = Intent(this, TopScreenActivity::class.java)
            startActivity(intent)
        }


    }

}

