package com.e.app_namebattler.view.strategy.enemystrategy

import com.e.app_namebattler.view.party.player.Player

class EnemyPriestAttackPattern : StrategyEnemyAttackPatternByJob() {

    override fun attackStrategy(
        player1: Player,
        party1: List<Player>,
        party2: List<Player>
    ): StringBuilder {

        this.cpuPlayer = player1
        this.userParty.addAll(party1)
        this.cpuParty.addAll(party2)
        cpuPlayer2 = lifeImportance() // HPの低い味方を入れる
        userPlayer = selectHighHp() // HPの高い相手を入れる

        when {

            cpuPlayer.isPoison -> { // playerが毒状態の場合
                battleLog.append(cpuPlayer.eat())
            }

            // HPが最大HPの1/4より小さい場合
            cpuPlayer2.hp < cpuPlayer2.getMaxHp() / 4 -> {

                if (20 <= cpuPlayer.mp) {
                    battleLog.append(cpuPlayer.healingMagic(cpuPlayer2))
                } else {
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
        cpuParty.clear()
        userParty.clear()
        return battleLog // バトルログを返す
    }
}

