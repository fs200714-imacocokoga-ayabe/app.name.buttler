package com.e.app_namebattler.view.view.activity

import android.content.Intent
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.e.app_namebattler.*
import com.e.app_namebattler.model.AllyOpenHelper
import com.e.app_namebattler.view.party.job.*
import com.e.app_namebattler.view.party.player.*
import com.e.app_namebattler.view.view.fragment.CharacterCreateMaxDialogFragment
import com.e.app_namebattler.view.view.music.MusicData
import kotlinx.android.synthetic.main.activity_character_created.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CharacterCreatedActivity : AppCompatActivity() {

    lateinit var mp0: MediaPlayer
    private lateinit var helper: AllyOpenHelper
    private lateinit var player: Player

    private val allyFighterImageList = ArrayList<AllyFighterImageData>()
    private val allyWizardImageList = ArrayList<AllyWizardImageData>()
    private val allyPriestImageList = ArrayList<AllyPriestImageData>()
    private val allyNinjaImageList = ArrayList<AllyNinjaImageData>()
    private var allyImage: Int = 0
    private val maxCharacterNumber = 8 // 登録できるキャラクター数

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_created)

        mp0 = MediaPlayer.create(this, MusicData.BGM04.getBgm())
        mp0.isLooping = true
        mp0.start()

        helper = AllyOpenHelper(applicationContext)//DB作成

        // 名前を受け取る
        val nameExtra = intent.getStringExtra("name_key")
        // 職業を受け取る
        val jobExtra = intent.getStringExtra("job_key")
        // 名前の表示
        val nameText: TextView = findViewById(R.id.character_created_character_name_text_id)
        nameText.text = nameExtra
        // 職業の表示
        val jobText: TextView = findViewById(R.id.character_created_character_job_text_id)
        jobText.text = jobExtra

        val name = nameExtra.toString()
        var job = 0

        // 職業を数字に変換
        when (jobExtra) {
            JobData.FIGHTER.getJobName() -> job = JobData.FIGHTER.getJobNumber()
            JobData.WIZARD.getJobName() -> job = JobData.WIZARD.getJobNumber()
            JobData.PRIEST.getJobName() -> job = JobData.PRIEST.getJobNumber()
            JobData.NINJA.getJobName() -> job = JobData.NINJA.getJobNumber()
        }

        // 職業と名前からキャラクターを作成
        when (job) {
            0 -> JobFighter(name).let { player = it }
            1 -> JobWizard(name).let { player = it }
            2 -> JobPriest(name).let { player = it }
            3 -> JobNinja(name).let { player = it }
        }

        val hp = player.hp
        val mp = player.mp
        val str = player.str
        val def = player.def
        val agi = player.agi
        val luck = player.luck

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:m")
        val createAt = current.format(formatter).toString()

        val characterImage = selectImage(job)

        //作成したキャラクターのステータス表示
        val hpText: TextView = findViewById(R.id.character_created_character_hp_text_id)
        hpText.text = hp.toString()

        val mpText: TextView = findViewById(R.id.character_created_character_mp_text_id)
        mpText.text = mp.toString()

        val strText: TextView = findViewById(R.id.character_created_character_str_text_id)
        strText.text = str.toString()

        val defText: TextView = findViewById(R.id.character_created_character_def_text_id)
        defText.text = def.toString()

        val agiText: TextView = findViewById(R.id.character_created_character_agi_text_id)
        agiText.text = agi.toString()

        val luckText: TextView = findViewById(R.id.character_created_character_luck_text_id)
        luckText.text = luck.toString()

        val characterImageView: ImageView = findViewById(R.id.character_created_imageView_id)
        characterImageView.setImageResource(characterImage)

        val db: SQLiteDatabase = helper.writableDatabase

        db.execSQL("INSERT INTO CHARACTER(NAME, JOB, HP, MP, STR, DEF, AGI, LUCK, CREATE_AT, CHARACTER_IMAGE) VALUES ('$name','$job','$hp','$mp','$str','$def','$agi','$luck','$createAt','$characterImage')")

        db.close()

        // 戻るボタン押したときの処理
        character_created_character_back_button_id.setOnClickListener {
            val intent = Intent(this, CharacterCreationActivity::class.java)
            mp0.reset()
            startActivity(intent)
        }

        // 続けて作成するボタンを押したときの処理
        character_created_character_continuously_character_text_id.setOnClickListener {

            val characterCount: Int

            try {
                // データベースのキャラクター数を取得する
                val db = helper.readableDatabase
                characterCount = DatabaseUtils.queryNumEntries(db, "CHARACTER").toInt()

            } finally {
                // dbを開いたら確実にclose
                db.close()
            }

            // キャラクター数8以上でダイアログが表示される
            if (maxCharacterNumber <= characterCount) {
                val dialog = CharacterCreateMaxDialogFragment()
                dialog.show(supportFragmentManager, "alert_dialog")

                //  キャラクター数が8未満の場合続けてキャラクターの作成をする
            } else {
                val intent = Intent(this, CharacterCreationActivity::class.java)
                mp0.reset()
                startActivity(intent)
            }
        }

        // 作成を終了するボタンを押したときの処理
        character_created_character_end_creation_text_id.setOnClickListener {
            val intent = Intent(this, CharacterListActivity::class.java)
            mp0.reset()
            startActivity(intent)
        }
    }

    // 味方キャラクターのイメージ画像をランダムで取得
    private fun selectImage(allyJob: Int): Int {

        for (image in AllyFighterImageData.values()) {
            allyFighterImageList.add(image)
        }

        for (image in AllyWizardImageData.values()) {
            allyWizardImageList.add(image)
        }

        for (image in AllyPriestImageData.values()) {
            allyPriestImageList.add(image)
        }

        for (image in AllyNinjaImageData.values()) {
            allyNinjaImageList.add(image)
        }

        when (allyJob) {
            0 -> allyImage =
                allyFighterImageList[(1..allyFighterImageList.size).random() - 1].getCharacterImage()
            1 -> allyImage =
                allyWizardImageList[(1..allyWizardImageList.size).random() - 1].getCharacterImage()
            2 -> allyImage =
                allyPriestImageList[(1..allyPriestImageList.size).random() - 1].getCharacterImage()
            3 -> allyImage =
                allyNinjaImageList[(1..allyNinjaImageList.size).random() - 1].getCharacterImage()
        }
        return allyImage
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


