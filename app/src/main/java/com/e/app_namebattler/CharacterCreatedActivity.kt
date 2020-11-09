package com.e.app_namebattler

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_character_created.*
import kotlinx.android.synthetic.main.activity_character_detail.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CharacterCreatedActivity : AppCompatActivity() {
    lateinit var helper: MyOpenHelper
    private lateinit var player: Player

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_created)

        helper = MyOpenHelper(applicationContext)//DB作成
        // 名前と職業を受け取る
        val nameExtra = intent.getStringExtra("name_key")
        val jobExtra = intent.getStringExtra("job_key")
        // 名前の表示
        val nameText: TextView = findViewById(R.id.textView13)
        nameText.text = nameExtra
        // 職業の表示
        val jobText:TextView = findViewById(R.id.textView14)
        jobText.text = jobExtra
        
        val name = nameExtra.toString()
        val job = jobExtra.toString()

            when(jobExtra){
//                "戦士" -> player = (Fighter(name))
//                "魔法使い" -> player = (Wizard(name))
//                "僧侶" -> player = (Priest(name))
//                "忍者" -> player = (Ninja(name))
                "戦士" -> nameExtra?.let { Fighter(it) }?.let { player = it }
                "魔法使い" -> nameExtra?.let { Wizard(it) }?.let { player = it }
                "僧侶" -> nameExtra?.let { Priest(it) }?.let { player = it }
                "忍者" -> nameExtra?.let { Ninja(it) }?.let { player = it }
            }

            val hp = player.getHP()
            val mp = player.getMP()
            val str = player.getSTR()
            val def = player.getDEF()
            val agi = player.getAGI()
            val luck = player.getLUCK()

            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:m")
            val create_at = current.format(formatter).toString()

            val hpText: TextView = findViewById(R.id.created_character_hp_text_id)
            hpText.text = "HP                     $hp"

            val mpText: TextView = findViewById(R.id.created_character_mp_text_id)
            mpText.text = "MP                     $mp"

            val strText: TextView = findViewById(R.id.created_character_str_text_id)
            strText.text = "STR                    $str"

            val defText: TextView = findViewById(R.id.created_character_def_text_id)
            defText.text = "DEF                    $def"

            val agiText: TextView = findViewById(R.id.created_character_agi_text_id)
            agiText.text = "AGI                     $agi"

            val luckText: TextView = findViewById(R.id.created_character_luck_text_id)
                luckText.text = "LUCK                  $luck"

            val db:SQLiteDatabase = helper.writableDatabase

                db.execSQL("INSERT INTO CHARACTER(NAME, JOB, HP, MP, STR, DEF, AGI, LUCK, CREATE_AT) VALUES ('$name','$job','$hp','$mp','$str','$def','$agi','$luck','$create_at')")

            db.close()

            // 戻るボタン押したときの処理
            created_character_back_button_id.setOnClickListener {
                val intent = Intent(this, CharacterCreationActivity::class.java)
                startActivity(intent)
            }

            // 続けて作成するボタンを押したときの処理
            created_character_Continuouslycharacter_text_id.setOnClickListener{
                val intent = Intent(this,CharacterCreationActivity::class.java)
                startActivity(intent)
            }

            // 作成を終了するボタンを押したときの処理
            created_character_end_creation_text_id.setOnClickListener{
                val intent = Intent(this,CharacterListActivity::class.java)
                startActivity(intent)
            }

    }

}

         private fun saveData(){

             lateinit var helper: MyOpenHelper

             val db:SQLiteDatabase = helper.writableDatabase
            // db.execSQL("INSERT INTO CHARACTER(NAME, JOB, HP, MP, STR, DEF, AGI, LUCK, CREATE_AT) VALUES ('$name','$job','$hp','$mp','$str','$def','$agi','$luck','$create_at')")
           
            db.close()
         }

    // fun saveData() {
    // val db: SQLiteDatabase = helper.writableDatabase
//        val value: ContentValues = ContentValues()
//        //名前を取得
//        val createNameValues = findViewById<EditText>(R.id.name_input_field_text_id) as EditText
//        //職業を取得
//        val radioGroup_job : RadioGroup = findViewById(R.id.character_select_radiogroup_id)
//        val radioId = radioGroup_job.checkedRadioButtonId
//        val createJobValues: RadioButton = radioGroup_job.findViewById(radioId)
//        //表示させる形式に変数を変換
//        val nameValue:String = createNameValues.text.toString()
//        val jobValue:Int = createJobValues.text.toString().toInt()
//

//        db.execSQL("INSERT INTO CHARACTER(NAME, JOB, HP, MP, STR, DEF, AGI, LUCK, CREATE_AT) VALUES　('name','job','hp','mp','str','def','agi','luck',(strftime('%Y/%m/%d %H:%M','now','LOCALTIME')))")


