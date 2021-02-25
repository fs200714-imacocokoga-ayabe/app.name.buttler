package com.e.app_namebattler.view.strategy

import com.e.app_namebattler.view.party.player.Player

class Context(var istrategy: IStrategy) {

    fun attackStrategy(
        player1: Player?,
        party1: List<Player>,
        party2: List<Player>
    ): IntArray {

        return istrategy.attackStrategy(player1!!, party1, party2)
    }
}
