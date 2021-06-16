package com.e.app_namebattler.view.view.activity

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.e.app_namebattler.*
import com.e.app_namebattler.model.EnemyOpenHelper
import com.e.app_namebattler.model.AllyOpenHelper
import com.e.app_namebattler.view.party.job.JobData
import com.e.app_namebattler.view.party.player.CharacterAllData
import com.e.app_namebattler.view.party.player.CreateEnemy
import com.e.app_namebattler.view.view.adapter.BattleStartPartyAdapter
import com.e.app_namebattler.view.view.music.MusicData
import kotlinx.android.synthetic.main.activity_battle_start.*


class BattleStartActivity : AppCompatActivity() {

    lateinit var mp0: MediaPlayer
    lateinit var helper: AllyOpenHelper
    lateinit var helper02: EnemyOpenHelper
    private lateinit var mListAdapterStart: BattleStartPartyAdapter

    var allyPartyList = arrayListOf<CharacterAllData>()
    var enemyPartyList = arrayListOf<CharacterAllData>()
    private val enemy = CreateEnemy()

    var name = ""
    var job = ""
    var hp = 0
    var mp = 0
    var str = 0
    var def = 0
    var agi = 0
    var luck = 0

    private var name01 = ""
    private var name02 = ""
    private var name03 = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle_start)

        mp0 = MediaPlayer.create(this, MusicData.BGM02.getBgm())
        mp0.isLooping = true
        mp0.start()

        helper = AllyOpenHelper(applicationContext)//DB作成
        helper02 = EnemyOpenHelper(applicationContext)//DB作成

        // PartyOrganizationActivityから名前を受け取る
        name01 = intent.getStringExtra("allyName01_key").toString()
        name02 = intent.getStringExtra("allyName02_key").toString()
        name03 = intent.getStringExtra("allyName03_key").toString()

        // データベースから敵の情報を削除
        deleteEnemy()
        // CreateEnemyクラスで敵を作成
        enemy.setName()

        enemyPartyList.clear()
        enemyPartyList = enemy.makeEnemy()

        // 敵と味方の表示
        printEnemy()
        printAlly()

        // 戻るボタンを押したときの処理
        battle_start_back_button_id.setOnClickListener {
            val intent = Intent(this, PartyOrganizationActivity::class.java)
            mp0.reset()
            startActivity(intent)
        }

        // この相手と戦うボタンを押したときの処理
        battle_start_this_enemy_battle_button_id.setOnClickListener {

            val intent = Intent(this, BattleMainActivity::class.java)

            // BattleMainActivityにデータを送る
            intent.putExtra("allyName01_key", name01)
            intent.putExtra("allyName02_key", name02)
            intent.putExtra("allyName03_key", name03)
            intent.putExtra("enemyName01_key", enemyPartyList[0].name)
            intent.putExtra("enemyName02_key", enemyPartyList[1].name)
            intent.putExtra("enemyName03_key", enemyPartyList[2].name)

            //　敵の情報をデータベースに入れる
            saveData(enemyPartyList)
            mp0.reset()
            startActivity(intent)
        }

        // 相手を選び直すボタンを押したときの処理
        battle_start_re_select_enemy_button_id.setOnClickListener {
            val intent = Intent(this, PartyOrganizationActivity::class.java)
            // 敵を新しく作成して表示
            printEnemy()
        }
    }
    // 味方キャラクター表示
    private fun printAlly() {

        helper = AllyOpenHelper(applicationContext)//DB作成

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
                        c.getInt(6), c.getInt(7), c.getString(8), c.getInt(9), 100
                    )
                )
                next = c.moveToNext()
            }

        } finally {
            // finallyは、tryの中で例外が発生した時でも必ず実行される
            // dbを開いたら確実にclose
            db.close()
        }

        val listView =
            findViewById<ListView>(R.id.character_list_battle_start_ally_battle_party_listView_id)
        mListAdapterStart = BattleStartPartyAdapter(this, allyPartyList)
        listView.adapter = mListAdapterStart
    }
    // 敵キャラクター表示
    @RequiresApi(Build.VERSION_CODES.O)
    fun printEnemy() {
        enemy.setName()
        enemyPartyList.clear()
        enemyPartyList = enemy.makeEnemy()
        enemy.setEnemyParty(enemyPartyList)
        val listView = findViewById<ListView>(R.id.battle_start_enemy_battle_party_list_view_id)
        mListAdapterStart = BattleStartPartyAdapter(this, enemyPartyList)
        listView.adapter = mListAdapterStart
    }
    //　データベースに入れる処理
    private fun saveData(enemyPartyList: ArrayList<CharacterAllData>) {

        helper02 = EnemyOpenHelper(applicationContext)//DB作成
        val db: SQLiteDatabase = helper02.writableDatabase

        try {

            for (i in 1..enemyPartyList.size) {

                val enemyName = enemyPartyList[i - 1].name
                val enemyJob = enemyPartyList[i - 1].job
                val enemyHp = enemyPartyList[i - 1].hp
                val enemyMp = enemyPartyList[i - 1].mp
                val enemyStr = enemyPartyList[i - 1].str
                val enemyDef = enemyPartyList[i - 1].def
                val enemyAgi = enemyPartyList[i - 1].agi
                val enemyLuck = enemyPartyList[i - 1].luck
                val enemyDate = enemyPartyList[i - 1].create_at
                val enemyImage = enemyPartyList[i - 1].character_image

                var jobNumber = 0

                when (enemyJob) {

                    JobData.FIGHTER.getJobName() -> jobNumber = JobData.FIGHTER.getJobNumber()
                    JobData.WIZARD.getJobName() -> jobNumber = JobData.WIZARD.getJobNumber()
                    JobData.PRIEST.getJobName() -> jobNumber = JobData.PRIEST.getJobNumber()
                    JobData.NINJA.getJobName() -> jobNumber = JobData.NINJA.getJobNumber()
                }

                db.execSQL("INSERT INTO ENEMY(NAME, JOB, HP, MP, STR, DEF, AGI, LUCK, CREATE_AT, CHARACTER_IMAGE) VALUES ('$enemyName','$jobNumber','$enemyHp','$enemyMp','$enemyStr','$enemyDef','$enemyAgi','$enemyLuck','$enemyDate','$enemyImage')")
            }

        } finally {

            db.close()
        }
    }
    // データベースから削除する処理
    private fun deleteEnemy() {

        helper02 = EnemyOpenHelper(applicationContext)//DB作成
        val db: SQLiteDatabase = helper02.writableDatabase

        try {

            db.execSQL("DELETE FROM ENEMY")
        } finally {
            db.close()
        }
    }
    // 数字からjob に職業を格納
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





