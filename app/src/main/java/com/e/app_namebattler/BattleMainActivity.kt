package com.e.app_namebattler

//import com.e.app_namebattler.GameManager.myThread
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_battle_main.*
import java.util.*
import kotlin.collections.ArrayList

class BattleMainActivity : AppCompatActivity(), View.OnClickListener, BattleLogListener {

     private val party1: MutableList<Player> = ArrayList()

     private val party2: MutableList<Player> = ArrayList()

    var handler: Handler? = null

    private var s = Scanner(System.`in`)

    lateinit var helper: MyOpenHelper

    private val gm = GameManager()

    val pt = Party()

     val pl = Player()

    //val hd = MyHandler()

    private var enemyPartyList = ArrayList<CharacterAllData>()

    private var allyPartyList = ArrayList<CharacterAllData>()

    lateinit var ally: Player
    private lateinit var enemy: Player
    lateinit var ally01: Player
    lateinit var ally02: Player
    lateinit var ally03: Player
    lateinit var enemy01: Player
    lateinit var enemy02: Player
    lateinit var enemy03: Player

    private val e = CreateEnemy()

    var job = ""

    var turnNumber = 1 // ターンの初期値

   // private var updateText: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle_main)

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
            printStrategy("ガンガンいこうぜ")
        }

        // タップすると始まります
        val bl = findViewById<TextView>(R.id.battle_main_battle_log_text_id)
        bl.text = "Tap to start"

        // 敵のデータを名前から取得する
        enemyPartyList = getEnemyData(enemyName01, enemyName02, enemyName03)
        // 味方のデータを名前から取得する
        allyPartyList = getAllyData(allyName01, allyName02, allyName03)

        gm.myCallBack = this
      //  pl.myCallBack = this
      //  hd.myCallBack = this

        // コントロールをGameManagerに移譲
        gm.controlTransfer(allyPartyList, enemyPartyList)

        // 作戦変更画面に遷移
        strategy_change_button_id.setOnClickListener {
            val intent = Intent(this, StrategyChangeActivity::class.java)
            startActivity(intent)
        }

        next_turn_button_id.setOnClickListener {

            if(turnNumber > 1) {

                gm.battle()

                if (gm.getParty01().isEmpty() || gm.getParty02().isEmpty()) {

                    val party00 = gm.getParty01().size

                    val intent = Intent(this, BattleResultScreenActivity::class.java)

                    intent.putExtra("name_key01", allyName01)
                    intent.putExtra("name_key02", allyName02)
                    intent.putExtra("name_key03", allyName03)
                    intent.putExtra("name_key04", enemyName01)
                    intent.putExtra("name_key05", enemyName02)
                    intent.putExtra("name_key06", enemyName03)
                    intent.putExtra("party_key", party00)

                    startActivity(intent)

                }
            }
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
    fun allyPrintStatus01(ally01: Player) {

        val allyName01Text: TextView = findViewById(R.id.ally_member01_name_id)

        if (ally01.getHP() <= 0) {

            allyName01Text.setTextColor(Color.RED)
           // allyName01Text.text = ally01.getName()
        }

            allyName01Text.text = ally01.getName()

        val allyHp01Text: TextView = findViewById(R.id.ally_member01_hp_id)
        allyHp01Text.text = ("%s %d/%d".format("HP", ally01.hp, ally01.getMaxHp()))

        val allyMp01Text: TextView = findViewById(R.id.ally_member01_mp_id)
        allyMp01Text.text = ("%s %d/%d".format("MP", ally01.mp, ally01.getMaxMp()))

        val allyStatus01Text: TextView = findViewById(R.id.ally_member01_status_id)
        allyStatus01Text.text = ("%s %s".format(ally01.getPoison(), ally01.getParalysis()))
    }

    // 味方キャラクター02のステータスを表示する
    fun allyPrintStatus02(ally02: Player) {

        val allyName02Text: TextView = findViewById(R.id.ally_member02_name_id)

        if (ally02.getHP() <= 0) {

            allyName02Text.setTextColor(Color.RED)
            // allyName01Text.text = ally01.getName()
        }
        allyName02Text.text = ally02.getName()

        val allyHp02Text: TextView = findViewById(R.id.ally_member02_hp_id)
        allyHp02Text.text = ("%s %d/%d".format("HP", ally02.hp, ally02.getMaxHp()))

        val allyMp02Text: TextView = findViewById(R.id.ally_member02_mp_id)
        allyMp02Text.text = ("%s %d/%d".format("MP", ally02.mp, ally02.getMaxMp()))

        val allyStatus02Text: TextView = findViewById(R.id.ally_member02_status_id)
        allyStatus02Text.text = ("%s %s".format(ally02.getPoison(), ally02.getParalysis()))
    }

    // 味方キャラクター03のステータスを表示する
    fun allyPrintStatus03(ally03: Player) {

        val allyName03Text: TextView = findViewById(R.id.ally_member03_name_id)

        if (ally03.getHP() <= 0) {

            allyName03Text.setTextColor(Color.RED)
            // allyName01Text.text = ally01.getName()
        }

        allyName03Text.text = ally03.getName()

        val allyHp03Text: TextView = findViewById(R.id.ally_member03_hp_id)
        allyHp03Text.text = ("%s %d/%d".format("HP", ally03.hp, ally03.getMaxHp()))

        val allyMp03Text: TextView = findViewById(R.id.ally_member03_mp_id)
        allyMp03Text.text = ("%s %d/%d".format("MP", ally03.mp, ally03.getMaxMp()))

        val allyStatus03Text: TextView = findViewById(R.id.ally_member03_status_id)
        allyStatus03Text.text = ("%s %s".format(ally03.getPoison(), ally03.getParalysis()))

    }

    //  敵キャラクター01のステータスを表示する
    fun enemyPrintStatus01(enemy01: Player) {

        val enemyName01Text: TextView = findViewById(R.id.enemy_member01_name_id)

        if (enemy01.getHP() <= 0) {

            enemyName01Text.setTextColor(Color.RED)
            // allyName01Text.text = ally01.getName()
        }
        enemyName01Text.text = enemy01.getName()

        val enemyHp01Text: TextView = findViewById(R.id.enemy_member01_hp_id)
        enemyHp01Text.text = ("%s %d/%d".format("HP", enemy01.hp, enemy01.getMaxHp()))

        val enemyMp01Text: TextView = findViewById(R.id.enemy_member01_mp_id)
        enemyMp01Text.text = ("%s %d/%d".format("MP", enemy01.mp, enemy01.getMaxMp()))

        val enemyStatus01Text: TextView = findViewById(R.id.enemy_member01_status_id)
        enemyStatus01Text.text = ("%s %s".format(enemy01.getPoison(), enemy01.getParalysis()))

    }

    //  敵キャラクター02のステータスを表示する
    fun enemyPrintStatus02(enemy02: Player) {

        val enemyName02Text: TextView = findViewById(R.id.enemy_member02_name_id)

        if (enemy02.getHP() <= 0) {

            enemyName02Text.setTextColor(Color.RED)
            // allyName01Text.text = ally01.getName()
        }
        enemyName02Text.text = enemy02.getName()

        val enemyHp02Text: TextView = findViewById(R.id.enemy_member02_hp_id)
        enemyHp02Text.text = ("%s %d/%d".format("HP", enemy02.hp, enemy02.getMaxHp()))

        val enemyMp02Text: TextView = findViewById(R.id.enemy_member02_mp_id)
        enemyMp02Text.text = ("%s %d/%d".format("MP", enemy02.mp, enemy02.getMaxMp()))

        val enemyStatus02Text: TextView = findViewById(R.id.enemy_member02_status_id)
        enemyStatus02Text.text = ("%s %s".format(enemy02.getPoison(), enemy02.getParalysis()))

    }

    //  敵キャラクター03のステータスを表示する
    fun enemyPrintStatus03(enemy03: Player) {

        val enemyName03Text: TextView = findViewById(R.id.enemy_member03_name_id)

        if (enemy03.getHP() <= 0) {

            enemyName03Text.setTextColor(Color.RED)
            // allyName01Text.text = ally01.getName()
        }
        enemyName03Text.text = enemy03.getName()

        val enemyHp03Text: TextView = findViewById(R.id.enemy_member03_hp_id)
        enemyHp03Text.text = ("%s %d/%d".format("HP", enemy03.hp, enemy03.getMaxHp()))

        val enemyMp03Text: TextView = findViewById(R.id.enemy_member03_mp_id)
        enemyMp03Text.text = ("%s %d/%d".format("MP", enemy03.mp, enemy03.getMaxMp()))

        val enemyStatus03Text: TextView = findViewById(R.id.enemy_member03_status_id)
        enemyStatus03Text.text = ("%s %s".format(enemy03.getPoison(), enemy03.getParalysis()))

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

    }

    override fun upDateBattleLog(testLog: List<String>){

//        for (i in testLog){
//
//            val array = i.split("@")
//        }


        val tl = findViewById<TextView>(R.id.battle_main_battle_log_text_id)
println("ろぐ00$testLog")
        for (i in 1..testLog.size) {
            when (i) {
                1 -> { handler?.postDelayed({ tl.text = testLog[i - 1] }, 500)
                    println("ろぐ01${testLog[i - 1]}")}

                2 -> { handler?.postDelayed({ tl.text = testLog[i - 1] }, 3500)
                        println("ろぐ02${testLog[i - 1]}")}

                3 -> { handler?.postDelayed({tl.text = testLog[i - 1] }, 6500)
                    println("ろぐ03${testLog[i - 1]}")}

                4 -> { handler?.postDelayed({tl.text = testLog[i - 1] }, 9500)
                    println("ろぐ04${testLog[i - 1]}")}

                5 -> { handler?.postDelayed({tl.text = testLog[i - 1] }, 12500)
                    println("ろぐ05${testLog[i - 1]}")}

                6 -> { handler?.postDelayed({tl.text = testLog[i - 1] }, 15500)
                    println("ろぐ06${testLog[i - 1]}")}
            }
        }
    }

    override fun upDateStatus(
        ally01: Player,
        ally02: Player,
        ally03: Player,
        enemy01: Player,
        enemy02: Player,
        enemy03: Player
    ) {

        allyPrintStatus01(ally01)
        allyPrintStatus02(ally02)
        allyPrintStatus03(ally03)
        enemyPrintStatus01(enemy01)
        enemyPrintStatus02(enemy02)
        enemyPrintStatus03(enemy03)

    }

    override fun onClick(v: View?) {

        if (turnNumber == 1) {

            gm.battle()

            turnNumber += 1
        }

        Toast.makeText(this, "テストモード", Toast.LENGTH_SHORT).show()
    }
}

