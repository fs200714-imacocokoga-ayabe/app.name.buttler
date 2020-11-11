package com.e.app_namebattler

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_character_list.*

//キャラクター一覧画面のクラス
class CharacterListActivity : AppCompatActivity(){

    lateinit var helper: MyOpenHelper
    var  characterList = arrayListOf<CharacterAllData>()
    private lateinit var mListAdapter: ListAdapter

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
        setContentView(R.layout.activity_character_list)

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

//        if (characterList.size != 0) {
//                val i = 0
//                name = characterList[i].name
//                job = characterList[i].job
//                hp = characterList[i].hp
//                mp = characterList[i].mp
//                str = characterList[i].str
//                def = characterList[i].def
//                agi = characterList[i].agi
//                luck = characterList[i].luck
//                create_at = characterList[i].create_at
//
//            } else {
//
//            name = "名無し"
//            job = "なし"
//            hp = 100
//            mp = 100
//            str = 100
//            def = 100
//            agi = 100
//            luck = 100
//            create_at = "2020/11/11 11:11"
//        }

            val listView = findViewById<ListView>(R.id.list_View)

                mListAdapter = ListAdapter(this, characterList)

                listView.adapter = mListAdapter
                // 項目をタップしたときの処理
                listView.setOnItemClickListener { parent, view, position, id ->


                    val nameValue  = characterList[position].name
                    val intent = Intent(this, CharacterDetailActivity::class.java)
                    intent.putExtra("name_key", nameValue)
                    startActivity(intent)



        }

        // テキストの表示
        val textView = findViewById<TextView>(R.id.text_characterlist)
        textView.text = "キャラ一覧(".plus(characterList.size).plus("人)")

        // 戻るボタンを押したときの処理
        characterlist_back_button.setOnClickListener {
            val intent = Intent(this, TopScreenActivity::class.java)
            startActivity(intent)
        }

        // 新しく作成するボタンを押したときの処理
        new_create_button.setOnClickListener {
            val intent = Intent(this, CharacterCreationActivity::class.java)
            //intent.putExtra("characterNumber_key",characterList.size)
            startActivity(intent)
        }

    }

}

class CharacterAllData(
    val name: String,
    val job: String,
    val hp: Int,
    val mp: Int,
    val str: Int,
    val def: Int,
    val agi: Int,
    val luck: Int,
    val create_at: String
)