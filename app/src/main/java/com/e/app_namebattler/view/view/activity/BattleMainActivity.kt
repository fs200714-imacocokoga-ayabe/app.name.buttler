package com.e.app_namebattler.view.view.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Point
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.e.app_namebattler.R
import com.e.app_namebattler.controller.BattleLogListener
import com.e.app_namebattler.controller.GameManager
import com.e.app_namebattler.model.AllyOpenHelper
import com.e.app_namebattler.model.EnemyOpenHelper
import com.e.app_namebattler.view.party.job.JobData
import com.e.app_namebattler.view.party.player.CharacterAllData
import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.strategy.StrategyData
import com.e.app_namebattler.view.view.adapter.BattleMainRecyclerAdapter
import com.e.app_namebattler.view.view.adapter.MemberStatusData
import com.e.app_namebattler.view.view.illust.BackGroundData
import com.e.app_namebattler.view.view.message.Comment
import com.e.app_namebattler.view.view.music.MusicData
import com.e.app_namebattler.view.view.music.SoundData
import kotlinx.android.synthetic.main.activity_battle_main.*

class BattleMainActivity : AppCompatActivity(), View.OnClickListener, BattleLogListener {

    private lateinit var s: String
    lateinit var mp0: MediaPlayer
    private lateinit var sp0: SoundPool
    private var snd0 = 0
    private var snd1 = 0
    private var snd2 = 0
    private var snd3 = 0
    private var snd4 = 0
    private var snd5 = 0
    private var snd6 = 0
    private var snd7 = 0
    private var snd8 = 0
    private var snd9 = 0
    private var snd10 = 0
    private var snd11 = 0
    private var snd12 = 0
    private var snd13 = 0
    private var snd14 = 0
    private var snd15 = 0

    private var handler = Handler()
    private lateinit var allyHelper: AllyOpenHelper
    private lateinit var enemyhelper: EnemyOpenHelper

    private var toast: Toast? = null

    private val gm = GameManager()

    private var enemyPartyList = ArrayList<CharacterAllData>()
    private var allyPartyList = ArrayList<CharacterAllData>()
    private lateinit var memberList: MutableList<MemberStatusData>

    private var strategyNumber = 0 //作戦番号を格納
    var job = "" // 職業名を格納

    private var isNextTurn: Boolean = false// true: 2ターン目以降　false:バトルログ画面を最初にタップするとtrueになる
    private var isMessageSpeed: Boolean = false// メッセージ速度変更に使用
    private var isTurnEnd: Boolean = false

    var allyPartyCount = 1
    var enemyPartyCount = 1

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("CutPasteId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle_main)

        val size2 = Point()
        this@BattleMainActivity.windowManager.getDefaultDisplay().getRealSize(size2)
        println("テキストサイズpx${size2.x}")
        println("テキストサイズpy${size2.y}")

        val size1 = Point()
        this@BattleMainActivity.windowManager.getDefaultDisplay().getSize(size1)
        println("テキストサイズpx$size1.x")
        println("テキストサイズpy$size1.y")

        backGround()// 背景選択

        mp0 = MediaPlayer.create(this, MusicData.BGM01.getBgm())
        mp0.isLooping = true
        mp0.start()

        val aa0 = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(
            AudioAttributes.CONTENT_TYPE_SPEECH).build()
        sp0 = SoundPool.Builder().setAudioAttributes(aa0).setMaxStreams(2).build()

        snd0 = sp0.load(this, SoundData.S_SWORD01.getSound(), 1)
        snd1 = sp0.load(this, SoundData.S_KATANA01.getSound(), 1)
        snd2 = sp0.load(this, SoundData.S_PUNCH01.getSound(), 1)
        snd3 = sp0.load(this, SoundData.S_SYURIKEN01.getSound(), 1)
        snd4 = sp0.load(this, SoundData.S_FIRE01.getSound(), 1)
        snd5 = sp0.load(this, SoundData.S_THUNDER01.getSound(), 1)
        snd6 = sp0.load(this, SoundData.S_POISON01.getSound(), 1)
        snd7 = sp0.load(this, SoundData.S_PARALYSIS01.getSound(), 1)
        snd8 = sp0.load(this, SoundData.S_HEAL01.getSound(), 1)
        snd9 = sp0.load(this, SoundData.S_RECOVERY01.getSound(), 1)
        snd10 = sp0.load(this, SoundData.S_KATANA02.getSound(), 1)
        snd11 = sp0.load(this, SoundData.S_POISON_DAMAGE.getSound(), 1)
        snd12 = sp0.load(this, SoundData.S_SLIDE01.getSound(), 1)
        snd13 = sp0.load(this, SoundData.S_SWORD02.getSound(), 1)
        snd14 = sp0.load(this, SoundData.S_SWORD01_AIR_SHOT.getSound(), 1)
        snd15 = sp0.load(this, SoundData.S_SWORD02_AIR_SHOT.getSound(), 1)

        this.handler = Handler()

        // "Tap to start"の表示とバトルログ画面のタップ処理
        val battleMainLogTextDialog =
            findViewById<View>(R.id.battle_main_battle_log_text_id) as TextView
        battleMainLogTextDialog.setOnClickListener(this)

        val nextButtonText = findViewById<TextView>(R.id.battle_main_next_turn_button_id)
        nextButtonText.text = Comment.M_BATTLE_START_COMMENT.getComment()

        val bl = findViewById<TextView>(R.id.battle_main_battle_log_text_id)
        bl.text = Comment.M_SPEED_CHANGE_COMMENT.getComment()

        // BattleStartActivityからデータを受け取る
        val allyName01 = intent.getStringExtra("allyName01_key")
        val allyName02 = intent.getStringExtra("allyName02_key")
        val allyName03 = intent.getStringExtra("allyName03_key")
        val enemyName01 = intent.getStringExtra("enemyName01_key")
        val enemyName02 = intent.getStringExtra("enemyName02_key")
        val enemyName03 = intent.getStringExtra("enemyName03_key")

        //  最初の作戦の表示
        printStrategy()

        // 敵のデータを名前から取得する
        enemyPartyList = getEnemyData(enemyName01, enemyName02, enemyName03)
        // 味方のデータを名前から取得する
        allyPartyList = getAllyData(allyName01, allyName02, allyName03)

        gm.callBack = this// GameManagerクラスからBattleLogListenerを経てデータを戻す準備

        // コントロールをGameManagerに移譲
        gm.controlTransfer(allyPartyList, enemyPartyList)

        // 作戦変更画面に遷移する処理
        battle_main_strategy_change_button_id.setOnClickListener {
            val intent = Intent(this, StrategyChangeActivity::class.java)
            startActivityForResult(intent, 1000)
        }

        // 次のターンボタンを押したときの処理
        battle_main_next_turn_button_id.setOnClickListener {

            nextButtonText.text = Comment.M_IN_BATTLE_COMMENT.getComment()
            if (!isNextTurn){
                gm.battle(strategyNumber)
                isNextTurn = true
                battleEnd()
              //  nextButtonText.text = Comment.M_BATTLE_NEXT_TURN_COMMENT.getComment()

            }else if (isNextTurn && isTurnEnd) {

                // どちらかのパーティが全滅した場合
                if (gm.getAllyParty().isEmpty() || gm.getEnemyParty().isEmpty()) {
                    // GameManagerクラス から　CharacterDataクラスにデータを渡す処理
                    gm.sendData()

                    val intent = Intent(this, BattleResultActivity::class.java)

                    // パーティの勝ち負け判定に使用　Party01のsizeが0なら敵の勝利1以上なら味方の勝利
                    val allyPartySurvivalNumber = gm.getAllyParty().size
                    
                    intent.putExtra("party_key", allyPartySurvivalNumber)

                    mp0.reset()

                    if (toast != null) {
                        toast!!.cancel()
                    }

                    startActivity(intent)

                    //どちらのパーティも全滅していない場合
                } else {

                    gm.battle(strategyNumber)
                    // turnEnd()
                    isTurnEnd = false
                }

            } else {

                printMessage(Comment.M_NOT_END_TURN_COMMENT.getComment())
            }
        }
    }

    // 終了判定
    private fun battleEnd() {

        allyPartyCount = gm.getAllyParty().size
        enemyPartyCount = gm.getEnemyParty().size

        if (gm.getAllyParty().isEmpty() || gm.getEnemyParty().isEmpty()) {
            // GameManagerクラス から　CharacterDataクラスにデータを渡す処理
            gm.sendData()

            val intent = Intent(this, BattleResultActivity::class.java)

            // BattleResultActivityに味方パーティの生存数を送る
            intent.putExtra("party_key", gm.getAllyParty().size)

            mp0.reset()

            if (toast != null) {
                toast!!.cancel()
            }

            startActivity(intent)
        }
        isTurnEnd = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode != 1000) {
            return
        }

        val sn = findViewById<TextView>(R.id.battle_main_strategy_text_id)

        if (resultCode == Activity.RESULT_OK && data != null) {

            var message = ""

            strategyNumber = data.getIntExtra("strategy_key", 0)

            when (strategyNumber) {

                0 -> message = StrategyData.S0.getStrategyName()
                1 -> message = StrategyData.S1.getStrategyName()
                2 -> message = StrategyData.S2.getStrategyName()
                3 -> message = StrategyData.S3.getStrategyName()
                4 -> message = StrategyData.S4.getStrategyName()
            }
            sn.text = message

        } else if (resultCode == Activity.RESULT_CANCELED) {

            sn.text = StrategyData.S0.getStrategyName()
        }
    }

    // 名前を受け取りデータベースから敵キャラクターのステータスを取得する
    private fun getEnemyData(
        enemyName01: String?,
        enemyName02: String?,
        enemyName03: String?
    ): ArrayList<CharacterAllData> {

        enemyhelper = EnemyOpenHelper(applicationContext)//DB作成

        val db = enemyhelper.readableDatabase

        try {
            // rawQueryというSELECT専用メソッドを使用してデータを取得する
            var c = db.rawQuery(
                "select * from ENEMY WHERE name = '$enemyName01' OR name = '$enemyName02' OR name = '$enemyName03'",
                null
            )

            // Cursorの先頭行があるかどうか確認
            var next = c.moveToFirst()

            // 取得した全ての行を取得
            while (next) {
                // 取得したカラムの順番(0から始まる)と型を指定してデータを取得する
                enemyPartyList.add(
                    CharacterAllData(
                        c.getString(0),
                        occupationConversion(c.getInt(1)),
                        c.getInt(2),
                        c.getInt(3),
                        c.getInt(4),
                        c.getInt(5),
                        c.getInt(6),
                        c.getInt(7),
                        c.getString(8),
                        c.getInt(9), 100
                    )
                )
                next = c.moveToNext()
            }

        } finally {
            // finallyは、tryの中で例外が発生した時でも必ず実行される
            // dbを開いたら確実にclose
            db.close()
        }
        return enemyPartyList
    }

    // 名前を受け取りデータベースから味方キャラクターのステータスを取得する
    private fun getAllyData(
        allyName01: String?,
        allyName02: String?,
        allyName03: String?
    ): ArrayList<CharacterAllData> {

        allyHelper = AllyOpenHelper(applicationContext)//DB作成

        val db = allyHelper.readableDatabase

        try {
            // rawQueryというSELECT専用メソッドを使用してデータを取得する
            var c = db.rawQuery(
                "select * from CHARACTER WHERE name = '$allyName01' OR name = '$allyName02' OR name = '$allyName03'",
                null
            )
            // Cursorの先頭行があるかどうか確認
            var next = c.moveToFirst()

            // 取得した全ての行を取得
            while (next) {

                // 取得したカラムの順番(0から始まる)と型を指定してデータを取得する
                allyPartyList.add(
                    CharacterAllData(
                        c.getString(0),
                        occupationConversion(c.getInt(1)),
                        c.getInt(2),
                        c.getInt(3),
                        c.getInt(4),
                        c.getInt(5),
                        c.getInt(6),
                        c.getInt(7),
                        c.getString(8),
                        c.getInt(9), 100
                    )
                )
                next = c.moveToNext()
            }

        } finally {
            // finallyは、tryの中で例外が発生した時でも必ず実行される
            // dbを開いたら確実にclose
            db.close()
        }
        return allyPartyList
    }

    // 職業を数字から文字に変換する
    private fun occupationConversion(jobValue: Int): String {

        when (jobValue) {
            0 -> job = JobData.FIGHTER.getJobName()
            1 -> job = JobData.WIZARD.getJobName()
            2 -> job = JobData.PRIEST.getJobName()
            3 -> job = JobData.NINJA.getJobName()
        }
        return job
    }

    // 作戦の表示
    private fun printStrategy() {
        val strategyText: TextView = findViewById(R.id.battle_main_strategy_text_id)
        strategyText.text = StrategyData.S0.getStrategyName()
    }

    // バトルログ画面でのタップした時の処理
    override fun onClick(v: View?) {

        if (isNextTurn) {
            isMessageSpeed = !isMessageSpeed // 反転

            if (isMessageSpeed) {
                printMessage(Comment.M_NEXT_TURN_FAST_COMMENT.getComment())
            } else {
                printMessage(Comment.M_NEXT_TURN_SLOW_COMMENT.getComment())
            }
        }
    }

    //  初期ステータス表示　GameManagerクラスからBattleLogListener経て呼ばれる
    override fun upDateInitialStatus(
        ally01StatusLog: MutableList<String>,
        ally02StatusLog: MutableList<String>,
        ally03StatusLog: MutableList<String>,
        enemy01StatusLog: MutableList<String>,
        enemy02StatusLog: MutableList<String>,
        enemy03StatusLog: MutableList<String>,
        ally01: Player,
        ally02: Player,
        ally03: Player,
        enemy01: Player,
        enemy02: Player,
        enemy03: Player
    ) {
        printAllyStatus(0,
            ally01StatusLog,
            ally02StatusLog,
            ally03StatusLog,
            ally01,
            ally02,
            ally03,
            false)
        printEnemyStatus(0,
            enemy01StatusLog,
            enemy02StatusLog,
            enemy03StatusLog,
            enemy01,
            enemy02,
            enemy03,
            true)
    }

    // バトルログとステータスの表示
    override fun upDateAllLog(
        battleLog: MutableList<Any>,
        ally01StatusLog: MutableList<String>,
        ally02StatusLog: MutableList<String>,
        ally03StatusLog: MutableList<String>,
        enemy01StatusLog: MutableList<String>,
        enemy02StatusLog: MutableList<String>,
        enemy03StatusLog: MutableList<String>,
        ally01: Player,
        ally02: Player,
        ally03: Player,
        enemy01: Player,
        enemy02: Player,
        enemy03: Player
    ) {

        // バトルログの最後に▼を入れる
        battleLog[battleLog.size - 1] =
            battleLog[battleLog.size - 1].toString() + "\n                              ▼"

        if (!isMessageSpeed) {

            for (i in 1..battleLog.size) {

                when (i) {

                    1 -> {
                        handler.postDelayed({
                            printBattleLog(battleLog[i - 1].toString())
                            printAllyStatus(0, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03, false)
                            printEnemyStatus(0, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03, false)
                        }, 100)

                        handler.postDelayed({
                            printAllyStatus(0, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03, true)
                            printEnemyStatus(0, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03, true)
                        }, 500)
                    }

                    2 -> {
                        handler.postDelayed({
                            printBattleLog(battleLog[i - 1].toString())
                            printAllyStatus(9, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03, false)
                            printEnemyStatus(9, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03, false)
                        }, 3300)

                        handler.postDelayed({
                            printAllyStatus(9, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03, true)
                            printEnemyStatus(9, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03, true)
                        }, 3700)
                    }

                    3 -> {
                        handler.postDelayed({
                            printBattleLog(battleLog[i - 1].toString())
                            printAllyStatus(18, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03, false)
                            printEnemyStatus(18, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03, false)
                        }, 6300)

                        handler.postDelayed({
                            printAllyStatus(18, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03, true)
                            printEnemyStatus(18, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03, true)
                        }, 6700)
                    }

                    4 -> {
                        handler.postDelayed({
                            printBattleLog(battleLog[i - 1].toString())
                            printAllyStatus(27, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03, false)
                            printEnemyStatus(27, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03, false)
                        }, 9300)

                        handler.postDelayed({
                            printAllyStatus(27, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03, true)
                            printEnemyStatus(27, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03, true)
                        }, 9700)
                    }

                    5 -> {
                        handler.postDelayed({
                            printBattleLog(battleLog[i - 1].toString())
                            printAllyStatus(36, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03, false)
                            printEnemyStatus(36, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03, false)
                        }, 12300)

                        handler.postDelayed({
                            printAllyStatus(36, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03, true)
                            printEnemyStatus(36, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03, true)
                        }, 12700)
                    }

                    6 -> {
                        handler.postDelayed({
                            printBattleLog(battleLog[i - 1].toString())
                            printAllyStatus(45, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03, false)
                            printEnemyStatus(45, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03, false)
                        }, 15300)

                        handler.postDelayed({
                            printAllyStatus(45, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03, true)
                            printEnemyStatus(45, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03, true)
                        }, 15700)
                    }
                }
            }

            waitTime(battleLog.size) // 待機時間処理

        } else if (isMessageSpeed) {

            for (i in 1..battleLog.size) {

                when (i) {

                    1 -> {
                        handler.postDelayed({
                            printBattleLog(battleLog[i - 1].toString())
                            shorteningPrintAllyStatus(0, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03, true)
                            shorteningPrintEnemyStatus(0, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03, true)
                        }, 100)
                    }

                    2 -> {
                        handler.postDelayed({
                            printBattleLog(battleLog[i - 1].toString())
                            shorteningPrintAllyStatus(9, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03, true)
                            shorteningPrintEnemyStatus(9, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03, true)
                        }, 200)
                    }

                    3 -> {
                        handler.postDelayed({
                            printBattleLog(battleLog[i - 1].toString())
                            shorteningPrintAllyStatus(18, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03, true)
                            shorteningPrintEnemyStatus(18, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03, true)
                        }, 300)
                    }

                    4 -> {
                        handler.postDelayed({
                            printBattleLog(battleLog[i - 1].toString())
                            shorteningPrintAllyStatus(27, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03, true)
                            shorteningPrintEnemyStatus(27, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03, true)
                        }, 400)
                    }

                    5 -> {
                        handler.postDelayed({
                            printBattleLog(battleLog[i - 1].toString())
                            shorteningPrintAllyStatus(36, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03, true)
                            shorteningPrintEnemyStatus(36, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03, true)
                        }, 500)
                    }

                    6 -> {
                        handler.postDelayed({
                            printBattleLog(battleLog[i - 1].toString())
                            shorteningPrintAllyStatus(45, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03, true)
                            shorteningPrintEnemyStatus(45, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03, true)
                        }, 600)
                    }
                }
            }
            waitTime(battleLog.size) // 待機時間処理
        }
    }

    private fun waitTime(battleLog: Int) {

        val nextButtonText = findViewById<TextView>(R.id.battle_main_next_turn_button_id)

        if (!isMessageSpeed) {
            when (battleLog) {
                1 -> handler.postDelayed({ isTurnEnd = true
                    nextButtonText.text = Comment.M_BATTLE_NEXT_TURN_COMMENT.getComment()}, 200)
                2 -> handler.postDelayed({ isTurnEnd = true
                    nextButtonText.text = Comment.M_BATTLE_NEXT_TURN_COMMENT.getComment()}, 3500)
                3 -> handler.postDelayed({ isTurnEnd = true
                    nextButtonText.text = Comment.M_BATTLE_NEXT_TURN_COMMENT.getComment()}, 6500)
                4 -> handler.postDelayed({ isTurnEnd = true
                    nextButtonText.text = Comment.M_BATTLE_NEXT_TURN_COMMENT.getComment()}, 9500)
                5 -> handler.postDelayed({ isTurnEnd = true
                    nextButtonText.text = Comment.M_BATTLE_NEXT_TURN_COMMENT.getComment()}, 12500)
                6 -> handler.postDelayed({ isTurnEnd = true
                    nextButtonText.text = Comment.M_BATTLE_NEXT_TURN_COMMENT.getComment()}, 15500)
            }

        } else {

            when (battleLog) {
                1 -> handler.postDelayed({ isTurnEnd = true
                    nextButtonText.text = Comment.M_BATTLE_NEXT_TURN_COMMENT.getComment()}, 100)
                2 -> handler.postDelayed({ isTurnEnd = true
                    nextButtonText.text = Comment.M_BATTLE_NEXT_TURN_COMMENT.getComment()}, 200)
                3 -> handler.postDelayed({ isTurnEnd = true
                    nextButtonText.text = Comment.M_BATTLE_NEXT_TURN_COMMENT.getComment()}, 300)
                4 -> handler.postDelayed({ isTurnEnd = true
                    nextButtonText.text = Comment.M_BATTLE_NEXT_TURN_COMMENT.getComment()}, 400)
                5 -> handler.postDelayed({ isTurnEnd = true
                    nextButtonText.text = Comment.M_BATTLE_NEXT_TURN_COMMENT.getComment()}, 500)
                6 -> handler.postDelayed({ isTurnEnd = true
                    nextButtonText.text = Comment.M_BATTLE_NEXT_TURN_COMMENT.getComment()}, 600)
            }
        }
    }

    private fun printEnemyStatus(
        num: Int,
        enemy01StatusLog: MutableList<String>,
        enemy02StatusLog: MutableList<String>,
        enemy03StatusLog: MutableList<String>,
        enemy01: Player,
        enemy02: Player,
        enemy03: Player,
        isEnemySecondTimes: Boolean
    ) {

        val enemy001 = getMemberStatusData(num, enemy01StatusLog, enemy01, isEnemySecondTimes)
        val enemy002 = getMemberStatusData(num, enemy02StatusLog, enemy02, isEnemySecondTimes)
        val enemy003 = getMemberStatusData(num, enemy03StatusLog, enemy03, isEnemySecondTimes)

        memberList = arrayListOf(enemy001, enemy002, enemy003)

        val layoutManager = LinearLayoutManager(
            this,
            RecyclerView.HORIZONTAL,
            false
        ).apply {

            battle_main_enemy_status_recycleView_id.layoutManager = this
        }

        BattleMainRecyclerAdapter(memberList).apply {

            battle_main_enemy_status_recycleView_id.adapter = this
        }

        battle_main_enemy_status_recycleView_id.adapter = BattleMainRecyclerAdapter(memberList)
        (battle_main_enemy_status_recycleView_id.adapter as BattleMainRecyclerAdapter).setOnItemClickListener(
            object : BattleMainRecyclerAdapter.OnItemClickListener {
                @SuppressLint("ResourceAsColor")

                override fun onItemClickListener(
                    viw: View,
                    position: Int
                ) {
                    when (position) {
                        0 -> setImageType(enemy01)
                        1 -> setImageType(enemy02)
                        2 -> setImageType(enemy03)
                    }
                }
            })
    }

    // バトルログ表示
    private fun printBattleLog(battleLog: String) {
        val textLog = findViewById<TextView>(R.id.battle_main_battle_log_text_id)
        textLog.text = battleLog
    }

    private fun printAllyStatus(
        num: Int,
        ally01StatusLog: MutableList<String>,
        ally02StatusLog: MutableList<String>,
        ally03StatusLog: MutableList<String>,
        ally01: Player,
        ally02: Player,
        ally03: Player,
        isAllySecondTimes: Boolean
    ) {

        val ally001 = getMemberStatusData(num, ally01StatusLog, ally01, isAllySecondTimes)
        val ally002 = getMemberStatusData(num, ally02StatusLog, ally02, isAllySecondTimes)
        val ally003 = getMemberStatusData(num, ally03StatusLog, ally03, isAllySecondTimes)

        memberList = arrayListOf(ally001, ally002, ally003)

        val layoutManager = LinearLayoutManager(
            this,
            RecyclerView.HORIZONTAL,
            false
        ).apply {

            battle_main_ally_status_recycleView_id.layoutManager = this
        }

        BattleMainRecyclerAdapter(memberList).apply {

            battle_main_ally_status_recycleView_id.adapter = this
        }

        battle_main_ally_status_recycleView_id.adapter = BattleMainRecyclerAdapter(memberList)
        (battle_main_ally_status_recycleView_id.adapter as BattleMainRecyclerAdapter).setOnItemClickListener(
            object : BattleMainRecyclerAdapter.OnItemClickListener {
                override fun onItemClickListener(
                    viw: View,
                    position: Int
                ) {
                    when (position) {
                        0 -> setImageType(ally01)
                        1 -> setImageType(ally02)
                        2 -> setImageType(ally03)
                    }
                }
            })
    }

    // バトルメイン画面でステータスをタップでキャラクターのステータスを表示する
    @SuppressLint("ShowToast", "InflateParams")
    private fun setImageType(character: Player) {

        if (toast != null) {
            toast!!.cancel()
        }

        val layoutInflater = layoutInflater
        val customToastView: View =
            layoutInflater.inflate(R.layout.toast_layout_character_status, null)

        (customToastView.findViewById(R.id.toast_layout_imageView_id) as ImageView).setImageResource(
            character.getCharacterImageType())
        toast = Toast.makeText(customToastView.context, "", Toast.LENGTH_SHORT)
        toast!!.setGravity(Gravity.CENTER, 0, 0)
        (customToastView.findViewById(R.id.toast_layout_job_id) as TextView).text =
            "${character.job}"
        (customToastView.findViewById(R.id.toast_layout_str_id) as TextView).text =
            "${character.str}"
        (customToastView.findViewById(R.id.toast_layout_def_id) as TextView).text =
            "${character.def}"
        (customToastView.findViewById(R.id.toast_layout_agi_id) as TextView).text =
            "${character.agi}"
        (customToastView.findViewById(R.id.toast_layout_luck_id) as TextView).text =
            "${character.luck}"
        toast!!.setView(customToastView)
        toast!!.show()
    }

    private fun printMessage(message: String) {

        if (toast != null) {
            toast!!.cancel()
        }

        val layoutInflater = layoutInflater
        val customToastView: View = layoutInflater.inflate(R.layout.toast_layout_message, null)
        toast = Toast.makeText(customToastView.context, "", Toast.LENGTH_LONG)
        toast!!.setGravity(Gravity.BOTTOM, 0, 250)
        (customToastView.findViewById(R.id.toast_layout_message_id) as TextView).text = message
        toast!!.setView(customToastView)
        toast!!.show()
    }

    private fun sound(sound: Int) {

        when (sound) {

            0 -> { println() }
            1 -> sp0.play(snd0, 1.0f, 1.0f, 0, 0, 1.0f)// sword
            2 -> sp0.play(snd1, 1.0f, 1.0f, 0, 0, 1.0f)// katana
            3 -> sp0.play(snd2, 1.0f, 1.0f, 0, 0, 1.0f)// punch
            4 -> sp0.play(snd3, 1.0f, 1.0f, 0, 0, 1.0f)// syuriken
            5 -> sp0.play(snd4, 1.0f, 1.0f, 0, 0, 1.0f)// fire
            6 -> sp0.play(snd5, 1.0f, 1.0f, 0, 0, 1.0f)// thunder
            7 -> sp0.play(snd6, 1.0f, 1.0f, 0, 0, 1.0f)// poison
            8 -> sp0.play(snd7, 1.0f, 1.0f, 0, 0, 1.0f)// paralysis
            9 -> sp0.play(snd8, 1.0f, 1.0f, 0, 0, 1.0f)// heal
            10 -> sp0.play(snd9, 1.0f, 1.0f, 0, 0, 1.0f)// recovery
            11 -> sp0.play(snd10, 1.0f, 1.0f, 0, 0, 1.0f)// katana02
            12 -> sp0.play(snd11, 1.0f, 1.0f, 0, 0, 1.0f)// poison-damage
            13 -> sp0.play(snd12, 1.0f, 1.0f, 0, 0, 1.0f)// 滑る
            14 -> sp0.play(snd13, 1.0f, 1.0f, 0, 0, 1.0f)//
            15 -> sp0.play(snd14, 1.0f, 1.0f, 0, 0, 1.0f)//
            16 -> sp0.play(snd15, 1.0f, 1.0f, 0, 0, 1.0f)//
        }
    }

    // 背景の設定
    private fun backGround() {

        val backGroundList = ArrayList<BackGroundData>()
        val backGroundView: TextView = findViewById(R.id.battle_main_battle_log_text_id)

        for (bg in BackGroundData.values()) {
            backGroundList.add(bg)
        }

        val backGroundValue =
            backGroundList[(1..backGroundList.size).random() - 1].getBackGround()

        backGroundView.setBackgroundResource(backGroundValue)
    }

    override fun onDestroy() {
        mp0.release()
        sp0.release()
        super.onDestroy()
    }

    override fun onBackPressed() {}

    private fun shorteningPrintEnemyStatus(
        num: Int,
        enemy01StatusLog: MutableList<String>,
        enemy02StatusLog: MutableList<String>,
        enemy03StatusLog: MutableList<String>,
        enemy01: Player,
        enemy02: Player,
        enemy03: Player,
        isEnemySecondTimes: Boolean
    ) {

        val enemy001 = getShorteningMemberStatusData(num, enemy01StatusLog, enemy01)
        val enemy002 = getShorteningMemberStatusData(num, enemy02StatusLog, enemy02)
        val enemy003 = getShorteningMemberStatusData(num, enemy03StatusLog, enemy03)

        memberList = arrayListOf(enemy001, enemy002, enemy003)

        val layoutManager = LinearLayoutManager(
            this,
            RecyclerView.HORIZONTAL,
            false
        ).apply {

            battle_main_enemy_status_recycleView_id.layoutManager = this
        }

        BattleMainRecyclerAdapter(memberList).apply {

            battle_main_enemy_status_recycleView_id.adapter = this
        }

        battle_main_enemy_status_recycleView_id.adapter = BattleMainRecyclerAdapter(memberList)
        (battle_main_enemy_status_recycleView_id.adapter as BattleMainRecyclerAdapter).setOnItemClickListener(
            object : BattleMainRecyclerAdapter.OnItemClickListener {
                @SuppressLint("ResourceAsColor")

                override fun onItemClickListener(
                    viw: View,
                    position: Int
                ) {
                    when (position) {
                        0 -> setImageType(enemy01)
                        1 -> setImageType(enemy02)
                        2 -> setImageType(enemy03)
                    }
                }
            })
    }

    private fun shorteningPrintAllyStatus(
        num: Int,
        ally01StatusLog: MutableList<String>,
        ally02StatusLog: MutableList<String>,
        ally03StatusLog: MutableList<String>,
        ally01: Player,
        ally02: Player,
        ally03: Player,
        isAllySecondTimes: Boolean
    ) {

        val ally001 = getShorteningMemberStatusData(num, ally01StatusLog, ally01)
        val ally002 = getShorteningMemberStatusData(num, ally02StatusLog, ally02)
        val ally003 = getShorteningMemberStatusData(num, ally03StatusLog, ally03)

        memberList = arrayListOf(ally001, ally002, ally003)

        val layoutManager = LinearLayoutManager(
            this,
            RecyclerView.HORIZONTAL,
            false
        ).apply {

            battle_main_ally_status_recycleView_id.layoutManager = this
        }

        BattleMainRecyclerAdapter(memberList).apply {

            battle_main_ally_status_recycleView_id.adapter = this
        }

        battle_main_ally_status_recycleView_id.adapter = BattleMainRecyclerAdapter(memberList)
        (battle_main_ally_status_recycleView_id.adapter as BattleMainRecyclerAdapter).setOnItemClickListener(
            object : BattleMainRecyclerAdapter.OnItemClickListener {
                override fun onItemClickListener(
                    viw: View,
                    position: Int
                ) {
                    when (position) {
                        0 -> setImageType(ally01)
                        1 -> setImageType(ally02)
                        2 -> setImageType(ally03)
                    }
                }
            })
    }

    private fun getMemberStatusData(
        num: Int,
        characterStatusLog: MutableList<String>,
        character00: Player,
        isSecondTimes: Boolean
    ): MemberStatusData {

        val character00Hp = characterStatusLog[num + 0]                     // HP
        val character00MaxHp = characterStatusLog[num + 1]                  // 最大HP
        val character00Mp = characterStatusLog[num + 2]                     // MP
        val character00MaxMp = characterStatusLog[num + 3]                  // 最大MP
        val character00Poison = characterStatusLog[num + 4]                 // 毒
        val character00Paralysis = characterStatusLog[num + 5]              // 麻痺
        val character00PrintStatusEffect = characterStatusLog[num + 6]      // ダメージ、回復の効果
        characterStatusLog[num + 6] = 0.toString()         // リセット
        val character00StatusSoundEffect = characterStatusLog[num + 7]      // 毒、麻痺の効果音
        val character00AttackSoundEffect = characterStatusLog[num + 8]      // 攻撃の効果音
        sound(character00AttackSoundEffect.toInt())

        // 表示2回目はスキップ
        if (isSecondTimes) {
            sound(character00StatusSoundEffect.toInt())                             // 効果音の処理
            characterStatusLog[num + 7] = 0.toString()
        }

        return MemberStatusData(("  %s".format(character00.getName())),
            ("%s %s/%s".format("  HP",
                character00Hp,
                character00MaxHp)),
            ("%s %s/%s".format("  MP", character00Mp, character00MaxMp)),
            ("%s %s".format(
                character00Poison,
                character00Paralysis)),
            (character00Hp.toInt()), (character00PrintStatusEffect.toInt()))
    }

    private fun getShorteningMemberStatusData(
        num: Int,
        characterStatusLog: MutableList<String>,
        character00: Player
    ): MemberStatusData {

        val character00Hp = characterStatusLog[num + 0]          // HP
        val character00MaxHp = characterStatusLog[num + 1]       // 最大HP
        val character00Mp = characterStatusLog[num + 2]          // MP
        val character00MaxMp = characterStatusLog[num + 3]       // 最大MP
        val character00Poison = characterStatusLog[num + 4]      // 毒
        val character00Paralysis = characterStatusLog[num + 5]   // 麻痺
        val character00PrintStatusEffect = 0.toString()          // ダメージ、回復の効果

        return MemberStatusData(("  %s".format(character00.getName())),
            ("%s %s/%s".format("  HP",
                character00Hp,
                character00MaxHp)),
            ("%s %s/%s".format("  MP", character00Mp, character00MaxMp)),
            ("%s %s".format(
                character00Poison,
                character00Paralysis)),
            (character00Hp.toInt()), (character00PrintStatusEffect.toInt()))
    }
}


