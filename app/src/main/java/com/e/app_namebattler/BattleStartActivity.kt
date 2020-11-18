package com.e.app_namebattler

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_battle_start.*

class BattleStartActivity : AppCompatActivity() {

    lateinit var helper: MyOpenHelper

    private lateinit var mListAdapter: BattlePartyAdapter

    var  partyList = arrayListOf<CharacterAllData>()

    var enemyPartyList = arrayListOf<CharacterAllData>()

    private var enemyList = ArrayList<Player>()

    private val e = CreateEnemy()

    var name = ""
    var job = ""
    var hp = 0
    var mp = 0
    var str = 0
    var def = 0
    var agi = 0
    var luck = 0
    var create_at = ""

private var name01 = ""
    private var name02 = ""
    private var name03 = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle_start)

        // 名前を受け取る
        name01 = intent.getStringExtra("name_key01").toString()
        name02 = intent.getStringExtra("name_key02").toString()
        name03 = intent.getStringExtra("name_key03").toString()

        e.setName()

        enemyPartyList.clear()

        enemyPartyList = e.makeEnemy()

        printEnemy()

        printAlly()

        val namelist = listOf(name01,name02,name03)

        // 戻るボタンを押したときの処理
        battle_start_back_button.setOnClickListener{
            val intent = Intent(this, PartyOrgnizationActivity ::class.java)
            startActivity(intent)
        }

        // この相手と戦うボタンを押したときの処理
        this_enemy_battle_button.setOnClickListener {
            val intent = Intent(this, BattleMainActivity ::class.java)
            startActivity(intent)
        }

        // 相手を選び直すボタンを押したときの処理
        re_select_enemy_button.setOnClickListener {
            val intent = Intent(this, PartyOrgnizationActivity :: class.java)
            printEnemy()
           // startActivity(intent)

        }
    }

    // 味方キャラクター表示
    fun printAlly() {

        helper = MyOpenHelper(applicationContext)//DB作成

        val db = helper.readableDatabase

        try {
            // rawQueryというSELECT専用メソッドを使用してデータを取得する
            var c = db.rawQuery(
                "select * from CHARACTER WHERE name = '$name01' OR name = '$name02' OR name = '$name03'",
                null
            )

            // Cursorの先頭行があるかどうか確認
            var next = c.moveToFirst()

            // 取得した全ての行を取得
            while (next) {

                // 取得したカラムの順番(0から始まる)と型を指定してデータを取得する
                partyList.add(
                    CharacterAllData(
                        c.getString(0), c.getString(1),
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

        val listView = findViewById<ListView>(R.id.list_View_battle_party)

        mListAdapter = BattlePartyAdapter(this, partyList)

        listView.adapter = mListAdapter

    }

            // 敵キャラクター表示
            @RequiresApi(Build.VERSION_CODES.O)
            fun printEnemy(){

                e.setName()

                enemyPartyList.clear()

                enemyPartyList = e.makeEnemy()


                val listView = findViewById<ListView>(R.id.list_View_enemy_battle_party)

                mListAdapter = BattlePartyAdapter(this, enemyPartyList)

                listView.adapter = mListAdapter
            }

}