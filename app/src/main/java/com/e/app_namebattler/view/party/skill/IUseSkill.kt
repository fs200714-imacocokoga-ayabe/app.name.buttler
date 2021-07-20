package com.e.app_namebattler.view.party.skill

import com.e.app_namebattler.view.party.player.Player

interface IUseSkill {

    fun initSkill()
    fun effect(attacker: Player, defender: Player): StringBuilder
    fun damageProcess(attacker: Player, defender: Player, damage: Int)
    fun recoveryProcess(attacker: Player, heal: Int)
}