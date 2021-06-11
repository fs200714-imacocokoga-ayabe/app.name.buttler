package com.e.app_namebattler.controller

import com.e.app_namebattler.view.party.job.*
import com.e.app_namebattler.view.party.party.Party
import com.e.app_namebattler.view.party.player.CharacterAllData
import com.e.app_namebattler.view.party.player.CharacterData
import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.strategy.*
import com.e.app_namebattler.view.strategy.enemystrategy.StrategyEnemyAttackPatternByJob

class GameManager {

    private val party = Party()
    var context: Context? = null

    private var battleLogList: MutableList<Any> = ArrayList()
    private var ally01StatusLogList: MutableList<String> = ArrayList()
    private var ally02StatusLogList: MutableList<String> = ArrayList()
    private var ally03StatusLogList: MutableList<String> = ArrayList()
    private var enemy01StatusLogList: MutableList<String> = ArrayList()
    private var enemy02StatusLogList: MutableList<String> = ArrayList()
    private var enemy03StatusLogList: MutableList<String> = ArrayList()

    private val characterNumber = 3

    var callBack: BattleLogListener? = null

    private var attackerList: MutableList<Player> = ArrayList() //攻撃するキャラクターを格納

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
        speedReordering(enemy01, enemy02, enemy03, ally01, ally02, ally03)
        // パーティの振り分け
        party.appendPlayer(enemy01, enemy02, enemy03, ally01, ally02, ally03)

        // キャラクターの初期ステータスの取得
        ally01StatusLogList = getCharacterStatusLogList(ally01)
        ally02StatusLogList = getCharacterStatusLogList(ally02)
        ally03StatusLogList = getCharacterStatusLogList(ally03)
        enemy01StatusLogList = getCharacterStatusLogList(enemy01)
        enemy02StatusLogList = getCharacterStatusLogList(enemy02)
        enemy03StatusLogList = getCharacterStatusLogList(enemy03)

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
    fun battle(allyStrategyNumber: Int) {

        attackerList.clear()
        battleLogList.clear()
        ally01StatusLogList.clear()
        ally02StatusLogList.clear()
        ally03StatusLogList.clear()
        enemy01StatusLogList.clear()
        enemy02StatusLogList.clear()
        enemy03StatusLogList.clear()

        // 行動するキャラクターattackListに格納
        for (i in 1..party.getMembers().size) {
            attackerList.add(party.getMembers()[i - 1])
        }

        for (i in 1..attackerList.size) { // attackに格納したplayerが全員行動する

            player = attackerList[i - 1] // 攻撃リストから呼び出し

            if (player.isLive) {// player1のHPが0より大きい場合

                if (player.isMark) { // player1が味方の場合

                    battleLogList.plusAssign(selectStrategyNumber(allyStrategyNumber))

                } else {

                    battleLogList.plusAssign(selectStrategyNumber(enemyStrategyNumber))
                }

                // キャラクターステータスの取得
                ally01StatusLogList.plusAssign(getCharacterStatusLogList(ally01))
                ally02StatusLogList.plusAssign(getCharacterStatusLogList(ally02))
                ally03StatusLogList.plusAssign(getCharacterStatusLogList(ally03))
                enemy01StatusLogList.plusAssign(getCharacterStatusLogList(enemy01))
                enemy02StatusLogList.plusAssign(getCharacterStatusLogList(enemy02))
                enemy03StatusLogList.plusAssign(getCharacterStatusLogList(enemy03))
            }

            // 敗北判定
            judgment()

            // どちらかのパーティが全滅した場合処理を抜ける
            if (party.getParty01().isEmpty() || party.getParty02().isEmpty()) {
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
    }

    // 選んだ作戦番号から対象プレイヤーと作戦を得て返す
    private fun selectStrategyNumber(number: Int): StringBuilder {
        when (number) {
            0 -> context = Context(StrategyNormalAttack())
            1 -> context = Context(StrategyUseMagic())
            2 -> context = Context(StrategySkillAttack())
            3 -> context = Context(StrategyUseHealingMagic())
            4 -> context = Context(StrategyUseHerb())
            5 -> context = Context(StrategyEnemyAttackPatternByJob())
        }
        return context?.attackStrategy(player, party.getParty01(),
            party.getParty02())!!
    }

    // 敗北判定の処理
    private fun judgment() {

        for (i in attackerList) {

            if (i.hp <= 0) {
                party.removePlayer(i)
                party.removeMembers(i)
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
            party.setMembers(player)
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

        character.setMaxHp(character.hp)
        character.setMaxMp(character.mp)
        character.isPoison = false
        character.isParalysis = false
        character.setIdNumber(id)
        character.setCharacterImageType(characterPartyList.character_image)
        character.setPrintStatusEffect(0)
        character.setSoundStatusEffect(0)
        character.setAttackSoundEffect(0)

        // idが3より小さい(敵キャラクター: false) idが3以上(味方キャラクター: true)　
        character.isMark = characterNumber <= id
        return character
    }

    fun getAllyParty(): List<Player> {
        return party.getParty01()
    }

    fun getEnemyParty(): List<Player> {
        return party.getParty02()
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
    private fun occupationConversion(jobValue: String): Int {

        var job = 0

        when (jobValue) {
            JobData.FIGHTER.getJobName() -> job = JobData.FIGHTER.getJobNumber()
            JobData.WIZARD.getJobName() -> job = JobData.WIZARD.getJobNumber()
            JobData.PRIEST.getJobName() -> job = JobData.PRIEST.getJobNumber()
            JobData.NINJA.getJobName() -> job = JobData.NINJA.getJobNumber()
        }
        return job
    }

    private fun getCharacterStatusLogList(character: Player): MutableList<String> {

        val characterStatusLogList: MutableList<String> = ArrayList()

        characterStatusLogList.plusAssign(character.getHP().toString())
        characterStatusLogList.plusAssign(character.getMaxHp().toString())
        characterStatusLogList.plusAssign(character.getMP().toString())
        characterStatusLogList.plusAssign(character.getMaxMp().toString())
        characterStatusLogList.plusAssign(character.getPoison())
        characterStatusLogList.plusAssign(character.getParalysis())
        characterStatusLogList.plusAssign(character.getPrintStatusEffect().toString())
        characterStatusLogList.plusAssign(character.getSoundStatusEffect().toString())
        characterStatusLogList.plusAssign(character.getAttackSoundEffect().toString())

        character.setPrintStatusEffect(0)
        character.setSoundStatusEffect(0)
        character.setAttackSoundEffect(0)

        return characterStatusLogList
    }
}
