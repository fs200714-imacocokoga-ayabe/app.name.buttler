package com.e.app_namebattler

class Context(var istrategy: IStrategy) {

    fun attackStrategy(
        player1: Player?,
        party1: List<Player>,
        party2: List<Player>
    ): IntArray {

        return istrategy.attackStrategy(player1!!, party1, party2)
    }
}
