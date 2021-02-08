package com.e.app_namebattler

interface BattleLogListener {

    fun upDateBattleLog(battleLog: List<String>)

    fun upDateAllyStatus(ally01: Player, ally02: Player, ally03: Player)

    fun upDateEnemyStatus(enemy01: Player, enemy02: Player, enemy03: Player)
}


