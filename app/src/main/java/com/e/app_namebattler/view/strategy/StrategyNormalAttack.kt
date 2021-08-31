package com.e.app_namebattler.view.strategy

import com.e.app_namebattler.view.party.player.Player

class StrategyNormalAttack : BaseStrategy() {// 通常攻撃

    override fun attackStrategy(
        player1: Player,
        party1: List<Player>,
        party2: List<Player>
    ): StringBuilder {

        battleLog.clear()

        this.player1 = player1

        party.addAll((party2))
        party.shuffle()// 敵をシャッフルで選択
        player2 = party[0]

        battleLog.append(player1.normalAttack(player2!!))

        party.clear()

        return battleLog // バトルログを返す
    }
}

