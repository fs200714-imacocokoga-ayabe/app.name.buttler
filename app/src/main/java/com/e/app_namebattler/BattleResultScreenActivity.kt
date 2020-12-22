package com.e.app_namebattler

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_battle_result_screen.*

class BattleResultScreenActivity : AppCompatActivity() {

    val pt = Party()
    val gm = GameManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle_result_screen)

        val allyName01 = intent.getStringExtra("name_key01")
        val allyName02 = intent.getStringExtra("name_key02")
        val allyName03 = intent.getStringExtra("name_key03")
        val enemyName01 = intent.getStringExtra("name_key04")
        val enemyName02 = intent.getStringExtra("name_key05")
        val enemyName03 = intent.getStringExtra("name_key06")
        val party00 = intent.getIntExtra("party_key",0)

        if (party00 == 0) {

            val imageView = findViewById<ImageView>(R.id.win_or_Loss_imageView_id)
            imageView.setImageResource(R.drawable.i_defeat)
            // "you win" を表示
        }else{

            val imageView = findViewById<ImageView>(R.id.win_or_Loss_imageView_id)
            imageView.setImageResource(R.drawable.i_victory)

        }

        // 次の対戦ボタンを押したときの処理
        next_battle_button_id.setOnClickListener {

            val intent = Intent(this, BattleStartActivity::class.java)

            intent.putExtra("name_key01", allyName01)
            intent.putExtra("name_key02", allyName02)
            intent.putExtra("name_key03", allyName03)

            startActivity(intent)
        }

        // 再挑戦ボタンを押したときの処理
        challenge_again_button_id.setOnClickListener {

            val intent = Intent(this, BattleMainActivity::class.java)

            intent.putExtra("name_key01", allyName01)
            intent.putExtra("name_key02", allyName02)
            intent.putExtra("name_key03", allyName03)
            intent.putExtra("name_key01", enemyName01)
            intent.putExtra("name_key02", enemyName02)
            intent.putExtra("name_key03", enemyName03)

            startActivity(intent)
        }

        // 対戦を終了するボタンを押したときの処理
        end_battle_button_id.setOnClickListener {

            val intent = Intent(this, TopScreenActivity::class.java)
            startActivity(intent)
        }


    }

    // 味方キャラクター01のステータスを表示する
    fun allyPrintStatus01(ally01: Player) {

        val allyName01Text: TextView = findViewById(R.id.result_ally_member01_name_id)
        allyName01Text.text = ally01.getName()

        val allyHp01Text: TextView = findViewById(R.id.result_ally_member01_hp_id)
        allyHp01Text.text = ("%s %d/%d".format("HP", ally01.hp, ally01.getMaxHp()))

        val allyMp01Text: TextView = findViewById(R.id.result_ally_member01_mp_id)
        allyMp01Text.text = ("%s %d/%d".format("MP", ally01.mp, ally01.getMaxMp()))
    }

    // 味方キャラクター02のステータスを表示する
    fun allyPrintStatus02(ally02: Player) {

        val allyName02Text: TextView = findViewById(R.id.result_ally_member02_name_id)
        allyName02Text.text = ally02.getName()

        val allyHp02Text: TextView = findViewById(R.id.result_ally_member02_hp_id)
        allyHp02Text.text = ("%s %d/%d".format("HP", ally02.hp, ally02.getMaxHp()))

        val allyMp02Text: TextView = findViewById(R.id.result_ally_member02_mp_id)
        allyMp02Text.text = ("%s %d/%d".format("MP", ally02.mp, ally02.getMaxMp()))
    }

    // 味方キャラクター03のステータスを表示する
    fun allyPrintStatus03(ally03: Player) {

        val allyName03Text: TextView = findViewById(R.id.result_ally_member03_name_id)
        allyName03Text.text = ally03.getName()

        val allyHp03Text: TextView = findViewById(R.id.result_ally_member03_hp_id)
        allyHp03Text.text = ("%s %d/%d".format("HP", ally03.hp, ally03.getMaxHp()))

        val allyMp03Text: TextView = findViewById(R.id.result_ally_member03_mp_id)
        allyMp03Text.text = ("%s %d/%d".format("MP", ally03.mp, ally03.getMaxMp()))

    }

    //  敵キャラクター01のステータスを表示する
    fun enemyPrintStatus01(enemy01: Player) {

        val enemyName01Text: TextView = findViewById(R.id.result_enemy_member01_name_id)
        enemyName01Text.text = enemy01.getName()

        val enemyHp01Text: TextView = findViewById(R.id.result_enemy_member01_hp_id)
        enemyHp01Text.text = ("%s %d/%d".format("HP", enemy01.hp, enemy01.getMaxHp()))

        val enemyMp01Text: TextView = findViewById(R.id.result_enemy_member01_mp_id)
        enemyMp01Text.text = ("%s %d/%d".format("MP", enemy01.mp, enemy01.getMaxMp()))

    }

    //  敵キャラクター02のステータスを表示する
    fun enemyPrintStatus02(enemy02: Player) {

        val enemyName02Text: TextView = findViewById(R.id.result_enemy_member02_name_id)
        enemyName02Text.text = enemy02.getName()

        val enemyHp02Text: TextView = findViewById(R.id.result_enemy_member02_hp_id)
        enemyHp02Text.text = ("%s %d/%d".format("HP", enemy02.hp, enemy02.getMaxHp()))

        val enemyMp02Text: TextView = findViewById(R.id.result_enemy_member02_mp_id)
        enemyMp02Text.text = ("%s %d/%d".format("MP", enemy02.mp, enemy02.getMaxMp()))

    }

    //  敵キャラクター03のステータスを表示する
    fun enemyPrintStatus03(enemy03: Player) {

        val enemyName03Text: TextView = findViewById(R.id.result_enemy_member03_name_id)
        enemyName03Text.text = enemy03.getName()

        val enemyHp03Text: TextView = findViewById(R.id.result_enemy_member03_hp_id)
        enemyHp03Text.text = ("%s %d/%d".format("HP", enemy03.hp, enemy03.getMaxHp()))

        val enemyMp03Text: TextView = findViewById(R.id.result_enemy_member03_mp_id)
        enemyMp03Text.text = ("%s %d/%d".format("MP", enemy03.mp, enemy03.getMaxMp()))

    }


}

