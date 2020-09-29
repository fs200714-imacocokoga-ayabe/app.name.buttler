package com.e.app_namebattler

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_character_list.*

//キャラクター一覧画面のクラス
class CharacterListActivity : AppCompatActivity() {

    var helper: MyOpenHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_list)

        // テキストの表示
        val textView = findViewById(R.id.text_characterlist) as TextView
        textView.text = "キャラ一覧"

        // 戻るボタン
        characterlist_back_button.setOnClickListener {
            finish()
        }

        // キャラクター詳細画面に遷移
        characterlist_01_button.setOnClickListener {
            val intent = Intent(this,CharacterDetailActivity::class.java)
            startActivity(intent)
        }
        // キャラクター詳細画面に遷移
        characterlist_02_button.setOnClickListener {
            val intent = Intent(this,CharacterDetailActivity::class.java)
            startActivity(intent)
        }

        characterlist_03_button.setOnClickListener {
            val intent = Intent(this,CharacterDetailActivity::class.java)
            startActivity(intent)
        }

        characterlist_04_button.setOnClickListener {
            val intent = Intent(this,CharacterDetailActivity::class.java)
            startActivity(intent)
        }

        characterlist_05_button.setOnClickListener {
            val intent = Intent(this,CharacterDetailActivity::class.java)
            startActivity(intent)
        }

        characterlist_06_button.setOnClickListener {
            val intent = Intent(this,CharacterDetailActivity::class.java)
            startActivity(intent)
        }

        characterlist_07_button.setOnClickListener {
            val intent = Intent(this,CharacterDetailActivity::class.java)
            startActivity(intent)
        }

        characterlist_08_button.setOnClickListener {
            val intent = Intent(this,CharacterDetailActivity::class.java)
            startActivity(intent)
        }

        fun selectCHARACTER() {

            val db = helper!!.writableDatabase



            try {
                // rawQueryというSELECT専用メソッドを使用してデータを取得する
                val c = db.rawQuery(
                    "select NAME, JOB, HP, MP, STR, DEF, AGI, LUCK, CREATE_AT from CHARACTER",
                    null
                )
                // Cursorの先頭行があるかどうか確認
                var next = c.moveToFirst()
                // 最終的に表示する文字列
                var dispStr = ""
                // 取得した全ての行を取得
                while (next) {
                    // 取得したカラムの順番(0から始まる)と型を指定してデータを取得する
                    var rowdata = c.getString(0) + "\n" // NAMEを取得
                    rowdata += c.getInt(1).toString() + " " // JOBを取得
                    rowdata += "HP:" + c.getInt(2).toString() + " " // HPを取得
                    rowdata += "MP:" + c.getInt(3).toString() + " " // MPを取得
                    rowdata += "STR:" + c.getInt(4).toString() + " " // STRを取得
                    rowdata += "DEF:" + c.getInt(5).toString() + " " // DEFを取得
                    rowdata += "AGI:" + c.getInt(6).toString() + " " // AGIを取得
                    rowdata += c.getString(8) + "\n"

                    dispStr += """
                    $rowdata
                    """.trimIndent()
                    // 次の行が存在するか確認
                    next = c.moveToNext()
                }

            } finally {
                // finallyは、tryの中で例外が発生した時でも必ず実行される
                // dbを開いたら確実にclose
                db.close()
            }
        }


    }
}