package com.e.app_namebattler.controller

import com.e.app_namebattler.view.party.job.*
import com.e.app_namebattler.view.party.party.Party
import com.e.app_namebattler.view.party.player.CharacterAllData
import com.e.app_namebattler.view.party.player.CharacterData
import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.strategy.*

class GameManager {

    private val pt = Party()
    var context: Context? = null

    private var battleLogList:MutableList<Any> = ArrayList()
    private var ally01StatusLogList:MutableList<String> = ArrayList()
    private var ally02StatusLogList:MutableList<String> = ArrayList()
    private var ally03StatusLogList:MutableList<String> = ArrayList()
    private var enemy01StatusLogList:MutableList<String> = ArrayList()
    private var enemy02StatusLogList:MutableList<String> = ArrayList()
    private var enemy03StatusLogList:MutableList<String> = ArrayList()

    var myCallBack: BattleLogListener? = null

    private lateinit var speedOrderList: List<Player>// 速さ順キャラクターを格納

    private var party01: MutableList<Player> = ArrayList() // BattleMainActivityからの呼び出しに使用
    private val party02: MutableList<Player> = ArrayList() // BattleMainActivityからの呼び出しに使用
    private var members01: MutableList<Player> = ArrayList() // プレイヤーの入れ物
    private var attackList: MutableList<Player> = ArrayList() //攻撃するキャラクターを格納

    lateinit var ally01: Player // 味方キャラクターを格納
    lateinit var ally02: Player
    lateinit var ally03: Player
    lateinit var enemy01: Player // 敵キャラクターを格納
    lateinit var enemy02: Player
    lateinit var enemy03: Player
    lateinit var character: Player
    private lateinit var player: Player // キャラクターを格納
    private lateinit var player1: Player
    private lateinit var player2: Player

    private var enemyStrategyNumber = 0 // 作戦の選択に使用

    // Activityから指揮権を受け取る
    fun controlTransfer(
        allyPartyList: ArrayList<CharacterAllData>,
        enemyPartyList: ArrayList<CharacterAllData>
    ) {

        // 敵キャラクターを作成する
        enemy01 = makeCharacter(enemyPartyList[0], 0)
        enemy02 = makeCharacter(enemyPartyList[1], 1)
        enemy03 = makeCharacter(enemyPartyList[2], 2)

        // 味方キャラクターを作成する
        ally01 = makeCharacter(allyPartyList[0], 3)
        ally02 = makeCharacter(allyPartyList[1], 4)
        ally03 = makeCharacter(allyPartyList[2], 5)

        // スピード順に取得する
        speedOrderList = speedReordering(enemy01, enemy02, enemy03, ally01, ally02, ally03)
        // パーティの振り分け
        pt.appendPlayer(enemy01, enemy02, enemy03, ally01, ally02, ally03)
        // キャラクターの表示
        myCallBack?.upDateAllyStatus(ally01, ally02, ally03)
        myCallBack?.upDateEnemyStatus(enemy01, enemy02, enemy03)

    }

    // 戦闘処理
    fun battle(strategyNumber: Int) {

        attackList.clear()
        battleLogList.clear()
        ally01StatusLogList.clear()
        ally02StatusLogList.clear()
        ally03StatusLogList.clear()
        enemy01StatusLogList.clear()
        enemy02StatusLogList.clear()
        enemy03StatusLogList.clear()

        // 行動するキャラクターattackListに格納
        for (i in 1..pt.getMembers().size) {
            attackList.add(pt.getMembers()[i - 1])
        }

        for (i in 1..attackList.size) { // attackに格納したplayerが全員行動する

            player = attackList[i - 1] // 攻撃リストから呼び出し

            if (player.isLive) {// player1のHPが0より大きい場合

                if (player.isMark) { // player1が味方の場合

                    //battleLog.append(selectStrategyNumber(strategyNumber))
                    battleLogList.plusAssign(selectStrategyNumber(strategyNumber))

                } else {
                    enemyStrategyNumber = (0..4).random()// 敵の作戦ランダム
                   // battleLog.append(selectStrategyNumber(enemyStrategyNumber))
                    battleLogList.plusAssign(selectStrategyNumber(enemyStrategyNumber))
                }

               // battleLog.append("@@")// playerごとのログを@@で分けるために加える

                ally01StatusLogList.plusAssign(ally01.getHP().toString())
                ally01StatusLogList.plusAssign(ally01.getMaxHp().toString())
                ally01StatusLogList.plusAssign(ally01.getMP().toString())
                ally01StatusLogList.plusAssign(ally01.getMaxMp().toString())
                ally01StatusLogList.plusAssign(ally01.getPoison())
                ally01StatusLogList.plusAssign(ally01.getParalysis())

                ally02StatusLogList.plusAssign(ally02.getHP().toString())
                ally02StatusLogList.plusAssign(ally02.getMaxHp().toString())
                ally02StatusLogList.plusAssign(ally02.getMP().toString())
                ally02StatusLogList.plusAssign(ally02.getMaxMp().toString())
                ally02StatusLogList.plusAssign(ally02.getPoison())
                ally02StatusLogList.plusAssign(ally02.getParalysis())

                ally03StatusLogList.plusAssign(ally03.getHP().toString())
                ally03StatusLogList.plusAssign(ally03.getMaxHp().toString())
                ally03StatusLogList.plusAssign(ally03.getMP().toString())
                ally03StatusLogList.plusAssign(ally03.getMaxMp().toString())
                ally03StatusLogList.plusAssign(ally03.getPoison())
                ally03StatusLogList.plusAssign(ally03.getParalysis())

                enemy01StatusLogList.plusAssign(enemy01.getHP().toString())
                enemy01StatusLogList.plusAssign(enemy01.getMaxHp().toString())
                enemy01StatusLogList.plusAssign(enemy01.getMP().toString())
                enemy01StatusLogList.plusAssign(enemy01.getMaxMp().toString())
                enemy01StatusLogList.plusAssign(enemy01.getPoison())
                enemy01StatusLogList.plusAssign(enemy01.getParalysis())

                enemy02StatusLogList.plusAssign(enemy02.getHP().toString())
                enemy02StatusLogList.plusAssign(enemy02.getMaxHp().toString())
                enemy02StatusLogList.plusAssign(enemy02.getMP().toString())
                enemy02StatusLogList.plusAssign(enemy02.getMaxMp().toString())
                enemy02StatusLogList.plusAssign(enemy02.getPoison())
                enemy02StatusLogList.plusAssign(enemy02.getParalysis())

                enemy03StatusLogList.plusAssign(enemy03.getHP().toString())
                enemy03StatusLogList.plusAssign(enemy03.getMaxHp().toString())
                enemy03StatusLogList.plusAssign(enemy03.getMP().toString())
                enemy03StatusLogList.plusAssign(enemy03.getMaxMp().toString())
                enemy03StatusLogList.plusAssign(enemy03.getPoison())
                enemy03StatusLogList.plusAssign(enemy03.getParalysis())
            }

            // 敗北判定
            judgment()

            // どちらかのパーティが全滅した場合処理を抜ける
            if (pt.getParty1().isEmpty() || pt.getParty2().isEmpty()) {
                break
            }
        }

       // val array = listOfNotNull(battleLog.split("@@"))
       // val array = battleLog.split("@@").toMutableList() //playerごとに分ける
       // val array = battleLog.split("@@") //playerごとに分ける

        println("ろぐ０３${battleLogList.size}")
        println("ろぐ０３${battleLogList}")

            myCallBack?.upDateAllLog(
                battleLogList,
                ally01StatusLogList,
                ally02StatusLogList,
                ally03StatusLogList,
                enemy01StatusLogList,
                enemy02StatusLogList,
                enemy03StatusLogList,
                ally01,
                ally02,
                ally03,
                enemy01,
                enemy02,
                enemy03
            )


        party01.clear()
        party02.clear()
        members01.clear()

        party01.plusAssign(pt.getParty1())
        party02.plusAssign(pt.getParty2())
        members01.plusAssign(pt.getMembers())
    }

    // 選んだ作戦番号から対象プレイヤーと作戦を得て返す
    private fun selectStrategyNumber(number: Int): StringBuilder {
        when (number) {
            0 -> context = Context(StrategyNormalAttack())
            1 -> context = Context(StrategyUseMagic())
            2 -> context = Context(StrategySkillAttack())
            3 -> context = Context(StrategyUseHealingMagic())
            4 -> context = Context(StrategyUseHerb())
        }
        return context?.attackStrategy(player, pt.getParty1(),
            pt.getParty2())!!
    }

    // 敗北判定の処理
    private fun judgment(){
        
        for (i in attackList){

            if (i.hp <= 0) {
                this.speedOrderList -= i
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
    private fun makeCharacter(characterPartyList: CharacterAllData, id: Int): Player {

        when (occupationConversion(characterPartyList.job)) {

            0 -> character = (JobFighter(
                characterPartyList.name,
                characterPartyList.job,
                characterPartyList.hp,
                characterPartyList.mp,
                characterPartyList.str,
                characterPartyList.def,
                characterPartyList.agi,
                characterPartyList.luck
            ))

            1 -> character = (JobWizard(
                characterPartyList.name,
                characterPartyList.job,
                characterPartyList.hp,
                characterPartyList.mp,
                characterPartyList.str,
                characterPartyList.def,
                characterPartyList.agi,
                characterPartyList.luck
            ))

            2 -> character = (JobPriest(
                characterPartyList.name,
                characterPartyList.job,
                characterPartyList.hp,
                characterPartyList.mp,
                characterPartyList.str,
                characterPartyList.def,
                characterPartyList.agi,
                characterPartyList.luck
            ))

            3 -> character = (JobNinja(
                characterPartyList.name,
                characterPartyList.job,
                characterPartyList.hp,
                characterPartyList.mp,
                characterPartyList.str,
                characterPartyList.def,
                characterPartyList.agi,
                characterPartyList.luck
            ))
        }

        // 敵キャラクターの場合
        if (id < 3) {
            character.setMaxHp(character.hp)
            character.setMaxMp(character.mp)
            character.isMark = false
            character.isPoison = false
            character.isParalysis = false
            character.setIdNumber(id)
            character.setCharacterImageType(characterPartyList.character_image)

            // 味方キャラクターの場合
        }else{
            character.setMaxHp(character.hp)
            character.setMaxMp(character.mp)
            character.isMark = true
            character.isPoison = false
            character.isParalysis = false
            character.setIdNumber(id)
            character.setCharacterImageType(characterPartyList.character_image)

        }
        return character
    }

    fun  getParty01():List<Player>{
        return pt.getParty1()
    }

    fun getParty02(): List<Player>{
        return pt.getParty2()
    }

    fun getMembers():List<Player>{
        return pt.getMembers()
    }

    fun setAttackList(attackList: MutableList<Player>) {
        this.attackList = attackList

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
