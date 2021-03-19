package com.e.app_namebattler.controller

import com.e.app_namebattler.view.party.player.Player

interface BattleLogListener {

    fun upDateBattleLog(battleLog: StringBuilder)

    fun upDateAllyStatus(ally01: Player, ally02: Player, ally03: Player)

    fun upDateEnemyStatus(enemy01: Player, enemy02: Player, enemy03: Player)
}


