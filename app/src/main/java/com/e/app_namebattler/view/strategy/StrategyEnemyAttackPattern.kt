package com.e.app_namebattler.view.strategy

import com.e.app_namebattler.view.party.job.JobData
import com.e.app_namebattler.view.party.player.Player

class StrategyEnemyAttackPattern: BaseStrategy() {

    private var userParty: MutableList<Player> = ArrayList()
    private var cpuParty: MutableList<Player> = ArrayList()
    private lateinit var userPlayer: Player
    private lateinit var cpuPlayer: Player
    private lateinit var cpuPlayer2: Player

    override fun attackStrategy(
        player1: Player,
        party1: List<Player>,
        party2: List<Player>
    ): StringBuilder {

        battleLog.clear()
        userParty.clear()
        cpuParty.clear()

        cpuPlayer = player1

        userParty.addAll(party1) // party1を入れる
        cpuParty.addAll(party2) // party2を入れる

        when (player1.job) {
            JobData.FIGHTER.getJobName() -> {
                fighterAi()
            }
            JobData.WIZARD.getJobName() -> {
                wizardAi()
            }
            JobData.PRIEST.getJobName() -> {
                priestAi()
            }
            JobData.NINJA.getJobName() -> {
                ninjaAi()
            }
        }
        return battleLog // バトルログを返す
    }

    private fun fighterAi(): StringBuilder {

        userPlayer = selectLowerHp() // HPの低い相手を呼び出す

        when {
            // 毒状態の場合
            cpuPlayer.isPoison -> {
                battleLog.append(cpuPlayer.eatHerb())
            }

            // 攻撃力の1/2が相手の防御力より大きい場合
            cpuPlayer.str / 2 > userPlayer.def -> {
                battleLog.append(cpuPlayer.normalAttack(userPlayer))
            }

            // 防御力の方が大きい場合
            else -> {
                battleLog.append(cpuPlayer.skillAttack(userPlayer))
            }
        }
        return battleLog
    }

    private fun wizardAi(): StringBuilder {

        userPlayer = selectLowerHp() // HPの低い相手を呼び出す

        // 毒状態の場合
        when {
            cpuPlayer.isPoison -> {
                battleLog.append(cpuPlayer.eatHerb())
            }

            // 攻撃力の1/2より相手の防御力の方が大きい場合
            userPlayer.def < cpuPlayer.str / 2 -> {
                battleLog.append(cpuPlayer.skillAttack(userPlayer))
            }

            else -> {
                battleLog.append(cpuPlayer.magicAttack(userPlayer))
            }
        }
        return battleLog
    }

    private fun priestAi(): StringBuilder {

        cpuPlayer2 = lifeImportance() // HPの低い味方を入れる
        userPlayer = selectHighHp() // HPの高い相手を入れる

        when {

            cpuPlayer.isPoison -> { // playerが毒状態の場合
                battleLog.append(cpuPlayer.eatHerb())
            }

            // HPが最大HPの1/4より小さい場合
            cpuPlayer2.hp < cpuPlayer2.getMaxHp() / 4 -> {

                if (20 <= cpuPlayer.mp) {
                    battleLog.append(cpuPlayer.healingMagic(cpuPlayer2))
                }else{
                    battleLog.append(cpuPlayer.healingMagic(userPlayer))
                }
            }

            // 魔法を使う
             10 <= cpuPlayer.mp -> {
                battleLog.append(cpuPlayer.magicAttack(userPlayer))
            }

            // 攻撃力の1/2より相手の防御力の方が大きい場合
             userPlayer.def < cpuPlayer.str / 2 -> {
                userPlayer = selectLowerHp() // HPの低い相手を呼び出す
                battleLog.append(cpuPlayer.normalAttack(userPlayer))
            }

            else -> {
                battleLog.append(cpuPlayer.skillAttack(userPlayer))
            }
        }
        return battleLog
    }

    private fun ninjaAi(): StringBuilder {

        userPlayer = selectLowerHp() // HPの低い相手を呼び出す

        when {
            // 毒状態の場合
            cpuPlayer.isPoison -> {
                battleLog.append(cpuPlayer.eatHerb())
            }

            // 攻撃力の1/2が相手の防御力より大きい場合
            cpuPlayer.str / 2 > userPlayer.def -> {
                battleLog.append(cpuPlayer.normalAttack(userPlayer))
            }

            // 魔法を使う
            10 <= cpuPlayer.mp -> { // MPが10以上ある場合
                battleLog.append(cpuPlayer.magicAttack(userPlayer))
            }

            else -> {
                battleLog.append(cpuPlayer.skillAttack(userPlayer))
            }
        }
        return battleLog
    }

    /**
     * HPの低い相手を選んで返す
     * @return userPlayer : HPの低い相手
     */
    private fun selectLowerHp(): Player {

        userPlayer = userParty[(1.. userParty.size).random() - 1] // 敵パーティから1人userPlayerに入れる
        println("ろぐろぐ０４${userParty[0]}")
        println("ろぐろぐ01${userParty[(1.. userParty.size).random() - 1]}")
        println("ろぐろぐ02$userPlayer")

        //   userPlayer = userParty[0] // 敵パーティから1人userPlayerに入れる

        for (i in 1 until userParty.size) {
            if (userParty[i].hp < userPlayer.hp) { // userPlayerよりHPが低い場合
                userPlayer = userParty[i] // HPのひくい敵をuserPlayerに入れる
            }
        }
        userParty.clear()
        return userPlayer
    }

    /**
     * HPの高い相手を選んで返す
     * @return userPlayer :HPの高い相手
     */
    private fun selectHighHp(): Player {
println("ろぐろぐ０３${userParty[0]}")
        userPlayer = userParty[(1.. userParty.size).random() - 1] // 敵パーティから1人userPlayerに入れる
       // userPlayer = userParty[0] // 敵パーティから1人userPlayerに入れる
        println("ろぐろぐ03${userParty[(1.. userParty.size).random() - 1]}")
        println("ろぐろぐ04$userPlayer")

        for (i in 1 until userParty.size) {
            if (userParty[i].hp > userPlayer.hp) { // userPlayerよりHPが大きい場合
                userPlayer = userParty[i] // HPの大きい相手をuserPlayerに入れる
            }
        }
        userParty.clear()
        return userPlayer
    }

    /**
     * HPの低い味方を返す
     * @return cpuPlayer2:HPの低い味方
     */
    private fun lifeImportance(): Player {

        cpuPlayer2 = cpuPlayer

        for (i in cpuParty.indices) {
            if (cpuParty[i].hp < cpuPlayer.hp) {
                cpuPlayer2 = cpuParty[i]
            }
        }
        cpuParty.clear()
        return cpuPlayer2
    }
}
