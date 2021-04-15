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

    var callBack: BattleLogListener? = null

  //  private lateinit var speedOrderList: List<Player>// 速さ順キャラクターを格納

//    private var party01: MutableList<Player> = ArrayList() // BattleMainActivityからの呼び出しに使用
//    private val party02: MutableList<Player> = ArrayList() // BattleMainActivityからの呼び出しに使用
//    private var members01: MutableList<Player> = ArrayList() // プレイヤーの入れ物
    private var attackList: MutableList<Player> = ArrayList() //攻撃するキャラクターを格納

    private lateinit var ally01: Player // 味方キャラクターを格納
    private lateinit var ally02: Player
    private lateinit var ally03: Player
    private lateinit var enemy01: Player // 敵キャラクターを格納
    private lateinit var enemy02: Player
    private lateinit var enemy03: Player
    private lateinit var character: Player
    private lateinit var player: Player // キャラクターを格納
    private lateinit var player1: Player
    private lateinit var player2: Player

    private val enemyStrategyNumber = 5 // 作戦の選択に使用

    // Activityから指揮権を受け取る
    fun controlTransfer(
        allyPartyList: ArrayList<CharacterAllData>,
        enemyPartyList: ArrayList<CharacterAllData>
    ) {

        // 敵キャラクターを作成する
        enemy01 = rebirthCharacter(enemyPartyList[0], 0)
        enemy02 = rebirthCharacter(enemyPartyList[1], 1)
        enemy03 = rebirthCharacter(enemyPartyList[2], 2)

        // 味方キャラクターを作成する
        ally01 = rebirthCharacter(allyPartyList[0], 3)
        ally02 = rebirthCharacter(allyPartyList[1], 4)
        ally03 = rebirthCharacter(allyPartyList[2], 5)

        // スピード順に取得する
       // speedOrderList = speedReordering(enemy01, enemy02, enemy03, ally01, ally02, ally03)
        speedReordering(enemy01, enemy02, enemy03, ally01, ally02, ally03)
        // パーティの振り分け
        pt.appendPlayer(enemy01, enemy02, enemy03, ally01, ally02, ally03)

        // キャラクターの初期ステータスの取得
        ally01StatusLogList = getAlly01StatusLogList(ally01)
        ally02StatusLogList = getAlly02StatusLogList(ally02)
        ally03StatusLogList = getAlly03StatusLogList(ally03)
        enemy01StatusLogList = getEnemy01StatusLogList(enemy01)
        enemy02StatusLogList = getEnemy02StatusLogList(enemy02)
        enemy03StatusLogList = getEnemy03StatusLogList(enemy03)

        // キャラクターの表示
        callBack?.upDateInitialStatus(
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

                    battleLogList.plusAssign(selectStrategyNumber(strategyNumber))

                } else {
                  //  enemyStrategyNumber = (0..4).random()// 敵の作戦ランダム

                    battleLogList.plusAssign(selectStrategyNumber(enemyStrategyNumber))
                }

                // キャラクターステータスの取得
                ally01StatusLogList = getAlly01StatusLogList(ally01)
                ally02StatusLogList = getAlly02StatusLogList(ally02)
                ally03StatusLogList = getAlly03StatusLogList(ally03)
                enemy01StatusLogList = getEnemy01StatusLogList(enemy01)
                enemy02StatusLogList = getEnemy02StatusLogList(enemy02)
                enemy03StatusLogList = getEnemy03StatusLogList(enemy03)

            }

            // 敗北判定
            judgment()

            // どちらかのパーティが全滅した場合処理を抜ける
            if (pt.getParty1().isEmpty() || pt.getParty2().isEmpty()) {
                break
            }
        }

        // ステータスとログの表示
            callBack?.upDateAllLog(
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

//        party01.clear()
//        party02.clear()
//        members01.clear()
//
//        party01.plusAssign(pt.getParty1())
//        party02.plusAssign(pt.getParty2())
//        members01.plusAssign(pt.getMembers())
    }

    // 選んだ作戦番号から対象プレイヤーと作戦を得て返す
    private fun selectStrategyNumber(number: Int): StringBuilder {
        when (number) {
            0 -> context = Context(StrategyNormalAttack())
            1 -> context = Context(StrategyUseMagic())
            2 -> context = Context(StrategySkillAttack())
            3 -> context = Context(StrategyUseHealingMagic())
            4 -> context = Context(StrategyUseHerb())
            5 -> context = Context(StrategyEnemyAttackPattern())
        }
        return context?.attackStrategy(player, pt.getParty1(),
            pt.getParty2())!!
    }

    // 敗北判定の処理
    private fun judgment(){
        
        for (i in attackList){

            if (i.hp <= 0) {
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
    ) {

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

                if (player1.agi < player2.agi) {
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
    }

    // 敵キャラクターを作成する
    private fun rebirthCharacter(characterPartyList: CharacterAllData, id: Int): Player {

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
            character.setPrintStatusEffect(0)
            character.setStatusEffect(0)
            character.setAttackSoundEffect(0)

            // 味方キャラクターの場合
        }else{
            character.setMaxHp(character.hp)
            character.setMaxMp(character.mp)
            character.isMark = true
            character.isPoison = false
            character.isParalysis = false
            character.setIdNumber(id)
            character.setCharacterImageType(characterPartyList.character_image)
            character.setPrintStatusEffect(0)
            character.setStatusEffect(0)
            character.setAttackSoundEffect(0)

        }
        return character
    }

    fun  getParty01():List<Player>{
        return pt.getParty1()
    }

    fun getParty02(): List<Player>{
        return pt.getParty2()
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

private fun getAlly01StatusLogList(ally01: Player):MutableList<String> {

    ally01StatusLogList.plusAssign(ally01.getHP().toString())
    ally01StatusLogList.plusAssign(ally01.getMaxHp().toString())
    ally01StatusLogList.plusAssign(ally01.getMP().toString())
    ally01StatusLogList.plusAssign(ally01.getMaxMp().toString())
    ally01StatusLogList.plusAssign(ally01.getPoison())
    ally01StatusLogList.plusAssign(ally01.getParalysis())
    ally01StatusLogList.plusAssign(ally01.getPrintStatusEffect().toString())
    ally01StatusLogList.plusAssign(ally01.getStatusEffect().toString())
    ally01StatusLogList.plusAssign(ally01.getAttackSoundEffect().toString())

    ally01.setPrintStatusEffect(0)
    ally01.setStatusEffect(0)
    ally01.setAttackSoundEffect(0)

    return ally01StatusLogList
}

    private fun getAlly02StatusLogList(ally02: Player):MutableList<String> {

        ally02StatusLogList.plusAssign(ally02.getHP().toString())
        ally02StatusLogList.plusAssign(ally02.getMaxHp().toString())
        ally02StatusLogList.plusAssign(ally02.getMP().toString())
        ally02StatusLogList.plusAssign(ally02.getMaxMp().toString())
        ally02StatusLogList.plusAssign(ally02.getPoison())
        ally02StatusLogList.plusAssign(ally02.getParalysis())
        ally02StatusLogList.plusAssign(ally02.getPrintStatusEffect().toString())
        ally02StatusLogList.plusAssign(ally02.getStatusEffect().toString())
        ally02StatusLogList.plusAssign(ally02.getAttackSoundEffect().toString())

        ally02.setPrintStatusEffect(0)
        ally02.setStatusEffect(0)
        ally02.setAttackSoundEffect(0)

        return ally02StatusLogList
    }

    private fun getAlly03StatusLogList(ally03: Player):MutableList<String> {

        ally03StatusLogList.plusAssign(ally03.getHP().toString())
        ally03StatusLogList.plusAssign(ally03.getMaxHp().toString())
        ally03StatusLogList.plusAssign(ally03.getMP().toString())
        ally03StatusLogList.plusAssign(ally03.getMaxMp().toString())
        ally03StatusLogList.plusAssign(ally03.getPoison())
        ally03StatusLogList.plusAssign(ally03.getParalysis())
        ally03StatusLogList.plusAssign(ally03.getPrintStatusEffect().toString())
        ally03StatusLogList.plusAssign(ally03.getStatusEffect().toString())
        ally03StatusLogList.plusAssign(ally03.getAttackSoundEffect().toString())

        ally03.setPrintStatusEffect(0)
        ally03.setStatusEffect(0)
        ally03.setAttackSoundEffect(0)

        return ally03StatusLogList
    }

    private fun getEnemy01StatusLogList(enemy01: Player):MutableList<String> {

        enemy01StatusLogList.plusAssign(enemy01.getHP().toString())
        enemy01StatusLogList.plusAssign(enemy01.getMaxHp().toString())
        enemy01StatusLogList.plusAssign(enemy01.getMP().toString())
        enemy01StatusLogList.plusAssign(enemy01.getMaxMp().toString())
        enemy01StatusLogList.plusAssign(enemy01.getPoison())
        enemy01StatusLogList.plusAssign(enemy01.getParalysis())
        enemy01StatusLogList.plusAssign(enemy01.getPrintStatusEffect().toString())
        enemy01StatusLogList.plusAssign(enemy01.getStatusEffect().toString())
        enemy01StatusLogList.plusAssign(enemy01.getAttackSoundEffect().toString())

        enemy01.setPrintStatusEffect(0)
        enemy01.setStatusEffect(0)
        enemy01.setAttackSoundEffect(0)

        return enemy01StatusLogList
    }

    private fun getEnemy02StatusLogList(enemy02: Player):MutableList<String> {

        enemy02StatusLogList.plusAssign(enemy02.getHP().toString())
        enemy02StatusLogList.plusAssign(enemy02.getMaxHp().toString())
        enemy02StatusLogList.plusAssign(enemy02.getMP().toString())
        enemy02StatusLogList.plusAssign(enemy02.getMaxMp().toString())
        enemy02StatusLogList.plusAssign(enemy02.getPoison())
        enemy02StatusLogList.plusAssign(enemy02.getParalysis())
        enemy02StatusLogList.plusAssign(enemy02.getPrintStatusEffect().toString())
        enemy02StatusLogList.plusAssign(enemy02.getStatusEffect().toString())
        enemy02StatusLogList.plusAssign(enemy02.getAttackSoundEffect().toString())

        enemy02.setPrintStatusEffect(0)
        enemy02.setStatusEffect(0)
        enemy02.setAttackSoundEffect(0)

        return enemy02StatusLogList
    }

    private fun getEnemy03StatusLogList(enemy03: Player):MutableList<String> {

        enemy03StatusLogList.plusAssign(enemy03.getHP().toString())
        enemy03StatusLogList.plusAssign(enemy03.getMaxHp().toString())
        enemy03StatusLogList.plusAssign(enemy03.getMP().toString())
        enemy03StatusLogList.plusAssign(enemy03.getMaxMp().toString())
        enemy03StatusLogList.plusAssign(enemy03.getPoison())
        enemy03StatusLogList.plusAssign(enemy03.getParalysis())
        enemy03StatusLogList.plusAssign(enemy03.getPrintStatusEffect().toString())
        enemy03StatusLogList.plusAssign(enemy03.getStatusEffect().toString())
        enemy03StatusLogList.plusAssign(enemy03.getAttackSoundEffect().toString())

        enemy03.setPrintStatusEffect(0)
        enemy03.setStatusEffect(0)
        enemy03.setAttackSoundEffect(0)

        return enemy03StatusLogList
    }

}
