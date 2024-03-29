package com.e.app_namebattler.view.view.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
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
import com.e.app_namebattler.controller.BattleRecordListener
import com.e.app_namebattler.controller.BattleLogListener
import com.e.app_namebattler.controller.GameManager
import com.e.app_namebattler.model.AllyOpenHelper
import com.e.app_namebattler.model.CharacterData
import com.e.app_namebattler.model.EnemyOpenHelper
import com.e.app_namebattler.view.party.player.CharacterAllData
import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.party.player.job.JobData
import com.e.app_namebattler.view.strategy.StrategyData
import com.e.app_namebattler.view.view.adapter.BattleMainRecyclerAdapter
import com.e.app_namebattler.view.view.adapter.MemberStatusData
import com.e.app_namebattler.view.view.illust.BackGroundData
import com.e.app_namebattler.view.view.message.Comment
import com.e.app_namebattler.view.view.music.MusicData
import com.e.app_namebattler.view.view.music.SoundData
import kotlinx.android.synthetic.main.activity_battle_main.*
import java.util.*
import kotlin.collections.ArrayList

class BattleMainActivity : AppCompatActivity(), View.OnClickListener, BattleLogListener {

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
    private var snd16 = 0

    private var handler = Handler()
    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    private lateinit var allyHelper: AllyOpenHelper
    private lateinit var enemyHelper: EnemyOpenHelper

    private var toast: Toast? = null
    private val gm = GameManager()
    private val bc = BattleRecordListener()

    private var enemyPartyList = ArrayList<CharacterAllData>()
    private var allyPartyList = ArrayList<CharacterAllData>()
    private lateinit var memberList: MutableList<MemberStatusData>

    private var strategyNumber = 0 //作戦番号を格納
    var job = "" // 職業名を格納

    private var isNextTurn: Boolean = true// true: 2ターン目以降　false:バトルログ画面を最初にタップするとtrueになる
    private var isMessageSpeed: Boolean = false// メッセージ速度変更に使用

    private var allyPartyCount = 1
    private var enemyPartyCount = 1

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

        sp0 = SoundPool.Builder().setAudioAttributes(aa0).setMaxStreams(1).build()

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
        snd16 = sp0.load(this, SoundData.S_MISS01.getSound(), 1)

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
        bc.callBack = this

        // コントロールをGameManagerに移譲
        gm.controlTransfer(allyPartyList, enemyPartyList)

        // 作戦変更画面に遷移する処理
        battle_main_strategy_change_button_id.setOnClickListener {
            val intent = Intent(this, StrategyChangeActivity::class.java)
            startActivityForResult(intent, 1000)
        }

        // 次のターンボタンを押したときの処理
        battle_main_next_turn_button_id.setOnClickListener {
          
            if (isNextTurn) {

                handler.postDelayed({

                val charaData = CharacterData.getInstance()
                val attackList = charaData.attackerList

                nextButtonText.text = Comment.M_IN_BATTLE_COMMENT.getComment()
                battle(attackList, strategyNumber)

                }, 100)
            }
        }
    }

    private fun battle(attackList: MutableList<Player>, strategyNumber: Int) {

        isNextTurn = false

        var i = 1

        if (!isMessageSpeed) {
            timer = Timer()

                timerTask = object : TimerTask(){
                    override fun run() {
                        handler.post{

                            if (0 < attackList[i - 1].hp) {
                                gm.battle(attackList[i - 1], strategyNumber)
                                battleEnd()
                                i += 1
                            }

                            if (attackList.size < i) {
                                timer!!.cancel()
                                nextTurnButtonMessage()
                                isNextTurn = true
                            }
                        }
                    }
                }
                timer!!.schedule(timerTask, 100, 2000)

        } else if(isMessageSpeed){

            timer = Timer()

            timerTask = object : TimerTask(){
                override fun run() {
                    handler.post{

                        if (0 < attackList[i - 1].hp) {
                            gm.battle(attackList[i - 1], strategyNumber)
                            battleEnd()
                            i += 1
                        }

                        if (attackList.size < i) {
                            timer!!.cancel()
                            nextTurnButtonMessage()
                            isNextTurn = true
                        }
                    }
                }
            }
            timer!!.schedule(timerTask, 100, 200)
        }
    }

    // 終了判定
    private fun battleEnd() {

        allyPartyCount = gm.getAllyParty().size
        enemyPartyCount = gm.getEnemyParty().size

        if (gm.getAllyParty().isEmpty() || gm.getEnemyParty().isEmpty()) {

            timer!!.cancel()

            val nextButtonText = findViewById<TextView>(R.id.battle_main_next_turn_button_id)
            nextButtonText.text = Comment.M_IN_PROCESS.getComment()

            handler.postDelayed({
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

            }, 1000)
        }
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

        enemyHelper = EnemyOpenHelper(applicationContext)//DB作成

        val db = enemyHelper.readableDatabase

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
    override fun initialStatusData(
        ally01: Player,
        ally02: Player,
        ally03: Player,
        enemy01: Player,
        enemy02: Player,
        enemy03: Player
    ) {
        printCharacterStatus(ally01, ally02, ally03,true)
        printCharacterStatus(enemy01, enemy02, enemy03,true)
    }

    override fun battleLogData(
        battleLog: MutableList<Any>,
        ally01: Player,
        ally02: Player,
        ally03: Player,
        enemy01: Player,
        enemy02: Player,
        enemy03: Player
    ) {

        if (!isMessageSpeed) {

            handler.postDelayed({
                printBattleLog(battleLog[0].toString())
                printCharacterStatus(ally01, ally02, ally03, false)
                printCharacterStatus(enemy01, enemy02, enemy03, false)
            }, 0)

            handler.postDelayed({
                printCharacterStatus(ally01, ally02, ally03, true)
                printCharacterStatus(enemy01, enemy02, enemy03, true)
            }, 500)

        }else{

            handler.postDelayed({
                printBattleLog(battleLog[0].toString())
                printCharacterStatus(ally01, ally02, ally03, false)
                printCharacterStatus(enemy01, enemy02, enemy03, false)
            }, 0)

            handler.postDelayed({
                printCharacterStatus(ally01, ally02, ally03, true)
                printCharacterStatus(enemy01, enemy02, enemy03, true)
            }, 10)
        }
    }
    //使用していない処理
    override fun battleData(
        record: String,
        ally01: Player,
        ally02: Player,
        ally03: Player,
        enemy01: Player,
        enemy02: Player,
        enemy03: Player
    ) {
        printBattleLog(record)
        printCharacterStatus(ally01, ally02, ally03, false)
        printCharacterStatus(enemy01, enemy02, enemy03, false)
    }

    private fun printCharacterStatus(
        character01: Player,
        character02: Player,
        character03: Player,
        isAllySecondTimes: Boolean
    ) {
        val  character001 = getMemberStatusData( character01, isAllySecondTimes)
        val  character002 = getMemberStatusData( character02, isAllySecondTimes)
        val  character003 = getMemberStatusData( character03, isAllySecondTimes)

        memberList = arrayListOf( character001,  character002,  character003)

        if (character01.isMark) {

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
                            0 -> setImageType( character01)
                            1 -> setImageType( character02)
                            2 -> setImageType( character03)
                        }
                    }
                })

        }else{

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
                    override fun onItemClickListener(
                        viw: View,
                        position: Int
                    ) {
                        when (position) {
                            0 -> setImageType( character01)
                            1 -> setImageType( character02)
                            2 -> setImageType( character03)
                        }
                    }
                })
        }
    }

    private fun getMemberStatusData(
        character00: Player,
        isSecondTimes: Boolean
    ) : MemberStatusData {

        var character00PrintStatusEffect = 0
        val character00StatusSoundEffect: Int
        val character00AttackSoundEffect: Int

        val character00Hp = character00.hp                     // HP
        val character00MaxHp = character00.getMaxHp()              // 最大HP
        val character00Mp = character00.mp                    // MP
        val character00MaxMp = character00.getMaxMp()              // 最大MP
        val character00Poison = character00.getPoison()                 // 毒
        val character00Paralysis = character00.getParalysis() // 麻痺

        if (!isMessageSpeed) {// isMessageSpeed false: 効果が表示される true:効果がスキップされる　
            character00PrintStatusEffect = character00.getPrintStatusEffect() // ダメージ、回復の効果
            character00AttackSoundEffect = character00.getAttackSoundEffect()      // 攻撃の効果音
            sound(character00AttackSoundEffect)
            character00.setAttackSoundEffect(0)
        }

        character00.setPrintStatusEffect(0)        // リセット

        // 表示2回目はスキップ
        if (isSecondTimes) {
            character00StatusSoundEffect = character00.getStatusSoundEffect() // 毒、麻痺の効果音
            sound(character00StatusSoundEffect)                             // 効果音の処理
            character00.setStatusSoundEffect(0)
        }

            return MemberStatusData(("  %s".format(character00.getName())),
                ("%s %s/%s".format("  HP", character00Hp, character00MaxHp)),
                ("%s %s/%s".format("  MP", character00Mp, character00MaxMp)),
                ("%s %s".format(character00Poison, character00Paralysis)),
                (character00Hp), (character00PrintStatusEffect))
    }

    private fun nextTurnButtonMessage(){
        val nextButtonText = findViewById<TextView>(R.id.battle_main_next_turn_button_id)
        nextButtonText.text = Comment.M_BATTLE_NEXT_TURN_COMMENT.getComment()
    }

    // バトルログ表示
    private fun printBattleLog(battleLog: String) {
        val textLog = findViewById<TextView>(R.id.battle_main_battle_log_text_id)
        textLog.text = battleLog
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

        (customToastView.findViewById(R.id.toast_layout_imageView_id) as ImageView).setImageResource(character.getCharacterImageType())
        toast = Toast.makeText(customToastView.context, "", Toast.LENGTH_SHORT)
        toast!!.setGravity(Gravity.CENTER, 0, 0)
        (customToastView.findViewById(R.id.toast_layout_job_id) as TextView).text = "${character.job}"
        (customToastView.findViewById(R.id.toast_layout_str_id) as TextView).text = "${character.str}"
        (customToastView.findViewById(R.id.toast_layout_def_id) as TextView).text = "${character.def}"
        (customToastView.findViewById(R.id.toast_layout_agi_id) as TextView).text = "${character.agi}"
        (customToastView.findViewById(R.id.toast_layout_luck_id) as TextView).text = "${character.luck}"
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

            0 -> {
                println()
            }
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
            17 -> sp0.play(snd16, 1.0f, 1.0f, 0, 0, 1.0f)//
        }
    }

    // 背景の設定
    private fun backGround() {

        val backGroundList = ArrayList<BackGroundData>()
        val backGroundView: TextView = findViewById(R.id.battle_main_battle_log_text_id)

        for (bgData in BackGroundData.values()) {
            backGroundList.add(bgData)
        }

        val randomNumberBox = (1..backGroundList.size).random() - 1
        val backGroundValue = backGroundList[randomNumberBox].getBackGround()
        val battleLogTextColor = backGroundList[randomNumberBox].getTextColor()
        backGroundView.setBackgroundResource(backGroundValue)
        backGroundView.setTextColor(Color.parseColor(battleLogTextColor))
    }

    override fun onDestroy() {
        mp0.release()
        sp0.release()
        super.onDestroy()
    }

    //此処の処理は一旦保留：スリープ解除後効果音が出なくなるため
//    override fun onPause() {
//        mp0.pause()
//        sp0.release()
//        super.onPause()
//    }
//
//    override fun onResume() {
//        mp0.start()
//        super.onResume()
//    }

    override fun onBackPressed() {}
}

