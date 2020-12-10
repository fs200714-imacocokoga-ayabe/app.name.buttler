package com.e.app_namebattler

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_battle_main.*
import java.util.*
import kotlin.collections.ArrayList


class BattleMainActivity : AppCompatActivity(), View.OnClickListener, BattleLogListener {



    // var txtView: TextView? = null

    private var s = Scanner(System.`in`)

    //private lateinit var mListAdapter: BattleMainListAdapter

    lateinit var helper: MyOpenHelper

    private val gm = GameManager()

    val pt = Party()

    // val pl = Player()

    private var enemyPartyList = ArrayList<CharacterAllData>()

    private var allyPartyList = ArrayList<CharacterAllData>()

    private var allyPartyList02 = ArrayList<Player>()

    private lateinit var allCharacterList: List<Player>

    private var enemyPartyList02 = ArrayList<Player>()

    private lateinit var attackList: List<Player>

    private lateinit var speedOrderList: List<Player>

    var allyBattlePartyList = arrayListOf<String>()

    var battleMessageRecord: MutableList<String> = mutableListOf() // バトルログを格納

    lateinit var ally: Player
    private lateinit var enemy: Player
    lateinit var actCharacter: Player
    lateinit var ally01: Player
    lateinit var ally02: Player
    lateinit var ally03: Player
    lateinit var enemy01: Player
    lateinit var enemy02: Player
    lateinit var enemy03: Player
    private lateinit var player: Player
    private lateinit var player1: Player
    lateinit var player2: Player

    private val e = CreateEnemy()

    var job = ""

    var turnNumber = 1 // ターンの初期値

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle_main)

        val battleMainLogTextDialog = findViewById<View>(R.id.battle_main_battle_log_text_id) as TextView

        battleMainLogTextDialog.setOnClickListener(this)

        val allyName01 = intent.getStringExtra("name01_key")
        val allyName02 = intent.getStringExtra("name02_key")
        val allyName03 = intent.getStringExtra("name03_key")
        val enemyName01 = intent.getStringExtra("enemyName01_key")
        val enemyName02 = intent.getStringExtra("enemyName02_key")
        val enemyName03 = intent.getStringExtra("enemyName03_key")
        val strategyName = intent.getStringExtra("strategy_key")

        println("確認$strategyName")

        if (strategyName != null) {
            printStrategy(strategyName)
        } else {
            printStrategy("ガンガンいこうぜ")
        }

        val bl = findViewById<TextView>(R.id.battle_main_battle_log_text_id)
        bl.text = "Tap to start"

        // 敵のデータを名前から取得する
        enemyPartyList = getEnemyData(enemyName01, enemyName02, enemyName03)
        // 味方のデータを名前から取得する
        allyPartyList = getAllyData(allyName01, allyName02, allyName03)

        // 敵キャラクターを作成する
        enemy01 = makeEnemyCharacter(enemyPartyList[0])
        enemy02 = makeEnemyCharacter(enemyPartyList[1])
        enemy03 = makeEnemyCharacter(enemyPartyList[2])

        // 味方キャラクターを作成する
        ally01 = makeAllyCharacter(allyPartyList[0])
        ally02 = makeAllyCharacter(allyPartyList[1])
        ally03 = makeAllyCharacter(allyPartyList[2])

        // スピード順に取得する
        speedOrderList = (gm.speedReordering(enemy01, enemy02, enemy03, ally01, ally02, ally03))

        // パーティの振り分け
        pt.appendPlayer(enemy01, enemy02, enemy03, ally01, ally02, ally03)

        // キャラクターの表示
        enemyPrintStatus01(enemy01)
        enemyPrintStatus02(enemy02)
        enemyPrintStatus03(enemy03)
        allyPrintStatus01(ally01)
        allyPrintStatus02(ally02)
        allyPrintStatus03(ally03)


//----------------------------------------------------------------------------------------------
//override fun onCreateOptionsMenu(menu: Menu): Boolean {
//    // Inflate the menu; this adds items to the action bar if it is present.
//    menuInflater.inflate(android.R.activity_battle_main, menu)
//    return true
//}

//        fun battle(view: View?) {
//            val ts = Toast.makeText(this, "TextViewがタップされたよ", Toast.LENGTH_SHORT)
//            ts.show()
//        }

//        fun onClick(v: View?) {
//
//
//
//        }

        //  var turnNumber = 1 // ターンの初期値

//        for (i in speedOrderList) {
//
//            player1 = i
//
//            player2 = if (player1.isMark()!!) {
//
//                val a = (1..pt.getParty2().size).random()
//                pt.getParty2()[a - 1]
//
//            } else {
//
//                val a = (1..pt.getParty1().size).random()
//                pt.getParty1()[a - 1]
//
//            }
//
//            battleMessageRecord.add(player1.attack(player2).toString())
//
//            val pl = Player()
//            pl.listener = this
//
//            val sb = StringBuilder()
//            for (s in battleMessageRecord) {
//
//                val ss = s.replace("[", "").replace("]", "").replace(",", "")
//                Log.i("debug", ss)
//                sb.append(ss)
//
//                //  Thread.sleep(1_000)  // wait for 1 second
////                pl.battleLog(s)
//
//            }
//
//            pl.battleLog(sb)
//
//
//
////            for (i in 1.. battleMessageRecord.size) {
////
////                //  Thread.sleep(1_000)  // wait for 1 second
////                pl.battleLog(battleMessageRecord[i - 1])
////
////            }
////            val p1 = battleMessageRecord[0]
////            val p2 = battleMessageRecord[1]
////            val p3 = battleMessageRecord[2]
////            val p4 = battleMessageRecord[3]
////            val p5 = battleMessageRecord[4]
////            val p6 = battleMessageRecord[5]
////
////            println("ログ001$p1")
////            println("ログ002$p2")
////            println("ログ003$p3")
////            println("ログ004$p4")
////            println("ログ005$p5")
////            println("ログ006$p6")
//
//
//        }
//
//          //  }
////        val pl = Player()
////        pl.listener = this
//
//        for (i in battleMessageRecord) {
//           // println("ログ000$i")
//            Log.i("debugログ000", i)
//        }

//            while (turnNumber <= 20) {// 最大でも20ターンまで
//
//            println("ターン$turnNumber")
//
//
//            for(i in speedOrderList){
//
//                player1 = i
//
//                player2 = if (player1.isMark()!!) {
//
//                    val a = (1..pt.getParty2().size).random()
//                    pt.getParty2()[a - 1]
//
//                }else{
//
//                    val a = (1..pt.getParty1().size).random()
//                    pt.getParty1()[a - 1]
//
//                }
//
//                player1.attack(player2)
//
//
//                val bl = findViewById<TextView>(R.id.battle_main_battle_log_text_id)
//                bl.text = player1.attack(player2).toString()
//
//                if (player1.getHP() <= 0) { // プレイヤー１相手プレイヤーの敗北判定
//                    speedOrderList-=player1
//                    pt.removePlayer(player1)
//                    pt.removeMembers(player1)
//                }
//                if (player2.getHP() <= 0) { // 相手プレイヤーがHP0の場合
//                    speedOrderList-=player2
//                    pt.removePlayer(player2)
//                    pt.removeMembers(player2)
//                }
//
//                enemyPrintStatus01(enemy01)
//                enemyPrintStatus02(enemy02)
//                enemyPrintStatus03(enemy03)
//                allyPrintStatus01(ally01)
//                allyPrintStatus02(ally02)
//                allyPrintStatus03(ally03)
//
//
//                // どちらかのパーティメンバーが0の場合処理を抜ける
//                if (pt.getParty1().isEmpty() || pt.getParty2().isEmpty()) {
//                    break
//                }
//
//            }
//
//            // どちらかのパーティメンバーが0の場合処理を抜ける
//            if (pt.getParty1().isEmpty() || pt.getParty2().isEmpty()) {
//                break
//            }

        // nextTurn()
//
//            turnNumber+=1
//
//        }
//
//        // 勝ち負けの表示(残っているパーティの勝ち)
//        println("")
//
//        when {
//            pt.getParty1().isEmpty() -> { // パーティ1が0の場合
//                println("パーティ2の勝利！！/n")
//            }
//            pt.getParty2().isEmpty() -> { // パーティ2が0の場合
//                println("パーティ１の勝利！！/n")
//            }
//            else -> {
//                println("引き分け/n") // お互いパーティが全滅していなければ引き分け
//            }
//        }

        // 作戦変更画面に遷移
        strategy_change_button_id.setOnClickListener {
            val intent = Intent(this, StrategyChangeActivity::class.java)
            startActivity(intent)
        }

        next_turn_button_id.setOnClickListener {
            battle()
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
                        OccupationConversion(c.getInt(1)),
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
                        OccupationConversion(c.getInt(1)),
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
    private fun OccupationConversion(jobValue: Int): String {

        when (jobValue) {

            0 -> {
                job = "戦士"
            }
            1 -> {
                job = "魔法使い"
            }
            2 -> {
                job = "僧侶"
            }
            3 -> {
                job = "忍者"
            }
        }
        return job
    }

    // 敵キャラクターを作成する
    private fun makeEnemyCharacter(enemyPartyList: CharacterAllData): Player {

        when (enemyPartyList.job) {

            "戦士" -> enemy = (Fighter(
                enemyPartyList.name,
                enemyPartyList.job,
                enemyPartyList.hp,
                enemyPartyList.mp,
                enemyPartyList.str,
                enemyPartyList.def,
                enemyPartyList.agi,
                enemyPartyList.luck
            ))

            "魔法使い" -> enemy = (Wizard(
                enemyPartyList.name,
                enemyPartyList.job,
                enemyPartyList.hp,
                enemyPartyList.mp,
                enemyPartyList.str,
                enemyPartyList.def,
                enemyPartyList.agi,
                enemyPartyList.luck
            ))

            "僧侶" -> enemy = (Priest(
                enemyPartyList.name,
                enemyPartyList.job,
                enemyPartyList.hp,
                enemyPartyList.mp,
                enemyPartyList.str,
                enemyPartyList.def,
                enemyPartyList.agi,
                enemyPartyList.luck
            ))

            "忍者" -> enemy = (Ninja(
                enemyPartyList.name,
                enemyPartyList.job,
                enemyPartyList.hp,
                enemyPartyList.mp,
                enemyPartyList.str,
                enemyPartyList.def,
                enemyPartyList.agi,
                enemyPartyList.luck
            ))
        }

        enemy.setMaxHp(enemy.hp)
        enemy.setMaxMp(enemy.mp)
        enemy.setMark(false)
        enemy.isPoison = false
        enemy.isParalysis = false

        return enemy
    }

    // 味方キャラクターを作成する
    fun makeAllyCharacter(allyPartyList: CharacterAllData): Player {

        when (allyPartyList.job) {

            "戦士" -> ally = (Fighter(
                allyPartyList.name,
                allyPartyList.job,
                allyPartyList.hp,
                allyPartyList.mp,
                allyPartyList.str,
                allyPartyList.def,
                allyPartyList.agi,
                allyPartyList.luck
            ))

            "魔法使い" -> ally = (Wizard(
                allyPartyList.name,
                allyPartyList.job,
                allyPartyList.hp,
                allyPartyList.mp,
                allyPartyList.str,
                allyPartyList.def,
                allyPartyList.agi,
                allyPartyList.luck
            ))

            "僧侶" -> ally = (Priest(
                allyPartyList.name,
                allyPartyList.job,
                allyPartyList.hp,
                allyPartyList.mp,
                allyPartyList.str,
                allyPartyList.def,
                allyPartyList.agi,
                allyPartyList.luck
            ))

            "忍者" -> ally = (Ninja(
                allyPartyList.name,
                allyPartyList.job,
                allyPartyList.hp,
                allyPartyList.mp,
                allyPartyList.str,
                allyPartyList.def,
                allyPartyList.agi,
                allyPartyList.luck
            ))
        }

        ally.setMaxHp(ally.hp)
        ally.setMaxMp(ally.mp)
        ally.setMark(true)
        ally.isPoison = false
        ally.isParalysis = false

        return ally
    }

    // 味方キャラクター01のステータスを表示する
    private fun allyPrintStatus01(ally01: Player) {

        val allyName01Text: TextView = findViewById(R.id.ally_member01_name_id)
        allyName01Text.text = ally01.getName()

        val allyHp01Text: TextView = findViewById(R.id.ally_member01_hp_id)
        allyHp01Text.text = ("%s %d/%d".format("HP", ally01.hp, ally01.getMaxHp()))

        val allyMp01Text: TextView = findViewById(R.id.ally_member01_mp_id)
        allyMp01Text.text = ("%s %d/%d".format("MP", ally01.mp, ally01.getMaxMp()))

//        val allyStatus01Text: TextView = findViewById(R.id.ally_member01_status_id)
//        allyStatus01Text.text =ally01.Status

    }

    // 味方キャラクター02のステータスを表示する
    private fun allyPrintStatus02(ally02: Player) {

        val allyName02Text: TextView = findViewById(R.id.ally_member02_name_id)
        allyName02Text.text = ally02.getName()

        val allyHp02Text: TextView = findViewById(R.id.ally_member02_hp_id)
        allyHp02Text.text = ("%s %d/%d".format("HP", ally02.hp, ally02.getMaxHp()))

        val allyMp02Text: TextView = findViewById(R.id.ally_member02_mp_id)
        allyMp02Text.text = ("%s %d/%d".format("MP", ally02.mp, ally02.getMaxMp()))
    }

    // 味方キャラクター03のステータスを表示する
    private fun allyPrintStatus03(ally03: Player) {

        val allyName03Text: TextView = findViewById(R.id.ally_member03_name_id)
        allyName03Text.text = ally03.getName()

        val allyHp03Text: TextView = findViewById(R.id.ally_member03_hp_id)
        allyHp03Text.text = ("%s %d/%d".format("HP", ally03.hp, ally03.getMaxHp()))

        val allyMp03Text: TextView = findViewById(R.id.ally_member03_mp_id)
        allyMp03Text.text = ("%s %d/%d".format("MP", ally03.mp, ally03.getMaxMp()))

    }

    //  敵キャラクター01のステータスを表示する
    private fun enemyPrintStatus01(enemy01: Player) {

        val enemyName01Text: TextView = findViewById(R.id.enemy_member01_name_id)
        enemyName01Text.text = enemy01.getName()

        val enemyHp01Text: TextView = findViewById(R.id.enemy_member01_hp_id)
        enemyHp01Text.text = ("%s %d/%d".format("HP", enemy01.hp, enemy01.getMaxHp()))

        val enemyMp01Text: TextView = findViewById(R.id.enemy_member01_mp_id)
        enemyMp01Text.text = ("%s %d/%d".format("MP", enemy01.mp, enemy01.getMaxMp()))


    }

    //  敵キャラクター02のステータスを表示する
    private fun enemyPrintStatus02(enemy02: Player) {

        val enemyName02Text: TextView = findViewById(R.id.enemy_member02_name_id)
        enemyName02Text.text = enemy02.getName()

        val enemyHp02Text: TextView = findViewById(R.id.enemy_member02_hp_id)
        enemyHp02Text.text = ("%s %d/%d".format("HP", enemy02.hp, enemy02.getMaxHp()))

        val enemyMp02Text: TextView = findViewById(R.id.enemy_member02_mp_id)
        enemyMp02Text.text = ("%s %d/%d".format("MP", enemy02.mp, enemy02.getMaxMp()))


    }

    //  敵キャラクター03のステータスを表示する
    private fun enemyPrintStatus03(enemy03: Player) {

        val enemyName03Text: TextView = findViewById(R.id.enemy_member03_name_id)
        enemyName03Text.text = enemy03.getName()

        val enemyHp03Text: TextView = findViewById(R.id.enemy_member03_hp_id)
        enemyHp03Text.text = ("%s %d/%d".format("HP", enemy03.hp, enemy03.getMaxHp()))

        val enemyMp03Text: TextView = findViewById(R.id.enemy_member03_mp_id)
        enemyMp03Text.text = ("%s %d/%d".format("MP", enemy03.mp, enemy03.getMaxMp()))


    }

    private fun printStrategy(strategyName: String) {

        val strategyText: TextView = findViewById(R.id.battle_main_strategy_text_id)
        strategyText.text = strategyName

    }

    private fun makePartyList(
        enemyName01: String?,
        enemyName02: String?,
        enemyName03: String?,
        allyName01: String?,
        allyName02: String?,
        allyName03: String?
    ) {

        // 敵のデータを名前から取得する
        enemyPartyList = getEnemyData(enemyName01, enemyName02, enemyName03)
        // 味方のデータを名前から取得する
        allyPartyList = getAllyData(allyName01, allyName02, allyName03)

        // 敵キャラクターを作成する
        enemy01 = makeEnemyCharacter(enemyPartyList[0])
        enemy02 = makeEnemyCharacter(enemyPartyList[1])
        enemy03 = makeEnemyCharacter(enemyPartyList[2])

        // 味方キャラクターを作成する
        ally01 = makeAllyCharacter(allyPartyList[0])
        ally02 = makeAllyCharacter(allyPartyList[1])
        ally03 = makeAllyCharacter(allyPartyList[2])

        // スピード順に取得する
        speedOrderList = (gm.speedReordering(enemy01, enemy02, enemy03, ally01, ally02, ally03))

    }

    override fun upDateBattleLog(log: StringBuilder) {

        val bl = findViewById<TextView>(R.id.battle_main_battle_log_text_id)
        bl.text = log
    }

    override fun onClick(v: View?) {

        if (turnNumber == 1){
            battle()
        }
//        if (v != null) {
//            if (v.id == R.id.battle_main_battle_log_text_id) {
        Toast.makeText(this, "テストモード", Toast.LENGTH_SHORT).show()
//                battle()
//            }
//        }
        turnNumber += 1

    }

    private fun battle(){

        val sb = StringBuilder()

        for (i in speedOrderList) {

            player1 = i

            player2 = if (player1.isMark()!!) {

                val a = (1..pt.getParty2().size).random()
                pt.getParty2()[a - 1]

            } else {

                val a = (1..pt.getParty1().size).random()
                pt.getParty1()[a - 1]
            }

            battleMessageRecord.add(player1.attack(player2).toString())

            if (player1.getHP() <= 0) { // プレイヤー１相手プレイヤーの敗北判定
                speedOrderList-=player1
                pt.removePlayer(player1)
                pt.removeMembers(player1)
            }
            if (player2.getHP() <= 0) { // 相手プレイヤーがHP0の場合
                speedOrderList-=player2
                pt.removePlayer(player2)
                pt.removeMembers(player2)
            }

            enemyPrintStatus01(enemy01)
            enemyPrintStatus02(enemy02)
            enemyPrintStatus03(enemy03)
            allyPrintStatus01(ally01)
            allyPrintStatus02(ally02)
            allyPrintStatus03(ally03)

            // どちらかのパーティメンバーが0の場合処理を抜ける
            if (pt.getParty1().isEmpty() || pt.getParty2().isEmpty()) {

                val intent = Intent(this, BattleResultScreenActivity::class.java)
                startActivity(intent)

            }

//            }
//
            // どちらかのパーティメンバーが0の場合処理を抜ける
//            if (pt.getParty1().isEmpty() || pt.getParty2().isEmpty()) {
//
//                val intent = Intent(this, BattleResultScreenActivity::class.java)
//                startActivity(intent)
//
//            }

            // nextTurn()
//
//            turnNumber+=1
//
//        }
//
            // 勝ち負けの表示(残っているパーティの勝ち)
            when {
                pt.getParty1().isEmpty() -> { // パーティ1が0の場合
                    println("パーティ2の勝利！！/n")
                    sb.append("パーティ2の勝利！！/n")
                }
                pt.getParty2().isEmpty() -> { // パーティ2が0の場合
                    println("パーティ１の勝利！！/n")
                    sb.append("パーティ1の勝利！！/n")
                }
            }


            val pl = Player()
            pl.listener = this


            for (s in battleMessageRecord) {

                val ss = s.replace("[", "").replace("]", "").replace(",", "")
                Log.i("debug", ss)
                sb.append(ss)

            }
            pl.battleLog(sb)

//            Log.i("エスビー00",sb.toString())
        }

        battleMessageRecord.clear()
        sb.clear()

        Log.i("エスビーi01", sb.toString())

//        for (i in battleMessageRecord) {
//            // println("ログ000$i")
//            Log.i("debugログ000", i)
//        }

    }
}

