package com.e.app_namebattler

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_battle_main.*


class BattleMainActivity : AppCompatActivity() {

    lateinit var helper: MyOpenHelper

    private var enemyPartyList = ArrayList<CharacterAllData>()

    private var allyPartyList = ArrayList<CharacterAllData>()

    private val e = CreateEnemy()

    var job =""

    var enemyPartyList02 = arrayListOf<CharacterAllData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle_main)

        val allyName01 = intent.getStringExtra("name01_key")
        val allyName02 = intent.getStringExtra("name02_key")
        val allyName03 = intent.getStringExtra("name03_key")
        val enemyName01 = intent.getStringExtra("enemyName01_key")
        val enemyName02 = intent.getStringExtra("enemyName02_key")
        val enemyName03 = intent.getStringExtra("enemyName03_key")

        enemyPartyList02 = printEnemy(enemyName01,enemyName02,enemyName03)

        val nameText: TextView = findViewById(R.id.ally_member01_name_id)
        nameText.text =allyName01

        val enemyNameText: TextView = findViewById(R.id.enemy_member01_name_id)
        enemyNameText.text =enemyPartyList02[0].name

        // 作戦変更画面に遷移
        strategy_change_button_id.setOnClickListener {
            val intent = Intent(this, StrategyChangeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun printEnemy(enemyName01: String?, enemyName02: String?, enemyName03: String?): ArrayList<CharacterAllData> {

        helper = MyOpenHelper(applicationContext)//DB作成

        val db = helper.readableDatabase

        try {
            // rawQueryというSELECT専用メソッドを使用してデータを取得する
            var c = db.rawQuery(
                "select * from ENEMY WHERE name = '$enemyName01' OR name = '$enemyName02' OR name = '$enemyName03'",
                null
            )

            // Cursorの先頭行があるかどうか確認
            var next = c.moveToFirst()

            // 取得した全ての行を取得
            while (next) {

                // 取得したカラムの順番(0から始まる)と型を指定してデータを取得する
                enemyPartyList.add(
                    CharacterAllData(
                        c.getString(0),(OccupationConversion(c.getInt(1))),
                        c.getInt(2), c.getInt(3), c.getInt(4), c.getInt(5),
                        c.getInt(6), c.getInt(7), c.getString(8)
                    )
                )
                next = c.moveToNext()
            }
            // }
        } finally {
            // finallyは、tryの中で例外が発生した時でも必ず実行される
            // dbを開いたら確実にclose
            db.close()
        }

        return enemyPartyList
    }

    private fun OccupationConversion(jobValue: Int):String{

        when(jobValue){

            0 -> {job = "戦士"}
            1 -> {job = "魔法使い"}
            2 -> {job = "僧侶"}
            3 -> {job = "忍者"}
        }

        return job
    }
}