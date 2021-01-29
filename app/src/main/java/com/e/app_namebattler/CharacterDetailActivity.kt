package com.e.app_namebattler

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_character_detail.*

// キャラクター詳細画面のクラス
class CharacterDetailActivity : AppCompatActivity() {

    lateinit var helper: MyOpenHelper

    var name = ""
    var jobValue = 0
    var hp = 0
    var mp = 0
    var str = 0
    var def = 0
    var agi = 0
    var luck = 0
    var create_at = ""
    var job = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_detail)

        // 名前を受け取る
        val nameExtra = intent.getStringExtra("name_key")

               helper = MyOpenHelper(applicationContext)//DB作成

        val db = helper.readableDatabase

        var cursor: Cursor? = null

        try {

            cursor = db.rawQuery("select * from CHARACTER WHERE name = '$nameExtra'", null)

            while (cursor.moveToNext()) {

                name = cursor.getString(0)
                job =  OccupationConversion(cursor.getInt(1))
                hp = cursor.getInt(2)
                mp = cursor.getInt(3)
                str = cursor.getInt(4)
                def = cursor.getInt(5)
                agi = cursor.getInt(6)
                luck = cursor.getInt(7)
                create_at = cursor.getString(8)
            }

        }finally {

            cursor?.close()
        }

        characterStatus()

        // 戻るボタンを押したときの処理
        character_detail_back_button_id.setOnClickListener {
            val intent = Intent(this, CharacterListActivity::class.java)
            startActivity(intent)
        }

        // 削除するボタンを押したときの処理
        character_detail_delete_button_id.setOnClickListener {

            deleteCharacter()
            characterStatus()
        }

    }

            private fun deleteCharacter(){

                helper = MyOpenHelper(applicationContext)//DB作成

                val db = helper.writableDatabase
                try{
                    db.execSQL("DELETE FROM CHARACTER WHERE NAME = '$name'")
                }finally {
                    db.close()
                }

                name = ""
                job = ""
                hp = 0
                mp = 0
                str = 0
                def = 0
                agi = 0
                luck = 0
                create_at = ""
            }

            private fun characterStatus(){

                val nameText: TextView = findViewById(R.id.character_detail_name_text_id)
                nameText.text = name

                val jobText: TextView = findViewById(R.id.character_detail_job_text_id)
                jobText.text = job

                val hpText: TextView = findViewById(R.id.character_detail_hp_text_id)
                hpText.text = ("%6d".format(hp))

                val mpText: TextView = findViewById(R.id.character_detail_mp_text_id)
                mpText.text = ("%6d".format(mp))

                val strText: TextView = findViewById(R.id.character_detail_str_text_id)
                strText.text = ("%6d".format(str))

                val defText: TextView = findViewById(R.id.character_detail_def_text_id)
                defText.text = ("%6d".format(def))

                val agiText: TextView = findViewById(R.id.character_detail_agi_text_id)
                agiText.text = ("%6d".format(agi))

                val luckText: TextView = findViewById(R.id.character_detail_luck_text_id)
                luckText.text = ("%6d".format(luck))

                val dateText: TextView = findViewById(R.id.character_detail_date_text_id)
                dateText.text = ("作成日：$create_at")
            }

    fun OccupationConversion(jobValue:Int):String{

        when(jobValue){
            0 -> {job = "戦士"}
            1 -> {job = "魔法使い"}
            2 -> {job = "僧侶"}
            3 -> {job = "忍者"}
        }
        return job
    }
}