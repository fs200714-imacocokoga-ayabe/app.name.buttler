package com.e.app_namebattler.view.strategy

import com.e.app_namebattler.view.party.player.Player

interface IStrategy {

    fun attackStrategy(
        player1: Player, party1: List<Player>,
        party2: List<Player>
    ): IntArray
}