package com.e.app_namebattler.view.strategy

import com.e.app_namebattler.view.party.player.Player


class StrategyUseHerb : BaseStrategy() { // 薬草を食べる

    override fun attackStrategy(
        player1: Player,
        party1: List<Player>,
        party2: List<Player>
    ): StringBuilder {

        battleLog.clear()

        this.player1 = player1

        party.addAll((party2))

        player2 = party[((1..party.size).random()) - 1] // 敵をランダムで選択

        battleLog.append(player1.eat())

        party.clear()

        return battleLog // バトルログを返す
    }
}
