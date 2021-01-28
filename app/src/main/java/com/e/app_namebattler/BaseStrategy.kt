package com.e.app_namebattler

import java.util.*
import kotlin.collections.ArrayList

open class BaseStrategy : IStrategy {
    var random: Random = Random()
    protected var player: Player? = null
    protected var player1: Player? = null
    protected var player2: Player? = null
    protected var party: MutableList<Player> = ArrayList()
    protected var party1: MutableList<Player> = ArrayList()
    protected var party2: MutableList<Player> = ArrayList()
    protected var data = IntArray(2) // 味方IDと敵IDと作戦番号を格納

    override fun attackStrategy(
        player1: Player,
        party1: List<Player>,
        party2: List<Player>
    ): IntArray {
        return data
    }
}
