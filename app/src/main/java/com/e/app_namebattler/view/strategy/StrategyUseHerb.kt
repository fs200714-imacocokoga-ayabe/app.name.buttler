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

        battleLog.append(player1.eat())

        party.clear()

        return battleLog // バトルログを返す
    }
}
