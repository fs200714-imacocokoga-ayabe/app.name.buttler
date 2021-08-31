package com.e.app_namebattler.view.strategy.enemystrategy

import com.e.app_namebattler.view.party.player.Player

class EnemyWizardAttackPattern : StrategyEnemyAttackPatternByJob() {

    override fun attackStrategy(
        player1: Player,
        party1: List<Player>,
        party2: List<Player>
    ): StringBuilder {

        this.cpuPlayer = player1
        this.userParty.addAll(party1)
        this.cpuParty.addAll(party2)
        userPlayer = selectLowerHp() // HPの低い相手を呼び出す

        // 毒状態の場合
        when {
            cpuPlayer.isPoison -> {
                battleLog.append(cpuPlayer.eat())
            }

            // 攻撃力の1/2より相手の防御力の方が大きい場合
            userPlayer.def < cpuPlayer.str / 2 -> {
                battleLog.append(cpuPlayer.skillAttack(userPlayer))
            }

            else -> {
                battleLog.append(cpuPlayer.magicAttack(userPlayer))
            }
        }
        cpuParty.clear()
        userParty.clear()
        return battleLog // バトルログを返す
    }
}