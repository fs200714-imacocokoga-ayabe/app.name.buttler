package com.e.app_namebattler.view.view.activity

import android.content.Intent
import android.database.Cursor
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.e.app_namebattler.model.AllyOpenHelper
import com.e.app_namebattler.R
import com.e.app_namebattler.view.party.job.JobData
import com.e.app_namebattler.view.view.music.MusicData
import kotlinx.android.synthetic.main.activity_character_detail.*

// キャラクター詳細画面のクラス
class CharacterDetailActivity : AppCompatActivity() {

    lateinit var mp0: MediaPlayer
    private lateinit var helper: AllyOpenHelper

    var name = ""
    var job = ""
    var hp = 0
    var mp = 0
    var str = 0
    var def = 0
    var agi = 0
    var luck = 0
    private var createAt = ""
    private var characterImage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_detail)

        mp0 = MediaPlayer.create(this, MusicData.BGM04.getBgm())
        mp0.isLooping = true
        mp0.start()

        // 名前を受け取る
        val nameExtra = intent.getStringExtra("name_key")

        helper = AllyOpenHelper(applicationContext)//DB作成

        val db = helper.readableDatabase

        var cursor: Cursor? = null

        try {

            cursor = db.rawQuery("select * from CHARACTER WHERE name = '$nameExtra'", null)

            while (cursor.moveToNext()) {

                name = cursor.getString(0)
                job = occupationConversion(cursor.getInt(1))
                hp = cursor.getInt(2)
                mp = cursor.getInt(3)
                str = cursor.getInt(4)
                def = cursor.getInt(5)
                agi = cursor.getInt(6)
                luck = cursor.getInt(7)
                createAt = cursor.getString(8)
                characterImage = cursor.getInt(9)
            }

        } finally {

            cursor?.close()
        }

        characterStatus()

        // 戻るボタンを押したときの処理
        character_detail_back_button_id.setOnClickListener {
            val intent = Intent(this, CharacterListActivity::class.java)
            mp0.reset()
            startActivity(intent)
        }

        // 削除するボタンを押したときの処理
        character_detail_delete_button_id.setOnClickListener {

            deleteCharacter()
            characterStatus()
        }
    }

    private fun deleteCharacter() {

        helper = AllyOpenHelper(applicationContext)//DB作成

        val db = helper.writableDatabase
        try {
            db.execSQL("DELETE FROM CHARACTER WHERE NAME = '$name'")
        } finally {
            db.close()
        }
        // 削除するボタンを押したときに表示するもの
        name = ""
        job = ""
        hp = 0
        mp = 0
        str = 0
        def = 0
        agi = 0
        luck = 0
        createAt = ""
        characterImage = 0
    }

    // キャラクターステータス表示
    private fun characterStatus() {

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
        dateText.text = ("作成日：$createAt")

        val characterImageView: ImageView = findViewById(R.id.character_detail_imageView_id)
        characterImageView.setImageResource(characterImage)
    }

    // 数字を職業に変換
    private fun occupationConversion(jobValue: Int): String {

        when (jobValue) {
            0 -> job = JobData.FIGHTER.getJobName()
            1 -> job = JobData.WIZARD.getJobName()
            2 -> job = JobData.PRIEST.getJobName()
            3 -> job = JobData.NINJA.getJobName()
        }
        return job
    }

    override fun onDestroy() {
        mp0.release()
        super.onDestroy()
    }

    override fun onPause() {
        mp0.pause()
        super.onPause()
    }

    override fun onResume() {
        mp0.start()
        super.onResume()
    }

    override fun onBackPressed() {}
}