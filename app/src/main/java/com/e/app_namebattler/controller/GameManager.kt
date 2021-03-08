package com.e.app_namebattler.controller

import android.os.Handler
import com.e.app_namebattler.view.party.job.*
import com.e.app_namebattler.view.party.party.Party
import com.e.app_namebattler.view.party.player.CharacterAllData
import com.e.app_namebattler.view.party.player.CharacterData
import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.strategy.*
import java.util.*
import kotlin.collections.ArrayList

class GameManager {

    var random = Random()

    private val pt = Party()
    var context: Context? = null

    private val handler: Handler = Handler()
    private var battleLog = StringBuilder()

    var myCallBack: BattleLogListener? = null

    private lateinit var speedOrderList: List<Player>// 速さ順キャラクターを格納

    private var party01: MutableList<Player> = ArrayList() // BattleMainActivityからの呼び出しに使用
    private val party02: MutableList<Player> = ArrayList() // BattleMainActivityからの呼び出しに使用
    private var attackList: MutableList<Player> = ArrayList() //攻撃するキャラクターを格納

    lateinit var ally: Player
    private lateinit var enemy: Player
    lateinit var ally01: Player // 味方キャラクターを格納
    lateinit var ally02: Player
    lateinit var ally03: Player
    lateinit var enemy01: Player // 敵キャラクターを格納
    lateinit var enemy02: Player
    lateinit var enemy03: Player
    private lateinit var player: Player // キャラクターを格納
    private lateinit var player1: Player
    private lateinit var player2: Player

    var imageType = 0 // キャラクターの外観に使用

    private var enemyStrategyNumber = 0 // 作戦の選択に使用

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
        // パーティの振り分け
        pt.appendPlayer(enemy01, enemy02, enemy03, ally01, ally02, ally03)
        // キャラクターの表示
        statusLog(ally01, ally02, ally03, enemy01, enemy02, enemy03)
    }

    // 戦闘処理
    fun battle(strategyNumber: Int) {

        attackList.clear()

        // 行動するキャラクターattackListに格納
        for (i in 1..pt.getMembers().size) {
            attackList.add(pt.getMembers()[i - 1])
        }

        for (i in 1..attackList.size) { // attackに格納したplayerが全員行動する

            player1 = attackList[i - 1] // 攻撃リストから呼び出し

            if (player1.isLive) {// player1のHPが0より大きい場合

                if (player1.isParalysis){// 麻痺している場合

                    battleLog.append("${player1.getName()}は麻痺で動けない！！\n")

                }else {// 麻痺していない場合

                    if (player1.isMark) { // player1が味方の場合
                        battleLog.append(selectStrategyNumber(strategyNumber))

                    } else {
                        enemyStrategyNumber = (0..4).random()// 敵の作戦ランダム
                        battleLog.append(selectStrategyNumber(enemyStrategyNumber))

                    }
                }

                battleLog.append("@@")// playerごとのログを@@で分けるために加える
            }

            // 敗北判定
            judgment()

            // どちらかのパーティが全滅した場合処理を抜ける
            if (pt.getParty1().isEmpty() || pt.getParty2().isEmpty()) {
                break
            }
        }

        val array = battleLog.split("@@") //playerごとに分ける

        myCallBack?.upDateBattleLog(array) //BattleLogListenerを通してBattleMainActivityにarrayを送る

        // キャラクターの表示
        statusLog(ally01, ally02, ally03, enemy01, enemy02, enemy03)

      //  allLog(ally01, ally02, ally03, enemy01, enemy02, enemy03, array)

        party01.clear()
        party02.clear()

        party01.plusAssign(pt.getParty1())
        party02.plusAssign(pt.getParty2())
        battleLog.clear()

    }

//    private fun allLog(ally01: Player, ally02: Player, ally03: Player, enemy01: Player, enemy02: Player, enemy03: Player, array: List<String>) {
//
//        myCallBack?.upAllLog(ally01, ally02, ally03, enemy01, enemy02, enemy03, array)
//    }

    // 選んだ作戦番号から対象プレイヤーと作戦を得て返す
    private fun selectStrategyNumber(number: Int): StringBuilder {
        when (number) {
            0 -> context = Context(StrategyNormalAttack())
            1 -> context = Context(StrategyUseMagic())
            2 -> context = Context(StrategySkillAttack())
            3 -> context = Context(StrategyUseHealingMagic())
            4 -> context = Context(StrategyUseHerb())
        }
        return context?.attackStrategy(player1, pt.getParty1(),
            pt.getParty2())!!
    }


    // 敗北判定の処理
    private fun judgment(){
        
        for (i in attackList){

            if (i.hp <= 0) {
                speedOrderList -= i
                pt.removePlayer(i)
                pt.removeMembers(i)
            }
        }
    }

    // 速さ順に並び処理
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

        for (i in 0 until speedData.size - 1) { // 速さ順の並び変える処理
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

    // 敵キャラクターを作成する
    private fun makeEnemyCharacter(enemyPartyList: CharacterAllData, id: Int): Player {

        when (occupationConversion(enemyPartyList.job)) {

            0 -> enemy = (JobFighter(
                enemyPartyList.name,
                enemyPartyList.job,
                enemyPartyList.hp,
                enemyPartyList.mp,
                enemyPartyList.str,
                enemyPartyList.def,
                enemyPartyList.agi,
                enemyPartyList.luck
            ))

            1 -> enemy = (JobWizard(
                enemyPartyList.name,
                enemyPartyList.job,
                enemyPartyList.hp,
                enemyPartyList.mp,
                enemyPartyList.str,
                enemyPartyList.def,
                enemyPartyList.agi,
                enemyPartyList.luck
            ))

            2 -> enemy = (JobPriest(
                enemyPartyList.name,
                enemyPartyList.job,
                enemyPartyList.hp,
                enemyPartyList.mp,
                enemyPartyList.str,
                enemyPartyList.def,
                enemyPartyList.agi,
                enemyPartyList.luck
            ))

            3 -> enemy = (JobNinja(
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
        enemy.isMark = false
        enemy.isPoison = false
        enemy.isParalysis = false
        enemy.setIdNumber(id)
        enemy.setCharacterImageType(enemyPartyList.character_image)

        return enemy
    }

    // 味方キャラクターを作成する
    private fun makeAllyCharacter(allyPartyList: CharacterAllData, id: Int): Player {

        when (occupationConversion(allyPartyList.job)) {

            0 -> ally = (JobFighter(
                allyPartyList.name,
                allyPartyList.job,
                allyPartyList.hp,
                allyPartyList.mp,
                allyPartyList.str,
                allyPartyList.def,
                allyPartyList.agi,
                allyPartyList.luck
            ))

            1 -> ally = (JobWizard(
                allyPartyList.name,
                allyPartyList.job,
                allyPartyList.hp,
                allyPartyList.mp,
                allyPartyList.str,
                allyPartyList.def,
                allyPartyList.agi,
                allyPartyList.luck
            ))

            2 -> ally = (JobPriest(
                allyPartyList.name,
                allyPartyList.job,
                allyPartyList.hp,
                allyPartyList.mp,
                allyPartyList.str,
                allyPartyList.def,
                allyPartyList.agi,
                allyPartyList.luck
            ))

            3 -> ally = (JobNinja(
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
        ally.isMark = true
        ally.isPoison = false
        ally.isParalysis = false
        ally.setIdNumber(id)
        ally.setCharacterImageType(allyPartyList.character_image)

        return ally
    }

    // BattleLogListenerを通してBattleMainActivityのメソッドを使用
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

    // データをcharaDataに保存
    fun sendData() {

        val charaData = CharacterData.getInstance()
        charaData.ally01 = ally01
        charaData.ally02 = ally02
        charaData.ally03 = ally03
        charaData.enemy01 = enemy01
        charaData.enemy02 = enemy02
        charaData.enemy03 = enemy03
    }

    // 数字を職業に変換
    private fun occupationConversion(jobValue: String): Int{

        var job = 0

        when(jobValue){
            JobData.FIGHTER.getJobName() -> job = JobData.FIGHTER.getJobNumber()
            JobData.WIZARD.getJobName() -> job = JobData.WIZARD.getJobNumber()
            JobData.PRIEST.getJobName()-> job = JobData.PRIEST.getJobNumber()
            JobData.NINJA.getJobName() -> job = JobData.NINJA.getJobNumber()
        }
        return job
    }
}
