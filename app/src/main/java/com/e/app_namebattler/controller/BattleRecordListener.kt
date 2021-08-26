package com.e.app_namebattler.controller

import com.e.app_namebattler.view.party.player.Player
//使用していない処理
open class BattleRecordListener {

    var callBack: BattleLogListener? = null

    private val record = ""
    private lateinit var  ally01: Player
    private lateinit var  ally02: Player
    private lateinit var  ally03: Player
    private lateinit var enemy01: Player
    private lateinit var enemy02: Player
    private lateinit var enemy03: Player

    fun sendBattleData() {
        callBack?.battleData(record, ally01, ally02, ally03, enemy01, enemy02, enemy03)
}
}