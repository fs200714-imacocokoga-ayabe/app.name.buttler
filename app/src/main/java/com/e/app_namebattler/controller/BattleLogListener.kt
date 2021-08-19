package com.e.app_namebattler.controller

import com.e.app_namebattler.view.party.player.Player

interface BattleLogListener {

    // 初期ステータス
    fun upDateInitialStatus(
        ally01: Player,
        ally02: Player,
        ally03: Player,
        enemy01: Player,
        enemy02: Player,
        enemy03: Player
    )

    fun upDateAllLog(battleLog: MutableList<Any>,
                     ally01: Player,
                     ally02: Player,
                     ally03: Player,
                     enemy01: Player,
                     enemy02: Player,
                     enemy03: Player) {
    }
}


