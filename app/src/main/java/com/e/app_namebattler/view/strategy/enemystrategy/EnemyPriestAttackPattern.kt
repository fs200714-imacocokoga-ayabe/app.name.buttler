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

            // HPが一番低い味方が最大HPの1/4より小さい場合、回復魔法そ使用
            cpuPlayer2.hp < cpuPlayer2.getMaxHp() / 4 -> {

                if (healUseMagicMp <= cpuPlayer.mp) {
                    battleLog.append(cpuPlayer.healingMagic(cpuPlayer2))
                }
            }

            // 魔法を使う
            priestUseMagicMp <= cpuPlayer.mp -> {
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

/*
Priestの攻撃優先順
1:回復魔法　2:攻撃魔法 3:通常攻撃 4:スキル攻撃
毒状態の場合:eat()が優先される
 */

