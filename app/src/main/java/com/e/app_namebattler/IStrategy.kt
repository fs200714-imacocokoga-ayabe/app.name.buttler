package com.e.app_namebattler

interface IStrategy {

    fun attackStrategy(
        player1: Player, party1: List<Player>,
        party2: List<Player>
    ): IntArray
}