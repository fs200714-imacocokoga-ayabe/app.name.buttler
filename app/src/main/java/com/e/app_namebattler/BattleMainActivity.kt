package com.e.app_namebattler

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_battle_main.*


class BattleMainActivity : AppCompatActivity() {

    //private lateinit var mListAdapter: BattleMainListAdapter

    lateinit var helper: MyOpenHelper

    val gm = GameManager()

    private var enemyPartyList = ArrayList<CharacterAllData>()

    private var allyPartyList = ArrayList<CharacterAllData>()

    private var allyPartyList02 = ArrayList<Player>()

    private  var enemyPartyList02 = ArrayList<Player>()

   // var enemyBattlePartyList = arrayListOf<Player>()
    //var enemyBattlePartyList:List<Player> = mutableListOf()
    var allyBattlePartyList = arrayListOf<Player>()

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

//    var enemyBattleStatusPartyList = arrayListOf<CharacterBattleStatusData>()
//
//    var allyBattleStatusPartyList = arrayListOf<CharacterBattleStatusData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle_main)

        val allyName01 = intent.getStringExtra("name01_key")
        val allyName02 = intent.getStringExtra("name02_key")
        val allyName03 = intent.getStringExtra("name03_key")
        val enemyName01 = intent.getStringExtra("enemyName01_key")
        val enemyName02 = intent.getStringExtra("enemyName02_key")
        val enemyName03 = intent.getStringExtra("enemyName03_key")

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


        //var enemyBattlePartyList = listOf<Player>()

       // gm.Start()

        // speedOrderList.addAll(gm.speedReordering(enemy01,enemy02,enemy03,ally01,ally02,ally03))

        val speedOrderList:List<Player> = (gm.speedReordering(enemy01,enemy02,enemy03,ally01,ally02,ally03))

        println("プリント$enemy01")


        for(i in 1 .. speedOrderList.size){

            if(speedOrderList[i - 1].isMark()!!){

                allyPartyList02.add(speedOrderList[i - 1])

            }else{

                enemyPartyList02.add(speedOrderList[i - 1])
            }
        }

        ally01 = allyPartyList02[0]
        ally02 = allyPartyList02[1]
        ally03 = allyPartyList02[2]
        enemy01 = enemyPartyList02[0]
        enemy02 = enemyPartyList02[1]
        enemy03 = enemyPartyList02[2]

        // キャラクターの表示
        enemyPrintStatus01(enemy01)
        enemyPrintStatus02(enemy02)
        enemyPrintStatus03(enemy03)
        allyPrintStatus01(ally01)
        allyPrintStatus02(ally02)
        allyPrintStatus03(ally03)









        // 作戦変更画面に遷移
        strategy_change_button_id.setOnClickListener {
            val intent = Intent(this, StrategyChangeActivity::class.java)
            startActivity(intent)
        }
    }

    // 名前を受け取りデータベースから敵キャラクターのステータスを取得する
    private fun getEnemyData(enemyName01: String?, enemyName02: String?, enemyName03: String?): ArrayList<CharacterAllData> {

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
    private fun getAllyData(allyName01: String?, allyName02: String?, allyName03: String?): ArrayList<CharacterAllData> {

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
    private fun OccupationConversion(jobValue: Int):String{

        when(jobValue){

            0 -> { job = "戦士" }
            1 -> { job = "魔法使い" }
            2 -> { job = "僧侶" }
            3 -> { job = "忍者" }
        }
        return job
    }

    // 敵キャラクターを作成する
    fun makeEnemyCharacter(enemyPartyList: CharacterAllData): Player {

        when(enemyPartyList.job){

        "戦士" ->  enemy = (Fighter(enemyPartyList.name,enemyPartyList.job,enemyPartyList.hp,enemyPartyList.mp,enemyPartyList.str,enemyPartyList.def,enemyPartyList.agi,enemyPartyList.luck))

        "魔法使い" -> enemy = (Wizard(enemyPartyList.name,enemyPartyList.job,enemyPartyList.hp,enemyPartyList.mp,enemyPartyList.str,enemyPartyList.def,enemyPartyList.agi,enemyPartyList.luck))

        "僧侶" -> enemy = (Priest(enemyPartyList.name,enemyPartyList.job,enemyPartyList.hp,enemyPartyList.mp,enemyPartyList.str,enemyPartyList.def,enemyPartyList.agi,enemyPartyList.luck))

        "忍者" -> enemy = (Ninja(enemyPartyList.name,enemyPartyList.job,enemyPartyList.hp,enemyPartyList.mp,enemyPartyList.str,enemyPartyList.def,enemyPartyList.agi,enemyPartyList.luck))
        }

        enemy.setMaxHp(enemy.hp)
        enemy.setMaxMp(enemy.mp)
        enemy.setMark(false)
        enemy.isPoison = false
        enemy.ispParalysis = false

         return  enemy
    }

    // 味方キャラクターを作成する
    fun makeAllyCharacter(allyPartyList: CharacterAllData): Player {

        when(allyPartyList.job){

        "戦士" ->  ally = (Fighter(allyPartyList.name,allyPartyList.job,allyPartyList.hp,allyPartyList.mp,allyPartyList.str,allyPartyList.def,allyPartyList.agi,allyPartyList.luck))

        "魔法使い" -> ally = (Wizard(allyPartyList.name,allyPartyList.job,allyPartyList.hp,allyPartyList.mp,allyPartyList.str,allyPartyList.def,allyPartyList.agi,allyPartyList.luck))

        "僧侶" -> ally = (Priest(allyPartyList.name,allyPartyList.job,allyPartyList.hp,allyPartyList.mp,allyPartyList.str,allyPartyList.def,allyPartyList.agi,allyPartyList.luck))

        "忍者" -> ally = (Ninja(allyPartyList.name,allyPartyList.job,allyPartyList.hp,allyPartyList.mp,allyPartyList.str,allyPartyList.def,allyPartyList.agi,allyPartyList.luck))
        }

        ally.setMaxHp(ally.hp)
        ally.setMaxMp(ally.mp)
        ally.setMark(true)
        ally.isPoison = false
        ally.ispParalysis = false

        return ally
    }

    // 味方キャラクター01のステータスを表示する
    private fun allyPrintStatus01(ally01: Player) {

        val allyName01Text: TextView = findViewById(R.id.ally_member01_name_id)
        allyName01Text.text =ally01.name

        val allyHp01Text: TextView = findViewById(R.id.ally_member01_hp_id)
        allyHp01Text.text = ("%s %d/%d".format("HP",ally01.hp,ally01.getMaxHp()))

        val allyMp01Text: TextView = findViewById(R.id.ally_member01_mp_id)
        allyMp01Text.text = ("%s %d/%d".format("MP",ally01.mp,ally01.getMaxMp()))

//        val allyStatus01Text: TextView = findViewById(R.id.ally_member01_status_id)
//        allyStatus01Text.text =ally01.Status

    }

    // 味方キャラクター02のステータスを表示する
    private fun allyPrintStatus02(ally02: Player) {

        val allyName02Text: TextView = findViewById(R.id.ally_member02_name_id)
        allyName02Text.text =ally02.name

        val allyHp02Text: TextView = findViewById(R.id.ally_member02_hp_id)
        allyHp02Text.text = ("%s %d/%d".format("HP",ally02.hp,ally02.getMaxHp()))

        val allyMp02Text: TextView = findViewById(R.id.ally_member02_mp_id)
        allyMp02Text.text = ("%s %d/%d".format("MP",ally02.mp,ally02.getMaxMp()))
    }

    // 味方キャラクター03のステータスを表示する
    private fun allyPrintStatus03(ally03: Player) {

        val allyName03Text: TextView = findViewById(R.id.ally_member03_name_id)
        allyName03Text.text =ally03.name

        val allyHp03Text: TextView = findViewById(R.id.ally_member03_hp_id)
        allyHp03Text.text = ("%s %d/%d".format("HP",ally03.hp,ally03.getMaxHp()))

        val allyMp03Text: TextView = findViewById(R.id.ally_member03_mp_id)
        allyMp03Text.text = ("%s %d/%d".format("MP",ally03.mp,ally03.getMaxMp()))

    }

    //  敵キャラクター01のステータスを表示する
    private fun enemyPrintStatus01(enemy01: Player) {

        val enemyName01Text: TextView = findViewById(R.id.enemy_member01_name_id)
        enemyName01Text.text =enemy01.name

        val enemyHp01Text: TextView = findViewById(R.id.enemy_member01_hp_id)
        enemyHp01Text.text = ("%s %d/%d".format("HP",enemy01.hp,enemy01.getMaxHp()))

        val enemyMp01Text: TextView = findViewById(R.id.enemy_member01_mp_id)
        enemyMp01Text.text = ("%s %d/%d".format("MP",enemy01.mp,enemy01.getMaxMp()))


    }

    //  敵キャラクター02のステータスを表示する
    private fun enemyPrintStatus02(enemy02: Player) {

        val enemyName02Text: TextView = findViewById(R.id.enemy_member02_name_id)
        enemyName02Text.text =enemy02.name

        val enemyHp02Text: TextView = findViewById(R.id.enemy_member02_hp_id)
        enemyHp02Text.text = ("%s %d/%d".format("HP",enemy02.hp,enemy02.getMaxHp()))

        val enemyMp02Text: TextView = findViewById(R.id.enemy_member02_mp_id)
        enemyMp02Text.text = ("%s %d/%d".format("MP",enemy02.mp,enemy02.getMaxMp()))



    }

    //  敵キャラクター03のステータスを表示する
    private fun enemyPrintStatus03(enemy03: Player) {

        val enemyName03Text: TextView = findViewById(R.id.enemy_member03_name_id)
        enemyName03Text.text =enemy03.name

        val enemyHp03Text: TextView = findViewById(R.id.enemy_member03_hp_id)
        enemyHp03Text.text = ("%s %d/%d".format("HP",enemy03.hp,enemy03.getMaxHp()))

        val enemyMp03Text: TextView = findViewById(R.id.enemy_member03_mp_id)
        enemyMp03Text.text = ("%s %d/%d".format("MP",enemy03.mp,enemy03.getMaxMp()))


    }

}