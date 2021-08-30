package com.e.app_namebattler.view.strategy

import com.e.app_namebattler.view.party.magic.IOwnMagic
import com.e.app_namebattler.view.party.player.Player

class StrategyUseMagic : BaseStrategy() {// 魔法を使用

    override fun attackStrategy(
        player1: Player,
        party1: List<Player>,
        party2: List<Player>
    ): StringBuilder {

        battleLog.clear()
        this.player1 = player1

        party.addAll((party2))

        player2 = party[((1..party.size).random()) - 1] // 敵をランダムで選択

        if (player1 is IOwnMagic) {
            battleLog.append(player1.magicAttack(player2!!))

        } else {
            battleLog.append(player1.normalAttack(player2!!))
        }
        party.clear()
        return battleLog // バトルログを返す
    }
}
