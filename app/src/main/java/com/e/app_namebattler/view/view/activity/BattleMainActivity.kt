package com.e.app_namebattler.view.view.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
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
import com.e.app_namebattler.model.EnemyOpenHelper
import com.e.app_namebattler.model.AllyOpenHelper
import com.e.app_namebattler.view.party.job.JobData
import com.e.app_namebattler.view.party.player.CharacterAllData
import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.strategy.StrategyData
import com.e.app_namebattler.view.view.adapter.BattleMainRecyclerAdapter
import com.e.app_namebattler.view.view.adapter.MemberStatusData
import com.e.app_namebattler.view.view.music.MusicData
import com.e.app_namebattler.view.view.music.SoundData
import kotlinx.android.synthetic.main.activity_battle_main.*


class BattleMainActivity : AppCompatActivity(), View.OnClickListener, BattleLogListener {

    lateinit var mp0: MediaPlayer
    lateinit var sp0:SoundPool
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

    //private var handler: Handler? = null
    private var handler = Handler()
    private lateinit var helper: AllyOpenHelper
    private lateinit var helper02: EnemyOpenHelper

    private val gm = GameManager()

    private var enemyPartyList = ArrayList<CharacterAllData>()
    private var allyPartyList = ArrayList<CharacterAllData>()
    private lateinit var memberList: MutableList<MemberStatusData>

    private var strategyNumber = 0 //作戦番号を格納
    var job = "" // 職業名を格納

    private var isNextTurn:Boolean = false// true: 最初のターンが実行される　false:バトルログ画面を最初にタップするとtrueになる
    private var isMessageSpeed:Boolean = false// メッセージ速度変更に使用
    private var isTurnEnd:Boolean = false

    var party01Count = 1
    var party02Count = 1

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("CutPasteId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle_main)

        mp0= MediaPlayer.create(this, MusicData.BGM01.getBgm())
        mp0.isLooping=true
        mp0.start()

        val aa0=AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(
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

        this.handler = Handler()

        // "Tap to start"の表示とバトルログ画面のタップ処理
        val battleMainLogTextDialog =
            findViewById<View>(R.id.battle_main_battle_log_text_id) as TextView
        battleMainLogTextDialog.setOnClickListener(this)
        val bl = findViewById<TextView>(R.id.battle_main_battle_log_text_id)
        bl.text = "Tap to start"

        // BattleStartActivityからデータを受け取る
        val allyName01 = intent.getStringExtra("name01_key")
        val allyName02 = intent.getStringExtra("name02_key")
        val allyName03 = intent.getStringExtra("name03_key")
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

                if (isNextTurn && isTurnEnd) {

                    // どちらかのパーティが全滅した場合
                    if (gm.getParty01().isEmpty() || gm.getParty02().isEmpty()) {
                        // GameManagerクラス から　CharacterDataクラスにデータを渡す処理
                        gm.sendData()

                        val intent = Intent(this, BattleResultActivity::class.java)

                        // パーティの勝ち負け判定に使用　Party01のsizeが0なら敵の勝利1以上なら味方の勝利
                        val party00 = gm.getParty01().size
                        // BattleResultActivityにparty00を送る
                        intent.putExtra("party_key", party00)

                        mp0.reset()
                        startActivity(intent)

                        //どちらのパーティも全滅していない場合
                    } else {

                        gm.battle(strategyNumber)
                       // turnEnd()
                        isTurnEnd = false
                      //  isCount = false
                    }
               // }

                }else{

                    val ts = Toast.makeText(this, "ターンが終了していません", Toast.LENGTH_SHORT)
                    ts.setGravity(Gravity.BOTTOM, 0, 30)
                    ts.show()
                }
        }
    }

    private fun turnEnd() {

        party01Count = gm.getParty01().size
        party02Count = gm.getParty02().size

        if (gm.getParty01().isEmpty() || gm.getParty02().isEmpty()) {
            // GameManagerクラス から　CharacterDataクラスにデータを渡す処理
            gm.sendData()

            val intent = Intent(this, BattleResultActivity::class.java)

            // パーティの勝ち負け判定に使用　Party01のsizeが0なら敵の勝利1以上なら味方の勝利
            val party00 = gm.getParty01().size
            // BattleResultActivityにparty00を送る
            intent.putExtra("party_key", party00)

            mp0.reset()
            startActivity(intent)
        }
        isTurnEnd = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode != 1000) { return }

        val sn = findViewById<TextView>(R.id.battle_main_strategy_text_id)

        if (resultCode == Activity.RESULT_OK && data != null){

            var message = ""

            strategyNumber = data.getIntExtra("strategy_key",0)

            when (strategyNumber) {

                0 -> message = StrategyData.S0.getStrategyName()
                1 -> message = StrategyData.S1.getStrategyName()
                2 -> message = StrategyData.S2.getStrategyName()
                3 -> message = StrategyData.S3.getStrategyName()
                4 -> message = StrategyData.S4.getStrategyName()
            }
            sn.text = message

        } else if(resultCode == Activity.RESULT_CANCELED){

            sn.text = StrategyData.S0.getStrategyName()
        }
    }

    // 名前を受け取りデータベースから敵キャラクターのステータスを取得する
    private fun getEnemyData(
        enemyName01: String?,
        enemyName02: String?,
        enemyName03: String?
    ): ArrayList<CharacterAllData> {

        helper02 = EnemyOpenHelper(applicationContext)//DB作成

        val db = helper02.readableDatabase

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
                        c.getInt(9)
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

        helper = AllyOpenHelper(applicationContext)//DB作成

        val db = helper.readableDatabase

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
                        c.getInt(9)
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
        strategyText.text =  StrategyData.S0.getStrategyName()
    }

    // バトルログ画面でのタップした時の処理
    override fun onClick(v: View?) {

        // 最初のターンの場合
        if (!isNextTurn){

            gm.battle(strategyNumber)
            turnEnd()
            isNextTurn = true

            val ms00 = Toast.makeText(this, "タップで次のターンの速度を変更できます", Toast.LENGTH_SHORT)
            ms00.setGravity(Gravity.CENTER, 0, 250)
            ms00.show()

            //2ターン目以降の場合
        }else {

            isMessageSpeed = !isMessageSpeed // 反転

            if (isMessageSpeed) {

                val ms01 = Toast.makeText(this, "次のターンのメッセージ速度：早い", Toast.LENGTH_SHORT)
                ms01.setGravity(Gravity.CENTER, 0, 250)
                ms01.show()

            } else {

                val ms02 = Toast.makeText(this, "次のターンのメッセージ速度：遅い", Toast.LENGTH_SHORT)
                ms02.setGravity(Gravity.CENTER, 0, 250)
                ms02.show()
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
        printAllyStatus(0, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03,false)
        printEnemyStatus(0, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03, true)
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

        if (!isMessageSpeed) {

            for (i in 1..battleLog.size) {

                when (i) {

                    1 -> {
                            handler.postDelayed({
                                printBattleLog(battleLog[i - 1].toString())
                                printAllyStatus(0, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03, false)
                                printEnemyStatus(0, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03,false)
                            }, 100)

                            handler.postDelayed({
                                printAllyStatus(0, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03, true)
                                printEnemyStatus(0, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03, true)

                        }, 300)

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
                            }, 3500)
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
                            }, 6500)
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
                            }, 9500)
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
                            }, 12500)
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
                            }, 15500)
                    }
                }
            }
        } else if (isMessageSpeed) {

            loop@ for (i in 1..battleLog.size) {

                when (i) {

                    1 -> {
                            handler.postDelayed({
                                printBattleLog(battleLog[i - 1].toString())
                                printAllyStatus(0, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03, false)
                                printEnemyStatus(0, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03, false)
                            }, 90)

                        handler.postDelayed({
                            printBattleLog(battleLog[i - 1].toString())
                            printAllyStatus(0, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03, true)
                            printEnemyStatus(0, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03, true)
                        }, 100)
                    }

                    2 -> {
                            handler.postDelayed({
                                printBattleLog(battleLog[i - 1].toString())
                                printAllyStatus(9, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03, false)
                                printEnemyStatus(9, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03, false)
                            }, 190)

                        handler.postDelayed({
                            printBattleLog(battleLog[i - 1].toString())
                            printAllyStatus(9, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03, true)
                            printEnemyStatus(9, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03, true)
                        }, 200)
                    }

                    3 -> {
                            handler.postDelayed({
                                printBattleLog(battleLog[i - 1].toString())
                                printAllyStatus(18, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03, false)
                                printEnemyStatus(18, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03, false)
                            }, 290)

                        handler.postDelayed({
                            printBattleLog(battleLog[i - 1].toString())
                            printAllyStatus(18, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03, true)
                            printEnemyStatus(18, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03, true)
                        }, 300)
                    }

                    4 -> {
                            handler.postDelayed({
                                printBattleLog(battleLog[i - 1].toString())
                                printAllyStatus(27, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03, false)
                                printEnemyStatus(27, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03, false)
                            }, 390)

                        handler.postDelayed({
                            printBattleLog(battleLog[i - 1].toString())
                            printAllyStatus(27, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03, true)
                            printEnemyStatus(27, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03, true)
                        }, 400)
                    }

                    5 -> {
                            handler.postDelayed({
                                printBattleLog(battleLog[i - 1].toString())
                                printAllyStatus(36, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03, false)
                                printEnemyStatus(36, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03, false)
                            }, 490)

                        handler.postDelayed({
                            printBattleLog(battleLog[i - 1].toString())
                            printAllyStatus(36, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03, true)
                            printEnemyStatus(36, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03, true)
                        }, 500)
                    }

                    6 -> {
                        handler.postDelayed({
                            printBattleLog(battleLog[i - 1].toString())
                            printAllyStatus(45, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03, false)
                            printEnemyStatus(45, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03, false)
                        }, 590)

                        handler.postDelayed({
                            printBattleLog(battleLog[i - 1].toString())
                            printAllyStatus(45, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03, true)
                            printEnemyStatus(45, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03, true)
                        }, 600)
                    }
                }
            }
        }

        //次のターンボタンをターンが終了するまで押せなくする時間　isTurnEnd true:ターンが終了
        if (!isMessageSpeed) {
            when (battleLog.size) {
                1 -> handler.postDelayed({ isTurnEnd = true }, 200)
                2 -> handler.postDelayed({ isTurnEnd = true }, 3500)
                3 -> handler.postDelayed({ isTurnEnd = true }, 6500)
                4 -> handler.postDelayed({ isTurnEnd = true }, 9500)
                5 -> handler.postDelayed({ isTurnEnd = true }, 12500)
                6 -> handler.postDelayed({ isTurnEnd = true }, 15500)
             }

         } else {

            when (battleLog.size) {
                1 -> handler.postDelayed({ isTurnEnd = true }, 100)
                2 -> handler.postDelayed({ isTurnEnd = true }, 200)
                3 -> handler.postDelayed({ isTurnEnd = true }, 300)
                4 -> handler.postDelayed({ isTurnEnd = true }, 400)
                5 -> handler.postDelayed({ isTurnEnd = true }, 500)
                6 -> handler.postDelayed({ isTurnEnd = true }, 600)
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
            isTimes: Boolean
        ) {

            val enemy01Hp = enemy01StatusLog[num + 0]
            val enemy01MaxHp = enemy01StatusLog[num + 1]
            val enemy01Mp = enemy01StatusLog[num + 2]
            val enemy01MaxMp = enemy01StatusLog[num + 3]
            val enemy01Poison = enemy01StatusLog[num + 4]
            val enemy01Paralysis = enemy01StatusLog[num + 5]
            val enemy01PrintStatusEffect = enemy01StatusLog[num + 6]
                enemy01StatusLog[num + 6] = 0.toString()
            val enemy01StatusEffect = enemy01StatusLog[num + 7]
            val enemy01AttackSoundEffect = enemy01StatusLog[num + 8]
            sound(enemy01AttackSoundEffect.toInt())

            if (isTimes){
                sound(enemy01StatusEffect.toInt())
                enemy01StatusLog[num + 7] = 0.toString()
            }

            val enemy02Hp = enemy02StatusLog[num + 0]
            val enemy02MaxHp = enemy02StatusLog[num + 1]
            val enemy02Mp = enemy02StatusLog[num + 2]
            val enemy02MaxMp = enemy02StatusLog[num + 3]
            val enemy02Poison = enemy02StatusLog[num + 4]
            val enemy02Paralysis = enemy02StatusLog[num + 5]
            val enemy02PrintStatusEffect = enemy02StatusLog[num + 6]
                enemy02StatusLog[num + 6] = 0.toString()
            val enemy02StatusEffect = enemy02StatusLog[num + 7]
            val enemy02AttackSoundEffect = enemy02StatusLog[num + 8]
                sound(enemy02AttackSoundEffect.toInt())

            if (isTimes){
                sound(enemy02StatusEffect.toInt())
                enemy02StatusLog[num + 7] = 0.toString()
            }


            val enemy03Hp = enemy03StatusLog[num + 0]
            val enemy03MaxHp = enemy03StatusLog[num + 1]
            val enemy03Mp = enemy03StatusLog[num + 2]
            val enemy03MaxMp = enemy03StatusLog[num + 3]
            val enemy03Poison = enemy03StatusLog[num + 4]
            val enemy03Paralysis = enemy03StatusLog[num + 5]
            val enemy03PrintStatusEffect = enemy03StatusLog[num + 6]
                enemy03StatusLog[num + 6] = 0.toString()
            val enemy03StatusEffect = enemy03StatusLog[num + 7]
            val enemy03AttackSoundEffect = enemy03StatusLog[num + 8]
                sound(enemy03AttackSoundEffect.toInt())

            if (isTimes){
                sound(enemy03StatusEffect.toInt())
                enemy03StatusLog[num + 7] = 0.toString()
            }

            val enemy001 = MemberStatusData(("  %s".format(enemy01.getName())),
                ("%s %s/%s".format("  HP",
                    enemy01Hp,
                    enemy01MaxHp)),
                ("%s %s/%s".format("  MP", enemy01Mp, enemy01MaxMp)),
                ("%s %s".format(
                    enemy01Poison,
                    enemy01Paralysis)),
                (enemy01Hp.toInt()), (enemy01PrintStatusEffect.toInt()), (enemy01StatusEffect.toInt()))

            val enemy002 = MemberStatusData(("  %s".format(enemy02.getName())),
                ("%s %s/%s".format("  HP",
                    enemy02Hp,
                    enemy02MaxHp)),
                ("%s %s/%s".format("  MP", enemy02Mp, enemy02MaxMp)),
                ("%s %s".format(
                    enemy02Poison,
                    enemy02Paralysis)),
                (enemy02Hp.toInt()), (enemy02PrintStatusEffect.toInt()), (enemy02StatusEffect.toInt()))

            val enemy003 = MemberStatusData(("  %s".format(enemy03.getName())),
                ("%s %s/%s".format("  HP",
                    enemy03Hp,
                    enemy03MaxHp)),
                ("%s %s/%s".format("  MP", enemy03Mp, enemy03MaxMp)),
                ("%s %s".format(
                    enemy03Poison,
                    enemy03Paralysis)),
                (enemy03Hp.toInt()), (enemy03PrintStatusEffect.toInt()), (enemy03StatusEffect.toInt()))

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

    private fun printBattleLog(str: String) {
        val tl = findViewById<TextView>(R.id.battle_main_battle_log_text_id)
        tl.text = str
    }

    private fun printAllyStatus(
        num : Int,
        ally01StatusLog: MutableList<String>,
        ally02StatusLog: MutableList<String>,
        ally03StatusLog: MutableList<String>,
        ally01: Player,
        ally02: Player,
        ally03: Player,
        is2Times: Boolean
    ) {

        val ally01Hp = ally01StatusLog[num + 0]
        val ally01MaxHp = ally01StatusLog[num + 1]
        val ally01Mp = ally01StatusLog[num + 2]
        val ally01MaxMp = ally01StatusLog[num + 3]
        val ally01Poison = ally01StatusLog[num + 4]
        val ally01Paralysis = ally01StatusLog[num + 5]
        val ally01PrintStatusEffect = ally01StatusLog[num + 6]
            ally01StatusLog[num + 6] = 0.toString()
        val ally01StatusEffect = ally01StatusLog[num + 7]
        val ally01AttackSoundEffect = ally01StatusLog[num + 8]
            sound(ally01AttackSoundEffect.toInt())

        if (is2Times){
            sound(ally01StatusEffect.toInt())
            ally01StatusLog[num + 7] = 0.toString()
        }

        val ally02Hp = ally02StatusLog[num + 0]
        val ally02MaxHp = ally02StatusLog[num + 1]
        val ally02Mp = ally02StatusLog[num + 2]
        val ally02MaxMp = ally02StatusLog[num + 3]
        val ally02Poison = ally02StatusLog[num + 4]
        val ally02Paralysis = ally02StatusLog[num + 5]
        val ally02PrintStatusEffect = ally02StatusLog[num + 6]
            ally02StatusLog[num + 6] = 0.toString()
        val ally02StatusEffect = ally02StatusLog[num + 7]
        val ally02AttackSoundEffect = ally02StatusLog[num + 8]
            sound(ally02AttackSoundEffect.toInt())

        if (is2Times){
            sound(ally02StatusEffect.toInt())
            ally02StatusLog[num + 7] = 0.toString()
        }

        val ally03Hp = ally03StatusLog[num + 0]
        val ally03MaxHp = ally03StatusLog[num + 1]
        val ally03Mp = ally03StatusLog[num + 2]
        val ally03MaxMp = ally03StatusLog[num + 3]
        val ally03Poison = ally03StatusLog[num + 4]
        val ally03Paralysis = ally03StatusLog[num + 5]
        val ally03PrintStatusEffect = ally03StatusLog[num + 6]
            ally03StatusLog[num + 6] = 0.toString()
        val ally03StatusEffect = ally03StatusLog[num + 7]
        val ally03AttackSoundEffect = ally03StatusLog[num + 8]
            sound(ally03AttackSoundEffect.toInt())

        if (is2Times){
            sound(ally03StatusEffect.toInt())
            ally03StatusLog[num + 7] = 0.toString()
        }

        val ally001 = MemberStatusData(("  %s".format(ally01.getName())),
            ("%s %s/%s".format("  HP",
                ally01Hp,
                ally01MaxHp)),
            ("%s %s/%s".format("  MP", ally01Mp, ally01MaxMp)),
            ("%s %s".format(
                ally01Poison,
                ally01Paralysis)),
            (ally01Hp.toInt()), (ally01PrintStatusEffect.toInt()), (ally01StatusEffect.toInt()))

        val ally002 = MemberStatusData(("  %s".format(ally02.getName())),
            ("%s %s/%s".format("  HP",
                ally02Hp,
                ally02MaxHp)),
            ("%s %s/%s".format("  MP", ally02Mp, ally02MaxMp)),
            ("%s %s".format(
                ally02Poison,
                ally02Paralysis)),
            (ally02Hp.toInt()), (ally02PrintStatusEffect.toInt()), (ally02StatusEffect.toInt()))

        val ally003 = MemberStatusData(("  %s".format(ally03.getName())),
            ("%s %s/%s".format("  HP",
                ally03Hp,
                ally03MaxHp)),
            ("%s %s/%s".format("  MP", ally03Mp, ally03MaxMp)),
            ("%s %s".format(
                ally03Poison,
                ally03Paralysis)),
            (ally03Hp.toInt()),(ally03PrintStatusEffect.toInt()), (ally03StatusEffect.toInt()))

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

        val layoutInflater = layoutInflater
        val customToastView: View = layoutInflater.inflate(R.layout.toast_layout, null)

        (customToastView.findViewById(R.id.toast_layout_imageView_id) as ImageView).setImageResource(character.getCharacterImageType())
        val ts = Toast.makeText(customToastView.context, "", Toast.LENGTH_SHORT)
        ts.setGravity(Gravity.CENTER, 0, 0)
        (customToastView.findViewById(R.id.toast_layout_job_id) as TextView).text = "${character.job}"
        (customToastView.findViewById(R.id.toast_layout_str_id) as TextView).text = "${character.str}"
        (customToastView.findViewById(R.id.toast_layout_def_id) as TextView).text = "${character.def}"
        (customToastView.findViewById(R.id.toast_layout_agi_id) as TextView).text = "${character.agi}"
        (customToastView.findViewById(R.id.toast_layout_luck_id) as TextView).text = "${character.luck}"
        ts.setView(customToastView)
        ts.show()
    }

    private fun sound(sound: Int) {

            when (sound) {

                0 -> {println()}
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
            }
    }

    override fun onDestroy() {
        mp0.release()
        sp0.release()
        super.onDestroy()
    }

    override fun onBackPressed() {}
}




