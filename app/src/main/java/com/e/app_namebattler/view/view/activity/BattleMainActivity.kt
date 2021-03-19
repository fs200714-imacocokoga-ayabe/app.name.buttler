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
import com.e.app_namebattler.view.party.party.Party
import com.e.app_namebattler.view.party.player.CharacterAllData
import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.strategy.StrategyName
import com.e.app_namebattler.view.view.adapter.BattleMainRecyclerAdapter
import com.e.app_namebattler.view.view.adapter.MemberStatusData
import com.e.app_namebattler.view.view.music.MusicData
import kotlinx.android.synthetic.main.activity_battle_main.*


class BattleMainActivity : AppCompatActivity(), View.OnClickListener, BattleLogListener {

    lateinit var mp0: MediaPlayer
    private var handler: Handler? = null
    private lateinit var helper: AllyOpenHelper
    private lateinit var helper02: EnemyOpenHelper

    private val gm = GameManager()

    private var enemyPartyList = ArrayList<CharacterAllData>()
    private var allyPartyList = ArrayList<CharacterAllData>()
    private lateinit var memberList: MutableList<MemberStatusData>
    private var attackList: MutableList<Player> = ArrayList() //攻撃するキャラクターを格納

    private var strategyNumber = 0 //作戦番号を格納
    var job = "" // 職業名を格納

    private lateinit var player: Player // キャラクターを格納

    private var isTurn:Boolean = false// true: 最初のターンが実行される　false:バトルログ画面を最初にタップするとtrueになる
    private var isMessageSpeed:Boolean = false// メッセージ速度変更に使用

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

            if (isTurn){

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
                }else{

                    battle(strategyNumber)
                }
            }
        }
    }

    private fun battle(strategyNumber: Int) {

        attackList.clear()

        // 行動するキャラクターattackListに格納
        for (i in 1..gm.getMembers().size) {
            attackList.add(gm.getMembers()[i - 1])
        }

        gm.setAttackList(attackList)

        for (i in 1..attackList.size) { // attackに格納したplayerが全員行動する
            party01Count = gm.getParty01().size
            party02Count = gm.getParty02().size

            player = attackList[i - 1]

            if (party01Count != 0 || party02Count != 0 && player.isLive) {

                if (!isMessageSpeed) {

                    when (i) {
                        1 -> {handler?.postDelayed({ gm.battle(1, strategyNumber) }, 200)}
                        2 -> {handler?.postDelayed({ gm.battle(2, strategyNumber) }, 3500)}
                        3 -> {handler?.postDelayed({ gm.battle(3, strategyNumber) }, 6500)}
                        4 -> {handler?.postDelayed({ gm.battle(4, strategyNumber) }, 9500)}
                        5 -> {handler?.postDelayed({ gm.battle(5, strategyNumber) }, 12500)}
                        6 -> {handler?.postDelayed({ gm.battle(6, strategyNumber) }, 15500)}
                    }

                } else {

                    when (i) {
                        1 -> {handler?.postDelayed({ gm.battle(1, strategyNumber) }, 100)}
                        2 -> {handler?.postDelayed({ gm.battle(2, strategyNumber) }, 200)}
                        3 -> {handler?.postDelayed({ gm.battle(3, strategyNumber) }, 300)}
                        4 -> {handler?.postDelayed({ gm.battle(4, strategyNumber) }, 400)}
                        5 -> {handler?.postDelayed({ gm.battle(5, strategyNumber) }, 500)}
                        6 -> {handler?.postDelayed({ gm.battle(6, strategyNumber) }, 600)}
                    }
                }
            }
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

    override fun upDateBattleLog(battleLog: StringBuilder){

            val tl = findViewById<TextView>(R.id.battle_main_battle_log_text_id)
            tl.text = battleLog.toString()
    }

    // バトルログ画面でのタップした時の処理
    override fun onClick(v: View?) {

        if (!isTurn){

            battle(strategyNumber)

            isTurn = true

            val ms00 = Toast.makeText(this, "タップで次のターンの速度を変更できます", Toast.LENGTH_SHORT)
            ms00.setGravity(Gravity.CENTER, 0, 0)
            ms00.show()

        }else {

            isMessageSpeed = !isMessageSpeed // 反転

            if (isMessageSpeed) {

                val ms01 = Toast.makeText(this, "次のターンのメッセージ速度：早い", Toast.LENGTH_SHORT)
                ms01.setGravity(Gravity.CENTER, 0, 0)
                ms01.show()

            } else {

                val ms02 = Toast.makeText(this, "次のターンのメッセージ速度：遅い", Toast.LENGTH_SHORT)
                ms02.setGravity(Gravity.CENTER, 0, 0)
                ms02.show()
            }
        }
    }

    // GameManagerクラスからBattleLogListener経て呼ばれる
    override fun upDateAllyStatus(ally01: Player, ally02: Player, ally03: Player){

        val ally001 = MemberStatusData(("  %s".format(ally01.getName())),
            ("%s %d/%d".format("  HP",
                ally01.hp,
                ally01.getMaxHp())),
            ("%s %d/%d".format("  MP", ally01.mp, ally01.getMaxMp())),
            ("%s %s".format(
                ally01.getPoison(),
                ally01.getParalysis())),
            (ally01.hp))
        val ally002 = MemberStatusData(("  %s".format(ally02.getName())),
            ("%s %d/%d".format("  HP",
                ally02.hp,
                ally02.getMaxHp())),
            ("%s %d/%d".format("  MP", ally02.mp, ally02.getMaxMp())),
            ("%s %s".format(
                ally02.getPoison(),
                ally02.getParalysis())),
            (ally02.hp))
        val ally003 = MemberStatusData(("  %s".format(ally03.getName())),
            ("%s %d/%d".format("  HP",
                ally03.hp,
                ally03.getMaxHp())),
            ("%s %d/%d".format("  MP", ally03.mp, ally03.getMaxMp())),
            ("%s %s".format(
                ally03.getPoison(),
                ally03.getParalysis())),
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

    // GameManagerクラスからBattleLogListener経て呼ばれる
    override fun upDateEnemyStatus(enemy01: Player, enemy02: Player, enemy03: Player){

        val enemy001 = MemberStatusData(("  %s".format(enemy01.getName())),
            ("%s %d/%d".format("  HP",
                enemy01.hp,
                enemy01.getMaxHp())),
            ("%s %d/%d".format("  MP", enemy01.mp, enemy01.getMaxMp())),
            ("%s %s".format(
                enemy01.getPoison(),
                enemy01.getParalysis())),
            (enemy01.hp))
        val enemy002 = MemberStatusData(("  %s".format(enemy02.getName())),
            ("%s %d/%d".format("  HP",
                enemy02.hp,
                enemy02.getMaxHp())),
            ("%s %d/%d".format("  MP", enemy02.mp, enemy02.getMaxMp())),
            ("%s %s".format(
                enemy02.getPoison(),
                enemy02.getParalysis())),
            (enemy02.hp))
        val enemy003 = MemberStatusData(("  %s".format(enemy03.getName())),
            ("%s %d/%d".format("  HP",
                enemy03.hp,
                enemy03.getMaxHp())),
            ("%s %d/%d".format("  MP", enemy03.mp, enemy03.getMaxMp())),
            ("%s %s".format(
                enemy03.getPoison(),
                enemy03.getParalysis())),
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


