package com.e.app_namebattler

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_party_orgnization.*

class PartyOrgnizationActivity : AppCompatActivity() {

    private var nameValue01: String? = null
    private var nameValue02: String? = null
    private var nameValue03: String? = null

    lateinit var helper: MyOpenHelper
    var characterList = arrayListOf<CharacterAllData>()
    private lateinit var mListAdapter: PartyListAdapter

    var name = ""
    var job = ""
    var hp = 0
    var mp = 0
    var str = 0
    var def = 0
    var agi = 0
    var luck = 0
    var create_at = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_party_orgnization)

        helper = MyOpenHelper(applicationContext)//DB作成

        val db = helper.readableDatabase

        characterList.clear()

        try {
            // rawQueryというSELECT専用メソッドを使用してデータを取得する
            val c = db.rawQuery(
                "select NAME, JOB, HP, MP, STR, DEF, AGI, LUCK, CREATE_AT from CHARACTER",
                null
            )
            // Cursorの先頭行があるかどうか確認
            var next = c.moveToFirst()

            // 取得した全ての行を取得
            while (next) {
                // 取得したカラムの順番(0から始まる)と型を指定してデータを取得する
                characterList.add(
                    CharacterAllData(
                        c.getString(0), c.getString(1),
                        c.getInt(2), c.getInt(3), c.getInt(4), c.getInt(5),
                        c.getInt(6), c.getInt(7), c.getString(8)
                    )
                )
                next = c.moveToNext()
            }

        } finally {
            // finallyは、tryの中で例外が発生した時でも必ず実行される
            // dbを開いたら確実にclose
            db.close()
        }

        val listView = findViewById<ListView>(R.id.list_View_party)

        mListAdapter = PartyListAdapter(this, characterList)

        listView.adapter = mListAdapter
        
        // 項目をタップしたときの処理
        listView.setOnItemClickListener { parent, view, position, id ->

            val nameValue = characterList[position].name
            val intent = Intent(this, CharacterDetailActivity::class.java)
            intent.putExtra("name_key", nameValue)
            startActivity(intent)
        }
            // このパーティで開始
            this_party_start.setOnClickListener {

                nameValue01 = characterList[listView.firstVisiblePosition].name
                nameValue02 = characterList[listView.firstVisiblePosition + 1].name
                nameValue03 = characterList[listView.firstVisiblePosition + 2].name

                val intent = Intent(this, BattleStartActivity::class.java)

                intent.putExtra("name_key01", nameValue01)
                intent.putExtra("name_key02", nameValue02)
                intent.putExtra("name_key03", nameValue03)

                startActivity(intent)
            }

            // 戻るボタン
            party_organizetion_back_button.setOnClickListener {
                val intent = Intent(this, TopScreenActivity::class.java)
                startActivity(intent)

            }


    }
}