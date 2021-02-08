package com.e.app_namebattler

import android.content.Intent
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_character_created.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CharacterCreatedActivity : AppCompatActivity() {

    lateinit var mp0: MediaPlayer
    lateinit var helper: MyOpenHelper
    private lateinit var player: Player

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_created)

        mp0= MediaPlayer.create(this,R.raw.yokoku)
        mp0.isLooping=true
        mp0.start()

        helper = MyOpenHelper(applicationContext)//DB作成

        // 名前を受け取る
        val nameExtra = intent.getStringExtra("name_key")
        // 職業を受け取る
        val jobExtra = intent.getStringExtra("job_key")
        // 名前の表示
        val nameText: TextView = findViewById(R.id.created_character_name_text_id)
        nameText.text = nameExtra

        // 職業の表示
        val jobText:TextView = findViewById(R.id.created_character_job_text_id)
        jobText.text = jobExtra

        val name = nameExtra.toString()

            when(jobExtra){
                //nameExtra:名前　Fighter(it):名前から戦士のキャラクターを作成 player = it: 作成したキャラクターをplayerに格納
                "戦士" -> nameExtra?.let { Fighter(it) }?.let { player = it }
                "魔法使い" -> nameExtra?.let { Wizard(it) }?.let { player = it }
                "僧侶" -> nameExtra?.let { Priest(it) }?.let { player = it }
                "忍者" -> nameExtra?.let { Ninja(it) }?.let { player = it }
            }
            var job = 0
            val hp = player.getHP()
            val mp = player.getMP()
            val str = player.getSTR()
            val def = player.getDEF()
            val agi = player.getAGI()
            val luck = player.getLUCK()

            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:m")
            val createAt = current.format(formatter).toString()

        //作成したキャラクターのステータス表示
            val hpText: TextView = findViewById(R.id.created_character_hp_text_id)
            hpText.text = hp.toString()

            val mpText: TextView = findViewById(R.id.created_character_mp_text_id)
            mpText.text = mp.toString()

            val strText: TextView = findViewById(R.id.created_character_str_text_id)
            strText.text = str.toString()

            val defText: TextView = findViewById(R.id.created_character_def_text_id)
            defText.text = def.toString()

            val agiText: TextView = findViewById(R.id.created_character_agi_text_id)
            agiText.text = agi.toString()

            val luckText: TextView = findViewById(R.id.created_character_luck_text_id)
                luckText.text = luck.toString()

            val db:SQLiteDatabase = helper.writableDatabase

        when(jobExtra){

            "戦士" -> {job = 0}
            "魔法使い" -> {job = 1}
            "僧侶" -> {job = 2}
            "忍者" -> {job = 3}
        }

        db.execSQL("INSERT INTO CHARACTER(NAME, JOB, HP, MP, STR, DEF, AGI, LUCK, CREATE_AT) VALUES ('$name','$job','$hp','$mp','$str','$def','$agi','$luck','$createAt')")

        db.close()

            // 戻るボタン押したときの処理
            created_character_back_button_id.setOnClickListener {
                val intent = Intent(this, CharacterCreationActivity::class.java)
                mp0.reset()
                startActivity(intent)
            }

            // 続けて作成するボタンを押したときの処理
            created_character_continuously_character_text_id.setOnClickListener{

                val characterCount: Int

                try {
                    // データベースのキャラクター数を取得する
                    val db = helper.readableDatabase
                     characterCount = DatabaseUtils.queryNumEntries(db,"CHARACTER").toInt()

                } finally {
                    // dbを開いたら確実にclose
                    db.close()
                }

               // キャラクター数8以上でダイアログが表示される
               if(characterCount >= 8){
                   val dialog = CharacterCreateMaxDialogFragment()
                   dialog.show(supportFragmentManager, "alert_dialog")

               //  キャラクター数が8未満の場合続けてキャラクターの作成をする
               }else {
                   val intent = Intent(this, CharacterCreationActivity::class.java)
                   mp0.reset()
                   startActivity(intent)
               }
            }

            // 作成を終了するボタンを押したときの処理
            created_character_end_creation_text_id.setOnClickListener{
                val intent = Intent(this, CharacterListActivity::class.java)
                mp0.reset()
                startActivity(intent)
            }
    }

    override fun onDestroy() {
        mp0.release()
        super.onDestroy()
    }

    override fun onBackPressed() {}
}


