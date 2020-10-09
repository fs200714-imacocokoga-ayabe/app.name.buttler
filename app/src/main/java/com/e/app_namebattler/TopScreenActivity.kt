package com.e.app_namebattler

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_top_screen.*

class TopScreenActivity : AppCompatActivity() {

    var helper: MyOpenHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_screen)
        // テーブルの作成
        helper = MyOpenHelper(this@TopScreenActivity)
      
        // キャラ一覧のボタン
        top_screen_character_list_button_id.setOnClickListener {
            val intent = Intent(this, CharacterListActivity::class.java)
            startActivity(intent)
        }
        // バトル開始ボタン
        top_screen_battle_start_button_id.setOnClickListener {
            val intent = Intent(this, PartyOrgnizationActivity::class.java)
            startActivity(intent)
        }

    }
    // selectCHARACTER()
    // CHARACTERの全行を取得
   // private fun selectCHARACTER(){
        // MyOpenHelperクラスがまだインスタンス化されていなかったらインスタンス化する
//        if (helper == null){
//            helper = MyOpenHelper(this@MainActivity)
//        }
        // データベースを取得する
//        val db = helper!!.writableDatabase
//        try {
//            // rawQueryというSELECT専用メソッドを使用してデータを取得する
//            val c = db.rawQuery(
//                "select NAME, JOB, HP, MP, STR, DEF, AGI, LUCK, CREATE_AT from CHARACTER",
//                null
//            )
//            // Cursorの先頭行があるかどうか確認
//            var next = c.moveToFirst()
//            // 最終的に表示する文字列
//            var dispStr = ""
//            // 取得した全ての行を取得
//            while (next) {
//                // 取得したカラムの順番(0から始まる)と型を指定してデータを取得する
//                var rowdata = c.getString(0) + " " // NAMEを取得
//                rowdata += c.getInt(1).toString() + " " // JOBを取得
//                rowdata += "HP:" + c.getInt(2).toString() + " " // HPを取得
//                rowdata += "MP:" + c.getInt(3).toString() + " " // MPを取得
//                rowdata += "STR:" + c.getInt(4).toString() + " " // STRを取得
//                rowdata += "DEF:" + c.getInt(5).toString() + " " // DEFを取得
//                rowdata += "AGI:" + c.getInt(6).toString() + " " // AGIを取得
//                rowdata += "LUCK:" + c.getInt(7).toString() + " " // LUCKを取得
//                rowdata += c.getString(8) + "\n"
//
//                dispStr += """
//                    $rowdata
//                    """.trimIndent()
//                // 次の行が存在するか確認
//                next = c.moveToNext()
//            }
//
//        }finally {
//            // finallyは、tryの中で例外が発生した時でも必ず実行される
//            // dbを開いたら確実にclose
//            db.close()
//        }
//    }
    // CHARACTERの挿入
    private fun incertCHARACTER(){
        // MyOpenHelperクラスがまだインスタンス化されていなかったらインスタンス化する
//        if (helper == null) {
//            helper = MyOpenHelper(this@MainActivity)
//        }
        val db = helper!!.writableDatabase
        try {

        } finally {
            db.close()
        }
    }
    // CHARACTERの削除
    private fun deleteCHARACTER(){
        // MyOpenHelperクラスがまだインスタンス化されていなかったらインスタンス化する
//        if (helper == null) {
//            helper = MyOpenHelper(this@MainActivity)
//        }
        val db = helper!!.writableDatabase
        try {

        }finally {
            db.close()
        }
    }

    private fun updateCHARACTER() {
//        if (helper == null) {
//            helper = MyOpenHelper(this@MainActivity)
//        }
        val db = helper!!.writableDatabase
        try {

        }finally {
            db.close()
        }
    }


}