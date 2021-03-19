package com.e.app_namebattler.controller

import android.os.Handler
import com.e.app_namebattler.view.party.job.*
import com.e.app_namebattler.view.party.party.Party
import com.e.app_namebattler.view.party.player.CharacterAllData
import com.e.app_namebattler.view.party.player.CharacterData
import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.strategy.*

class GameManager {

    private val pt = Party()
    var context: Context? = null

    private val handler: Handler = Handler()
    private var battleLog = StringBuilder()

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
        speedOrderList = (speedReordering(enemy01, enemy02, enemy03, ally01, ally02, ally03))
        // パーティの振り分け
        pt.appendPlayer(enemy01, enemy02, enemy03, ally01, ally02, ally03)
        // キャラクターの表示
        statusLog(ally01, ally02, ally03, enemy01, enemy02, enemy03)
    }

    // 戦闘処理
    fun battle(num: Int, strategyNumber: Int) {

        player1 = attackList[num - 1] // 攻撃リストから呼び出し

        if (player1.isLive) {// player1のHPが0より大きい場合

            if (player1.isMark) { // player1が味方の場合
                battleLog.append(selectStrategyNumber(strategyNumber))

            } else {
                enemyStrategyNumber = (0..4).random()// 敵の作戦ランダム
                battleLog.append(selectStrategyNumber(enemyStrategyNumber))
                }
            }
        // 敗北判定
        judgment()

        myCallBack?.upDateBattleLog(battleLog) //BattleLogListenerを通してBattleMainActivityにarrayを送る

        // キャラクターの表示
        statusLog(ally01, ally02, ally03, enemy01, enemy02, enemy03)

        party01.clear()
        party02.clear()
        members01.clear()

        party01.plusAssign(pt.getParty1())
        party02.plusAssign(pt.getParty2())
        members01.plusAssign(pt.getMembers())
        battleLog.clear()
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
