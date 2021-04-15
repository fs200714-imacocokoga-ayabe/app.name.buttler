package com.e.app_namebattler.controller

import com.e.app_namebattler.view.party.player.Player

interface BattleLogListener {

    fun upDateInitialStatus(
        ally01StatusLog: MutableList<String>,
        ally02StatusLog: MutableList<String>,
        ally03StatusLog: MutableList<String>,
        enemy01StatusLog: MutableList<String>,
        enemy02StatusLog: MutableList<String>,
        enemy03StatusLog: MutableList<String>,
        ally01: Player,
        ally02: Player,
        ally03: Player,
        enemy01: Player,
        enemy02: Player,
        enemy03: Player)

    fun upDateAllLog(
        battleLog: MutableList<Any>,
        ally01StatusLog: MutableList<String>,
        ally02StatusLog: MutableList<String>,
        ally03StatusLog: MutableList<String>,
        enemy01StatusLog: MutableList<String>,
        enemy02StatusLog: MutableList<String>,
        enemy03StatusLog: MutableList<String>,
        ally01: Player,
        ally02: Player,
        ally03: Player,
        enemy01: Player,
        enemy02: Player,
        enemy03: Player
    )
}


