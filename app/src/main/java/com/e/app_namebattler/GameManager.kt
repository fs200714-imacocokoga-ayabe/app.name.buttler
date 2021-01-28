package com.e.app_namebattler

import android.os.Handler
import java.util.*
import kotlin.collections.ArrayList


class GameManager {

    var random = Random()

    private var party01: MutableList<Player> = ArrayList()

    private val party02: MutableList<Player> = ArrayList()

    private var attackList: MutableList<Player> = ArrayList()

    private var allyStatusData: MutableList<MemberStatusData> = ArrayList()

    private val enemyStatusData: MutableList<MemberStatusData> = ArrayList()

    private val pt = Party()

    private val pl = Player()

    var context: Context? = null

    private val handler: Handler = Handler()

    private val sb = StringBuilder()

    var myCallBack: BattleLogListener? = null

    private lateinit var speedOrderList: List<Player>

    private var allyList: MutableList<Player> = ArrayList() // パーティ1の入れ物

    private var enemyList: MutableList<Player> = ArrayList() // パーティ1の入れ物

    lateinit var ally: Player
    private lateinit var enemy: Player
    lateinit var ally01: Player
    lateinit var ally02: Player
    lateinit var ally03: Player
    lateinit var enemy01: Player
    lateinit var enemy02: Player
    lateinit var enemy03: Player
    private lateinit var player: Player
    private lateinit var player1: Player
    private lateinit var player2: Player
    var job = ""

    private var allyStrategyNumber = 0 // 作戦の選択に使用
    private var enemyStrategyNumber = 0 // 作戦の選択に使用
    var strategyNumber = 0// 個別の作戦の選択に使用
    var strategyData = IntArray(2) // 攻撃プレイヤーIDと守備プレイヤーIDと作戦番号を格納


    // Activityから指揮権を受け取る
    fun controlTransfer(
        allyPartyList: ArrayList<CharacterAllData>,
        enemyPartyList: ArrayList<CharacterAllData>
    ) {

        // 敵キャラクターを作成する
        enemy01 = makeEnemyCharacter(enemyPartyList[0], 0)
        enemy02 = makeEnemyCharacter(enemyPartyList[1], 1)
        enemy03 = makeEnemyCharacter(enemyPartyList[2], 2)

        // 味方キャラクターを作成する
        ally01 = makeAllyCharacter(allyPartyList[0], 3)
        ally02 = makeAllyCharacter(allyPartyList[1], 4)
        ally03 = makeAllyCharacter(allyPartyList[2], 5)

        // スピード順に取得する
        speedOrderList = (speedReordering(enemy01, enemy02, enemy03, ally01, ally02, ally03))

        huriwake(enemy01, enemy02, enemy03, ally01, ally02, ally03)

        // パーティの振り分け
        pt.appendPlayer(enemy01, enemy02, enemy03, ally01, ally02, ally03)

        // キャラクターの表示
        statusLog(ally01, ally02, ally03, enemy01, enemy02, enemy03)


    }

    private fun huriwake(
        enemy01: Player,
        enemy02: Player,
        enemy03: Player,
        ally01: Player,
        ally02: Player,
        ally03: Player
    ) {

        val charaList: MutableList<Player> =
            mutableListOf(ally01, ally02, ally03, enemy01, enemy02, enemy03)

        for (player in charaList) {

            if (player.isMark()!!) {

                allyList.add(player)

            } else {

                enemyList.add(player)
            }
        }

    }


    fun battle(strategyNumber: Int) {

        attackList.clear()

        for (i in 1..pt.getMembers().size) {

            attackList.add(pt.getMembers()[i - 1])
        }
        for (i in attackList.indices) { // attackに格納したplayerが全員行動する
            player1 = attackList[i] // 攻撃リストから呼び出し
            if (player1.isLive) {
                if (player1.isMark()!!) { // player1が敵の場合
                    strategyData = selectStrategyNumber(strategyNumber)
                } else {
                    enemyStrategyNumber = random.nextInt(5) + 1 // 作戦ランダム
                    strategyData = selectStrategyNumber(enemyStrategyNumber)
                }
            }
println("ストラテジーデータ$strategyData[0]")
            println("ストラテジーデータ$strategyData[0].name")
            player2 = pt.selectMember(strategyData[0])!! // 作戦で選んだ相手を呼ぶ
            sb.append("@@")
            sb.append(player1.attack(player2, strategyData[1])) // player1に相手と作戦を送り攻撃する

//            sb.append("@@")
//            sb.append(player1.attack(player2, strategyData[1]))

            // 敗北判定
            defeatDecision()

            if (pt.getParty1().isEmpty() || pt.getParty2().isEmpty()) {
                break
            }


        }

        val array = sb.split("@@")

        myCallBack?.upDateBattleLog(array)

        // キャラクターの表示
        statusLog(ally01, ally02, ally03, enemy01, enemy02, enemy03)

        sb.clear()

        party01.clear()
        party02.clear()

        party01.plusAssign(pt.getParty1())

        party02.plusAssign(pt.getParty2())

        sb.clear()

    }

    private fun selectStrategyNumber(number: Int): IntArray {
        when (number) {
            0 -> context = Context(Strategy1())
            1 -> context = Context(Strategy2())
            2 -> context = Context(Strategy3())
            3 -> context = Context(Strategy4())
            4 -> context = Context(Strategy5())
        }
        strategyData = context?.attackStrategy(player1, pt.getParty1(),
            pt.getParty2())!!
        println("ストラテジーデータ$strategyData")
        return strategyData
    }


    private fun defeatDecision(){

        if (player1.getHP() <= 0) { // プレイヤー１相手プレイヤーの敗北判定
            speedOrderList-=player1
            pt.removePlayer(player1)
            pt.removeMembers(player1)
        }
        if (player2.getHP() <= 0) { // 相手プレイヤーがHP0の場合
            speedOrderList -= player2
            pt.removePlayer(player2)
            pt.removeMembers(player2)
        }

    }

    private fun speedReordering(
        enemy01: Player,
        enemy02: Player,
        enemy03: Player,
        ally01: Player,
        ally02: Player,
        ally03: Player
    ): List<Player> {

        val speedData: MutableList<Player> = mutableListOf(ally01,
            ally02,
            ally03,
            enemy01,
            enemy02,
            enemy03)

        for (i in 0 until speedData.size - 1) { // 速さ順の並び替え処理
            for (j in 0 until speedData.size - i - 1) {
                player1 = speedData[j]
                    player2 = speedData[j + 1]
                    if (player1.getAGI() < player2.getAGI()) {
                        val box: Player? = speedData[j]
                        speedData[j] = speedData[j + 1]
                        if (box != null) {
                            speedData[j + 1] = box
                        }
                    }
            }
        }

        for (i in speedData) { // membersに速さ順に格納
            player = i
            pt.setMembers(player)
        }

        return speedData
    }

    // 職業を数字から文字に変換する
    private fun occupationConversion(jobValue: Int): String {

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
    private fun makeEnemyCharacter(enemyPartyList: CharacterAllData, id: Int): Player {

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
        enemy.setIdNumber(id)

        return enemy
    }

    // 味方キャラクターを作成する
    private fun makeAllyCharacter(allyPartyList: CharacterAllData, id: Int): Player {

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
        ally.setIdNumber(id)

        return ally
    }

    private fun statusLog(
        ally01: Player,
        ally02: Player,
        ally03: Player,
        enemy01: Player,
        enemy02: Player,
        enemy03: Player
    ) {

        myCallBack?.upDateAllyStatus(ally01, ally02, ally03)
        myCallBack?.upDateEnemyStatus(enemy01, enemy02, enemy03)

    }

    fun  getParty01():List<Player>{

        return party01

    }

    fun getParty02(): List<Player>{

        return party02
    }

    fun sendData() {

        val charaData = CharacterData.getInstance()
        charaData.ally01 = ally01
        charaData.ally02 = ally02
        charaData.ally03 = ally03
        charaData.enemy01 = enemy01
        charaData.enemy02 = enemy02
        charaData.enemy03 = enemy03
    }

}