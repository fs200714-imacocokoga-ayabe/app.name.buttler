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
            10 <= cpuPlayer.mp -> { // MPが10以上ある場合
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

    /**
     * HPの低い相手を選んで返す
     * @return userPlayer : HPの低い相手
     */
    private fun selectLowerHp(): Player {

        userPlayer = userParty[((1..userParty.size).random()) - 1] // 敵パーティから1人userPlayerに入れる
        //   userPlayer = userParty[0] // 敵パーティから1人userPlayerに入れる

        for (i in 1 until userParty.size) {
            if (userParty[i].hp < userPlayer.hp) { // userPlayerよりHPが低い場合
                userPlayer = userParty[i] // HPのひくい敵をuserPlayerに入れる
            }
        }
        userParty.clear()
        return userPlayer
    }
}