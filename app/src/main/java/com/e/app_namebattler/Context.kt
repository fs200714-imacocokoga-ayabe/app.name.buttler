package com.e.app_namebattler

class Context(var istrategy: IStrategy) {
    var player1: Player? = null
    var player2: Player? = null
    var party1: List<Player>? = null
    var party2: List<Player>? = null
    fun attackStrategy(
        player1: Player?,
        party1: List<Player>,
        party2: List<Player>
    ): IntArray {

        return istrategy.attackStrategy(player1!!, party1, party2)
    }
}
