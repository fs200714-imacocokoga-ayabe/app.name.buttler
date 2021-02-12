package com.e.app_namebattler

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
import kotlinx.android.synthetic.main.activity_battle_main.*


class BattleMainActivity : AppCompatActivity(), View.OnClickListener, BattleLogListener {

    lateinit var mp0: MediaPlayer
    var handler: Handler? = null
    lateinit var helper: MyOpenHelper
    private val gm = GameManager()

    private var enemyPartyList = ArrayList<CharacterAllData>()
    private var allyPartyList = ArrayList<CharacterAllData>()
    lateinit var memberList: MutableList<MemberStatusData>

    var strategyNumber = 0
    var job = ""

    private var isTurn:Boolean = false
    var isMessageSpeed:Boolean = false

    @SuppressLint("CutPasteId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle_main)

        mp0= MediaPlayer.create(this, R.raw.lastwar)
        mp0.isLooping=true
        mp0.start()

        this.handler = Handler()

        val battleMainLogTextDialog =
            findViewById<View>(R.id.battle_main_battle_log_text_id) as TextView

        battleMainLogTextDialog.setOnClickListener(this)

        val allyName01 = intent.getStringExtra("name01_key")
        val allyName02 = intent.getStringExtra("name02_key")
        val allyName03 = intent.getStringExtra("name03_key")
        val enemyName01 = intent.getStringExtra("enemyName01_key")
        val enemyName02 = intent.getStringExtra("enemyName02_key")
        val enemyName03 = intent.getStringExtra("enemyName03_key")
        val strategyName = intent.getStringExtra("strategy_key")

            if (strategyName != null) {

                printStrategy(strategyName)

            } else {

                printStrategy("武器でたたかおう")
            }

        // タップすると始まります
        val bl = findViewById<TextView>(R.id.battle_main_battle_log_text_id)
        bl.text = "Tap to start"

        // 敵のデータを名前から取得する
        enemyPartyList = getEnemyData(enemyName01, enemyName02, enemyName03)
        // 味方のデータを名前から取得する
        allyPartyList = getAllyData(allyName01, allyName02, allyName03)

        gm.myCallBack = this

        // コントロールをGameManagerに移譲
        gm.controlTransfer(allyPartyList, enemyPartyList)

        // 作戦変更画面に遷移
        battle_main_strategy_change_button_id.setOnClickListener {
            val intent = Intent(this, StrategyChangeActivity::class.java)
            startActivityForResult(intent, 1000)
        }

        // 次のターンボタンを押したときの処理
        battle_main_next_turn_button_id.setOnClickListener {

            if (isTurn){

                if (gm.getParty01().isEmpty() || gm.getParty02().isEmpty()) {
                    // GameManagerクラス から　CharacterDataクラスにデータを渡す処理
                    gm.sendData()

                    // パーティの勝ち負け判定に使用
                    val party00 = gm.getParty01().size

                    val intent = Intent(this, BattleResultActivity::class.java)

                    intent.putExtra("name_key01", allyName01)
                    intent.putExtra("name_key02", allyName02)
                    intent.putExtra("name_key03", allyName03)
                    intent.putExtra("name_key04", enemyName01)
                    intent.putExtra("name_key05", enemyName02)
                    intent.putExtra("name_key06", enemyName03)
                    intent.putExtra("party_key", party00)

                    mp0.reset()
                    startActivity(intent)

                }else{

                    gm.battle(strategyNumber)

                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode != 1000) { return }

        val sn = findViewById<TextView>(R.id.battle_main_strategy_text_id)

        if (resultCode == Activity.RESULT_OK && data != null){

            val message = data.getStringExtra("strategy_key")

            when (message) {
                "武器でたたかおう" -> {
                    strategyNumber = 0
                }
                "攻撃魔法をつかおう" -> {
                    strategyNumber = 1
                }
                "スキルをつかおう" -> {
                    strategyNumber = 2
                }
                "回復魔法をつかおう" -> {
                    strategyNumber = 3
                }
                "薬草をつかおう" -> {
                    strategyNumber = 4
                }
            }
            sn.text = message

        } else if(resultCode == Activity.RESULT_CANCELED){

            sn.text = "武器でたたかおう"
        }
    }

    // 名前を受け取りデータベースから敵キャラクターのステータスを取得する
    private fun getEnemyData(
        enemyName01: String?,
        enemyName02: String?,
        enemyName03: String?
    ): ArrayList<CharacterAllData> {

        helper = MyOpenHelper(applicationContext)//DB作成

        val db = helper.readableDatabase

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
                        c.getString(8)
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

        helper = MyOpenHelper(applicationContext)//DB作成

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
                        c.getString(8)
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

            0 -> job = "戦士"
            1 -> job = "魔法使い"
            2 -> job = "僧侶"
            3 -> job = "忍者"
        }
        return job
    }

    // 作戦の表示
    private fun printStrategy(strategyName: String) {

        val strategyText: TextView = findViewById(R.id.battle_main_strategy_text_id)
        strategyText.text = strategyName
    }

    override fun upDateBattleLog(battleLog: List<String>){

        if (!isMessageSpeed) {
            println("------------------------------------------------------------------------------")
            val tl = findViewById<TextView>(R.id.battle_main_battle_log_text_id)

            for (i in 1..battleLog.size) {
                when (i) {
                    1 -> {
                        handler?.postDelayed({ tl.text = battleLog[i - 1] }, 200)
                        println("ろぐ01${battleLog[i - 1]}")
                    }

                    2 -> {
                        handler?.postDelayed({ tl.text = battleLog[i - 1] }, 3500)
                        println("ろぐ02${battleLog[i - 1]}")
                    }

                    3 -> {
                        handler?.postDelayed({ tl.text = battleLog[i - 1] }, 6500)
                        println("ろぐ03${battleLog[i - 1]}")
                    }

                    4 -> {
                        handler?.postDelayed({ tl.text = battleLog[i - 1] }, 9500)
                        println("ろぐ04${battleLog[i - 1]}")
                    }

                    5 -> {
                        handler?.postDelayed({ tl.text = battleLog[i - 1] }, 12500)
                        println("ろぐ05${battleLog[i - 1]}")
                    }

                    6 -> {
                        handler?.postDelayed({ tl.text = battleLog[i - 1] }, 15500)
                        println("ろぐ06${battleLog[i - 1]}")
                    }
                }
            }
        }else{
println("------------------------------------------------------------------------------")
            val tl = findViewById<TextView>(R.id.battle_main_battle_log_text_id)

            for (i in 1..battleLog.size) {
                when (i) {
                    1 -> {
                        handler?.postDelayed({ tl.text = battleLog[i - 1] }, 100)
                        println("ろぐ01${battleLog[i - 1]}")
                    }

                    2 -> {
                        handler?.postDelayed({ tl.text = battleLog[i - 1] }, 200)
                        println("ろぐ02${battleLog[i - 1]}")
                    }

                    3 -> {
                        handler?.postDelayed({ tl.text = battleLog[i - 1] }, 300)
                        println("ろぐ03${battleLog[i - 1]}")
                    }

                    4 -> {
                        handler?.postDelayed({ tl.text = battleLog[i - 1] }, 400)
                        println("ろぐ04${battleLog[i - 1]}")
                    }

                    5 -> {
                        handler?.postDelayed({ tl.text = battleLog[i - 1] }, 500)
                        println("ろぐ05${battleLog[i - 1]}")
                    }

                    6 -> {
                        handler?.postDelayed({ tl.text = battleLog[i - 1] }, 600)
                        println("ろぐ06${battleLog[i - 1]}")
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {

        if (!isTurn){

            gm.battle(strategyNumber)

            isTurn = true
            val ms00 = Toast.makeText(this, "タップで次のターンの速度を変更できます", Toast.LENGTH_SHORT)
            ms00.setGravity(Gravity.CENTER, 0, 0)
            ms00.show()
        }else {
            isMessageSpeed = !isMessageSpeed

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
                        0 -> setAppearance(ally01)
                        1 -> setAppearance(ally02)
                        2 -> setAppearance(ally03)
                    }
                }
            })
    }

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
                        0 -> setAppearance(enemy01)
                        1 -> setAppearance(enemy02)
                        2 -> setAppearance(enemy03)
                    }
                }
            })
    }

    @SuppressLint("ShowToast", "InflateParams")
    private fun setAppearance(characterAppearance: Player) {

        val layoutInflater = layoutInflater
        val customToastView: View = layoutInflater.inflate(R.layout.toast_layout, null)

        when(characterAppearance.getCharacterAppearance()){

            0 -> (customToastView.findViewById(R.id.toast_layout_imageView_id) as ImageView).setImageResource(R.drawable.fighter_ally01)
            1 -> (customToastView.findViewById(R.id.toast_layout_imageView_id) as ImageView).setImageResource(R.drawable.fighter_ally02)
            2 -> (customToastView.findViewById(R.id.toast_layout_imageView_id) as ImageView).setImageResource(R.drawable.fighter_ally03)
            3 -> (customToastView.findViewById(R.id.toast_layout_imageView_id) as ImageView).setImageResource(R.drawable.fighter_ally04)
            4 -> (customToastView.findViewById(R.id.toast_layout_imageView_id) as ImageView).setImageResource(R.drawable.wizard_ally01)
            5 -> (customToastView.findViewById(R.id.toast_layout_imageView_id) as ImageView).setImageResource(R.drawable.wizard_ally02)
            6 -> (customToastView.findViewById(R.id.toast_layout_imageView_id) as ImageView).setImageResource(R.drawable.wizard_ally03)
            7 -> (customToastView.findViewById(R.id.toast_layout_imageView_id) as ImageView).setImageResource(R.drawable.wizard_ally04)
            8 -> (customToastView.findViewById(R.id.toast_layout_imageView_id) as ImageView).setImageResource(R.drawable.priest_ally01)
            9 -> (customToastView.findViewById(R.id.toast_layout_imageView_id) as ImageView).setImageResource(R.drawable.priest_ally02)
            10 -> (customToastView.findViewById(R.id.toast_layout_imageView_id) as ImageView).setImageResource(R.drawable.priest_ally03)
            11 -> (customToastView.findViewById(R.id.toast_layout_imageView_id) as ImageView).setImageResource(R.drawable.priest_ally04)
            12 -> (customToastView.findViewById(R.id.toast_layout_imageView_id) as ImageView).setImageResource(R.drawable.ninja_ally01)
            13 -> (customToastView.findViewById(R.id.toast_layout_imageView_id) as ImageView).setImageResource(R.drawable.ninja_ally02)
            14 -> (customToastView.findViewById(R.id.toast_layout_imageView_id) as ImageView).setImageResource(R.drawable.ninja_ally03)
            15 -> (customToastView.findViewById(R.id.toast_layout_imageView_id) as ImageView).setImageResource(R.drawable.fighter_enemy01)
            16 -> (customToastView.findViewById(R.id.toast_layout_imageView_id) as ImageView).setImageResource(R.drawable.fighter_enemy02)
            17 -> (customToastView.findViewById(R.id.toast_layout_imageView_id) as ImageView).setImageResource(R.drawable.fighter_enemy03)
            18 -> (customToastView.findViewById(R.id.toast_layout_imageView_id) as ImageView).setImageResource(R.drawable.fighter_enemy04)
            19 -> (customToastView.findViewById(R.id.toast_layout_imageView_id) as ImageView).setImageResource(R.drawable.wizard_enemy01)
            20 -> (customToastView.findViewById(R.id.toast_layout_imageView_id) as ImageView).setImageResource(R.drawable.wizard_enemy02)
            21 -> (customToastView.findViewById(R.id.toast_layout_imageView_id) as ImageView).setImageResource(R.drawable.wizard_enemy03)
            22 -> (customToastView.findViewById(R.id.toast_layout_imageView_id) as ImageView).setImageResource(R.drawable.wizard_enemy04)
            23 -> (customToastView.findViewById(R.id.toast_layout_imageView_id) as ImageView).setImageResource(R.drawable.priest_enemy01)
            24 -> (customToastView.findViewById(R.id.toast_layout_imageView_id) as ImageView).setImageResource(R.drawable.priest_enemy02)
            25 -> (customToastView.findViewById(R.id.toast_layout_imageView_id) as ImageView).setImageResource(R.drawable.priest_enemy03)
            26 -> (customToastView.findViewById(R.id.toast_layout_imageView_id) as ImageView).setImageResource(R.drawable.priest_enemy04)
            27 -> (customToastView.findViewById(R.id.toast_layout_imageView_id) as ImageView).setImageResource(R.drawable.ninja_enemy01)
            28 -> (customToastView.findViewById(R.id.toast_layout_imageView_id) as ImageView).setImageResource(R.drawable.ninja_enemy02)
            29 -> (customToastView.findViewById(R.id.toast_layout_imageView_id) as ImageView).setImageResource(R.drawable.ninja_enemy03)

        }

        val ts = Toast.makeText(customToastView.context, "", Toast.LENGTH_SHORT)
        ts.setGravity(Gravity.CENTER, 0, 0)
        (customToastView.findViewById(R.id.toast_layout_job_id) as TextView).text = "${characterAppearance.job}"
        (customToastView.findViewById(R.id.toast_layout_str_id) as TextView).text = "${characterAppearance.str}"
        (customToastView.findViewById(R.id.toast_layout_def_id) as TextView).text = "${characterAppearance.def}"
        (customToastView.findViewById(R.id.toast_layout_agi_id) as TextView).text = "${characterAppearance.agi}"
        (customToastView.findViewById(R.id.toast_layout_luck_id) as TextView).text = "${characterAppearance.luck}"
        ts.setView(customToastView)
        ts.show()
    }


    override fun onDestroy() {
        mp0.release()
        super.onDestroy()
    }

    override fun onBackPressed() {}
}

