package com.e.app_namebattler.view.strategy.enemystrategy

import com.e.app_namebattler.view.party.player.Player

class EnemyNinjaAttackPattern : StrategyEnemyAttackPatternByJob() {

    override fun attackStrategy(
        player1: Player,
        party1: List<Player>,
        party2: List<Player>
    ): StringBuilder {

        this.cpuPlayer = player1
        this.userParty.addAll(party1)
        this.cpuParty.addAll(party2)
        this.userPlayer = selectLowerHp() // HPの低い相手を呼び出す

        when {
            // 毒状態の場合
            cpuPlayer.isPoison -> {
                battleLog.append(cpuPlayer.eat())
            }

            // 攻撃力の1/2が相手の防御力より大きい場合
            cpuPlayer.str / 2 > userPlayer.def -> {
                battleLog.append(cpuPlayer.normalAttack(userPlayer))
            }

            // 魔法を使う
           ninjaUseMagicMp <= cpuPlayer.mp -> { // MPが10以上ある場合
                battleLog.append(cpuPlayer.magicAttack(userPlayer))
            }

            else -> {
                battleLog.append(cpuPlayer.skillAttack(userPlayer))
            }
        }
        cpuParty.clear()
        userParty.clear()
        return battleLog // バトルログを返す
    }
}

/*
Ninjaの攻撃優先順
1:通常攻撃　2:魔法攻撃 3:スキル攻撃
毒状態の場合:eat()が優先される
 */