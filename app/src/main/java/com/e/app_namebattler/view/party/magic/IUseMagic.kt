package com.e.app_namebattler.view.party.magic

import com.e.app_namebattler.view.party.player.Player

interface IUseMagic {
    fun initMagic()
    fun effect(attacker: Player, defender: Player): StringBuilder
    fun hasEnoughMp(mp: Int): Boolean
    fun damageProcess(attacker: Player, defender: Player, damage: Int)
    fun recoveryProcess(attacker: Player, defender: Player, heal: Int)
}