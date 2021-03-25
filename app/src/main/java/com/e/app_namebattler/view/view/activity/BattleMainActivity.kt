package com.e.app_namebattler.view.view.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
import com.e.app_namebattler.view.strategy.StrategyName
import com.e.app_namebattler.view.view.adapter.BattleMainRecyclerAdapter
import com.e.app_namebattler.view.view.adapter.MemberStatusData
import com.e.app_namebattler.view.view.music.MusicData
import kotlinx.android.synthetic.main.activity_battle_main.*


class BattleMainActivity : AppCompatActivity(), View.OnClickListener, BattleLogListener {

    lateinit var mp0: MediaPlayer
    //private var handler: Handler? = null
    private var handler = Handler()
    private lateinit var helper: AllyOpenHelper
    private lateinit var helper02: EnemyOpenHelper

    private val gm = GameManager()

    private var enemyPartyList = ArrayList<CharacterAllData>()
    private var allyPartyList = ArrayList<CharacterAllData>()
    private lateinit var memberList: MutableList<MemberStatusData>

    private var ally01StatusList:MutableList<String> = ArrayList()
    private var ally02StatusList:MutableList<String> = ArrayList()
    private var ally03StatusList:MutableList<String> = ArrayList()
    private var enemy01StatusList:MutableList<String> = ArrayList()
    private var enemy02StatusList:MutableList<String> = ArrayList()
    private var enemy03StatusList:MutableList<String> = ArrayList()

    private var strategyNumber = 0 //作戦番号を格納
    var job = "" // 職業名を格納

    private var isNextTurn:Boolean = false// true: 最初のターンが実行される　false:バトルログ画面を最初にタップするとtrueになる
    private var isMessageSpeed:Boolean = false// メッセージ速度変更に使用
    private var isTurnEnd:Boolean = false

    var party01Count = 1
    var party02Count = 1

    @SuppressLint("CutPasteId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle_main)

        mp0= MediaPlayer.create(this, MusicData.BGM01.getBgm())
        mp0.isLooping=true
        mp0.start()

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

        gm.myCallBack = this// GameManagerクラスからBattleLogListenerを経てデータを戻す準備

        // コントロールをGameManagerに移譲
        gm.controlTransfer(allyPartyList, enemyPartyList)

        // 作戦変更画面に遷移する処理
        battle_main_strategy_change_button_id.setOnClickListener {
            val intent = Intent(this, StrategyChangeActivity::class.java)
            startActivityForResult(intent, 1000)
        }

        // 次のターンボタンを押したときの処理
        battle_main_next_turn_button_id.setOnClickListener {

            if (isNextTurn && isTurnEnd){
                if (isNextTurn) {

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
                        turnEnd()
                    }
                }
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode != 1000) { return }

        val sn = findViewById<TextView>(R.id.battle_main_strategy_text_id)

        if (resultCode == Activity.RESULT_OK && data != null){

            var message = ""

            strategyNumber = data.getIntExtra("strategy_key",0)

            when (strategyNumber) {

                0 -> message = StrategyName.S0.getStrategyName()
                1 ->  message = StrategyName.S1.getStrategyName()
                2 ->  message = StrategyName.S2.getStrategyName()
                3 ->  message = StrategyName.S3.getStrategyName()
                4 ->  message = StrategyName.S4.getStrategyName()
            }
            sn.text = message

        } else if(resultCode == Activity.RESULT_CANCELED){

            sn.text = StrategyName.S0.getStrategyName()
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
        strategyText.text =  StrategyName.S0.getStrategyName()
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

    // GameManagerクラスからBattleLogListener経て呼ばれる
    override fun upDateAllyStatus(ally01: Player, ally02: Player, ally03: Player){

        ally01StatusList.plusAssign(ally01.getHP().toString())
        ally01StatusList.plusAssign(ally01.getMaxHp().toString())
        ally01StatusList.plusAssign(ally01.getMP().toString())
        ally01StatusList.plusAssign(ally01.getMaxMp().toString())
        ally01StatusList.plusAssign(ally01.getPoison())
        ally01StatusList.plusAssign(ally01.getParalysis())

        ally02StatusList.plusAssign(ally02.getHP().toString())
        ally02StatusList.plusAssign(ally02.getMaxHp().toString())
        ally02StatusList.plusAssign(ally02.getMP().toString())
        ally02StatusList.plusAssign(ally02.getMaxMp().toString())
        ally02StatusList.plusAssign(ally02.getPoison())
        ally02StatusList.plusAssign(ally02.getParalysis())

        ally03StatusList.plusAssign(ally03.getHP().toString())
        ally03StatusList.plusAssign(ally03.getMaxHp().toString())
        ally03StatusList.plusAssign(ally03.getMP().toString())
        ally03StatusList.plusAssign(ally03.getMaxMp().toString())
        ally03StatusList.plusAssign(ally03.getPoison())
        ally03StatusList.plusAssign(ally03.getParalysis())

        printAllyStatus(0, ally01StatusList, ally02StatusList, ally03StatusList, ally01, ally02, ally03)
    }

    // GameManagerクラスからBattleLogListener経て呼ばれる
    override fun upDateEnemyStatus(enemy01: Player, enemy02: Player, enemy03: Player){

        enemy01StatusList.plusAssign(enemy01.getHP().toString())
        enemy01StatusList.plusAssign(enemy01.getMaxHp().toString())
        enemy01StatusList.plusAssign(enemy01.getMP().toString())
        enemy01StatusList.plusAssign(enemy01.getMaxMp().toString())
        enemy01StatusList.plusAssign(enemy01.getPoison())
        enemy01StatusList.plusAssign(enemy01.getParalysis())

        enemy02StatusList.plusAssign(enemy02.getHP().toString())
        enemy02StatusList.plusAssign(enemy02.getMaxHp().toString())
        enemy02StatusList.plusAssign(enemy02.getMP().toString())
        enemy02StatusList.plusAssign(enemy02.getMaxMp().toString())
        enemy02StatusList.plusAssign(enemy02.getPoison())
        enemy02StatusList.plusAssign(enemy02.getParalysis())

        enemy03StatusList.plusAssign(enemy03.getHP().toString())
        enemy03StatusList.plusAssign(enemy03.getMaxHp().toString())
        enemy03StatusList.plusAssign(enemy03.getMP().toString())
        enemy03StatusList.plusAssign(enemy03.getMaxMp().toString())
        enemy03StatusList.plusAssign(enemy03.getPoison())
        enemy03StatusList.plusAssign(enemy03.getParalysis())

        printEnemyStatus(0, enemy01StatusList, enemy02StatusList, enemy03StatusList, enemy01, enemy02, enemy03)
    }

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

        isTurnEnd = false

        if (!isMessageSpeed) {

            for (i in 1..battleLog.size) {
                when (i) {
                    1 -> {
                            handler.postDelayed({
                                printBattleLog(battleLog[i - 1].toString())
                                printAllyStatus(0, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03)
                                printEnemyStatus(0, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03)
                            }, 200)
                    }

                    2 -> {
                            handler.postDelayed({
                                printBattleLog(battleLog[i - 1].toString())
                                printAllyStatus(6, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03)
                                printEnemyStatus(6, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03)
                            }, 3500)
                    }

                    3 -> {
                            handler.postDelayed({
                                printBattleLog(battleLog[i - 1].toString())
                                printAllyStatus(12, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03)
                                printEnemyStatus(12, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03)
                            }, 6500)
                    }

                    4 -> {
                            handler.postDelayed({
                                printBattleLog(battleLog[i - 1].toString())
                                printAllyStatus(18, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03)
                                printEnemyStatus(18, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03)
                            }, 9500)
                    }

                    5 -> {
                            handler.postDelayed({
                                printBattleLog(battleLog[i - 1].toString())
                                printAllyStatus(24, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03)
                                printEnemyStatus(24, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03)
                            }, 12500)
                    }

                    6 -> {
                            handler.postDelayed({
                                printBattleLog(battleLog[i - 1].toString())
                                printAllyStatus(30, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03)
                                printEnemyStatus(30, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03)
                            }, 15500)
                    }
                }
            }
        } else {

            for (i in 1..battleLog.size) {
                when (i) {
                    1 -> {
                            handler.postDelayed({
                                printBattleLog(battleLog[i - 1].toString())
                                printAllyStatus(0, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03)
                                printEnemyStatus(0, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03)
                            }, 100)
                    }

                    2 -> {
                            handler.postDelayed({
                                printBattleLog(battleLog[i - 1].toString())
                                printAllyStatus(6, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03)
                                printEnemyStatus(6, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03)
                            }, 200)
                    }

                    3 -> {
                            handler.postDelayed({
                                printBattleLog(battleLog[i - 1].toString())
                                printAllyStatus(12, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03)
                                printEnemyStatus(12, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03)
                            }, 300)
                    }

                    4 -> {
                            handler.postDelayed({
                                printBattleLog(battleLog[i - 1].toString())
                                printAllyStatus(18, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03)
                                printEnemyStatus(18, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03)
                            }, 400)
                    }

                    5 -> {
                            handler.postDelayed({
                                printBattleLog(battleLog[i - 1].toString())
                                printAllyStatus(24, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03)
                                printEnemyStatus(24, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03)
                            }, 500)
                    }

                    6 -> {
                        handler.postDelayed({
                            printBattleLog(battleLog[i - 1].toString())
                            printAllyStatus(30, ally01StatusLog, ally02StatusLog, ally03StatusLog, ally01, ally02, ally03)
                            printEnemyStatus(30, enemy01StatusLog, enemy02StatusLog, enemy03StatusLog, enemy01, enemy02, enemy03)
                        }, 600)
                    }
                }
            }
        }

//        if (!isMessageSpeed) {
//            when (battleLog.size) {
//                1 -> handler.postDelayed({ isTurnEnd = true }, 200)
//                2 -> handler.postDelayed({ isTurnEnd = true }, 3500)
//                3 -> handler.postDelayed({ isTurnEnd = true }, 6500)
//                4 -> handler.postDelayed({ isTurnEnd = true }, 9500)
//                5 -> handler.postDelayed({ isTurnEnd = true }, 12500)
//                6 -> handler.postDelayed({ isTurnEnd = true }, 15500)
//            }
//           // isTurnEnd = true
//
//        } else {
//            when (battleLog.size) {
//                1 -> handler.postDelayed({ isTurnEnd = true }, 100)
//                2 -> handler.postDelayed({ isTurnEnd = true }, 200)
//                3 -> handler.postDelayed({ isTurnEnd = true }, 300)
//                4 -> handler.postDelayed({ isTurnEnd = true }, 400)
//                5 -> handler.postDelayed({ isTurnEnd = true }, 500)
//                6 -> handler.postDelayed({ isTurnEnd = true }, 600)
//            }
          //  isTurnEnd = true
      //  }
        isTurnEnd = true
    }
        private fun printEnemyStatus(
            num: Int,
            enemy01StatusLog: MutableList<String>,
            enemy02StatusLog: MutableList<String>,
            enemy03StatusLog: MutableList<String>,
            enemy01: Player,
            enemy02: Player,
            enemy03: Player
        ) {

            val enemy01Hp = enemy01StatusLog[num + 0]
            val enemy01MaxHp = enemy01StatusLog[num + 1]
            val enemy01Mp = enemy01StatusLog[num + 2]
            val enemy01MaxMp = enemy01StatusLog[num + 3]
            val enemy01Poison = enemy01StatusLog[num + 4]
            val enemy01Paralysis = enemy01StatusLog[num + 5]

            val enemy02Hp = enemy02StatusLog[num + 0]
            val enemy02MaxHp = enemy02StatusLog[num + 1]
            val enemy02Mp = enemy02StatusLog[num + 2]
            val enemy02MaxMp = enemy02StatusLog[num + 3]
            val enemy02Poison = enemy02StatusLog[num + 4]
            val enemy02Paralysis = enemy02StatusLog[num + 5]

            val enemy03Hp = enemy03StatusLog[num + 0]
            val enemy03MaxHp = enemy03StatusLog[num + 1]
            val enemy03Mp = enemy03StatusLog[num + 2]
            val enemy03MaxMp = enemy03StatusLog[num + 3]
            val enemy03Poison = enemy03StatusLog[num + 4]
            val enemy03Paralysis = enemy03StatusLog[num + 5]

            val enemy001 = MemberStatusData(("  %s".format(enemy01.getName())),
                ("%s %s/%s".format("  HP",
                    enemy01Hp,
                    enemy01MaxHp)),
                ("%s %s/%s".format("  MP", enemy01Mp, enemy01MaxMp)),
                ("%s %s".format(
                    enemy01Poison,
                    enemy01Paralysis)),
                (enemy01.hp))
            val enemy002 = MemberStatusData(("  %s".format(enemy02.getName())),
                ("%s %s/%s".format("  HP",
                    enemy02Hp,
                    enemy02MaxHp)),
                ("%s %s/%s".format("  MP", enemy02Mp, enemy02MaxMp)),
                ("%s %s".format(
                    enemy02Poison,
                    enemy02Paralysis)),
                (enemy02.hp))
            val enemy003 = MemberStatusData(("  %s".format(enemy03.getName())),
                ("%s %s/%s".format("  HP",
                    enemy03Hp,
                    enemy03MaxHp)),
                ("%s %s/%s".format("  MP", enemy03Mp, enemy03MaxMp)),
                ("%s %s".format(
                    enemy03Poison,
                    enemy03Paralysis)),
                (enemy03.hp))

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
        ally03: Player

    ) {

        val ally01Hp = ally01StatusLog[num]
        val ally01MaxHp = ally01StatusLog[num + 1]
        val ally01Mp = ally01StatusLog[num + 2]
        val ally01MaxMp = ally01StatusLog[num + 3]
        val ally01Poison = ally01StatusLog[num + 4]
        val ally01Paralysis = ally01StatusLog[num + 5]

        val ally02Hp = ally02StatusLog[num]
        val ally02MaxHp = ally02StatusLog[num + 1]
        val ally02Mp = ally02StatusLog[num + 2]
        val ally02MaxMp = ally02StatusLog[num + 3]
        val ally02Poison = ally02StatusLog[num + 4]
        val ally02Paralysis = ally02StatusLog[num + 5]

        val ally03Hp = ally03StatusLog[num]
        val ally03MaxHp = ally03StatusLog[num + 1]
        val ally03Mp = ally03StatusLog[num + 2]
        val ally03MaxMp = ally03StatusLog[num + 3]
        val ally03Poison = ally03StatusLog[num + 4]
        val ally03Paralysis = ally03StatusLog[num + 5]

        val ally001 = MemberStatusData(("  %s".format(ally01.getName())),
            ("%s %s/%s".format("  HP",
                ally01Hp,
                ally01MaxHp)),
            ("%s %s/%s".format("  MP", ally01Mp, ally01MaxMp)),
            ("%s %s".format(
                ally01Poison,
                ally01Paralysis)),
            (ally01.hp))

        val ally002 = MemberStatusData(("  %s".format(ally02.getName())),
            ("%s %s/%s".format("  HP",
                ally02Hp,
                ally02MaxHp)),
            ("%s %s/%s".format("  MP", ally02Mp, ally02MaxMp)),
            ("%s %s".format(
                ally02Poison,
                ally02Paralysis)),
            (ally02.hp))

        val ally003 = MemberStatusData(("  %s".format(ally03.getName())),
            ("%s %s/%s".format("  HP",
                ally03Hp,
                ally03MaxHp)),
            ("%s %s/%s".format("  MP", ally03Mp, ally03MaxMp)),
            ("%s %s".format(
                ally03Poison,
                ally03Paralysis)),
            (ally03.hp))

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

    override fun onDestroy() {
        mp0.release()
        super.onDestroy()
    }

    override fun onBackPressed() {}
}




