package com.e.app_namebattler

open class BaseStrategy : IStrategy {

    protected var player: Player? = null
    protected var player1: Player? = null
    protected var player2: Player? = null
    protected var party: MutableList<Player> = ArrayList()
    protected var data = IntArray(2) // 味方IDと敵IDと作戦番号を格納

    override fun attackStrategy(
        player1: Player,
        party1: List<Player>,
        party2: List<Player>
    ): IntArray {
        return data
    }
}
