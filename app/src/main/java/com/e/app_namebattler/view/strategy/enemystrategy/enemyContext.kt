package com.e.app_namebattler.view.strategy.enemystrategy

import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.strategy.IStrategy

class EnemyContext(var iStrategy: IStrategy) {

    fun attackStrategy(
        player1: Player?,
        party1: List<Player>,
        party2: List<Player>
    ): StringBuilder {
        return this.iStrategy.attackStrategy(player1!!, party1, party2)
    }
}
