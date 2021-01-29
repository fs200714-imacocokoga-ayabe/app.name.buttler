package com.e.app_namebattler

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_battle_start.*


class BattleStartActivity : AppCompatActivity() {

    lateinit var helper: MyOpenHelper

    private lateinit var mListAdapter: BattlePartyAdapter

    var  allyPartyList = arrayListOf<CharacterAllData>()

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
    var create_at = null

    private var name01 = ""
    private var name02 = ""
    private var name03 = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle_start)

        helper = MyOpenHelper(applicationContext)//DB作成

        // PartyOrganizationActivityから名前を受け取る
        name01 = intent.getStringExtra("name_key01").toString()
        name02 = intent.getStringExtra("name_key02").toString()
        name03 = intent.getStringExtra("name_key03").toString()

        deleteEnemy()
        e.setName()

        enemyPartyList.clear()
        enemyPartyList = e.makeEnemy()

        printEnemy()
        printAlly()

        // 戻るボタンを押したときの処理
        battle_start_back_button.setOnClickListener{
            val intent = Intent(this, PartyOrganizationActivity::class.java)
            startActivity(intent)
        }

        // この相手と戦うボタンを押したときの処理
        this_enemy_battle_button.setOnClickListener {

            val intent = Intent(this, BattleMainActivity::class.java)

            intent.putExtra("name01_key", name01)
            intent.putExtra("name02_key", name02)
            intent.putExtra("name03_key", name03)
            intent.putExtra("enemyName01_key", enemyPartyList[0].name)
            intent.putExtra("enemyName02_key", enemyPartyList[1].name)
            intent.putExtra("enemyName03_key", enemyPartyList[2].name)

            saveData(enemyPartyList)
            startActivity(intent)
        }

        // 相手を選び直すボタンを押したときの処理
        re_select_enemy_button.setOnClickListener {
            val intent = Intent(this, PartyOrganizationActivity::class.java)
            printEnemy()
        }
    }

    // 味方キャラクター表示
    private fun printAlly() {

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
                allyPartyList.add(
                    CharacterAllData(
                        c.getString(0), occupationConversion(c.getInt(1)),
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
        mListAdapter = BattlePartyAdapter(this, allyPartyList)
        listView.adapter = mListAdapter
    }

            // 敵キャラクター表示
            @RequiresApi(Build.VERSION_CODES.O)
            fun printEnemy(){
                e.setName()
                enemyPartyList.clear()
                enemyPartyList = e.makeEnemy()
                e.setEnemyParty(enemyPartyList)
                val listView = findViewById<ListView>(R.id.list_View_enemy_battle_party)
                mListAdapter = BattlePartyAdapter(this, enemyPartyList)
                listView.adapter = mListAdapter
            }

            fun saveData(enemyPartyList: ArrayList<CharacterAllData>) {

                val db: SQLiteDatabase = helper.writableDatabase

                try {

                    for (i in 1.. enemyPartyList.size) {

                        val enemyName = enemyPartyList[i -1].name
                        val enemyJob = enemyPartyList[i - 1].job
                        val enemyHp = enemyPartyList[i - 1].hp
                        val enemyMp = enemyPartyList[i - 1].mp
                        val enemyStr = enemyPartyList[i - 1].str
                        val enemyDef = enemyPartyList[i - 1].def
                        val enemyAgi = enemyPartyList[i - 1].agi
                        val enemyLuck = enemyPartyList[i - 1].luck
                        val enemyDate = enemyPartyList[i - 1].create_at

                        var jobI = 0

                        when(enemyJob){

                            "戦士" -> {jobI = 0}
                            "魔法使い" -> {jobI = 1}
                            "僧侶" -> {jobI = 2}
                            "忍者" -> {jobI = 3}
                        }

                        db.execSQL("INSERT INTO ENEMY(NAME, JOB, HP, MP, STR, DEF, AGI, LUCK, CREATE_AT) VALUES ('$enemyName','$jobI','$enemyHp','$enemyMp','$enemyStr','$enemyDef','$enemyAgi','$enemyLuck','$enemyDate')")
                    }

                }finally {

                    db.close()
                }
            }

    fun deleteEnemy(){

        val db = helper.writableDatabase
        try{
            db.execSQL("DELETE FROM ENEMY")
        }finally {
            db.close()
        }
    }

    private fun occupationConversion(jobValue: Int): String {

        when (jobValue) {
            0 -> { job = "戦士" }
            1 -> { job = "魔法使い" }
            2 -> { job = "僧侶" }
            3 -> { job = "忍者" }
        }
        return job
    }
}





