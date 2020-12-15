package com.e.app_namebattler

interface BattleLogListener {

    fun upDateBattleLog(log: StringBuilder)

    fun upDateStatus(ally01: Player, ally02: Player, ally03: Player, enemy01: Player, enemy02: Player, enemy03: Player)

}


