package com.e.app_namebattler

class GameManager {

    lateinit var player: Player

    private lateinit var player1: Player

    private lateinit var player2: Player

    private val pt = Party()

    var listener: BattleLogListener? = null

    fun battle(player: Player) {


    }

    fun speedReordering(
        enemy01: Player,
        enemy02: Player,
        enemy03: Player,
        ally01: Player,
        ally02: Player,
        ally03: Player
    ): List<Player> {

        val speedData: MutableList<Player> = mutableListOf(ally01,
            ally02,
            ally03,
            enemy01,
            enemy02,
            enemy03)

        for (i in 0 until speedData.size - 1) { // 速さ順の並び替え処理
            for (j in 0 until speedData.size - i - 1) {
                player1 = speedData[j]
                player2 = speedData[j + 1]
                if (player1.getAGI() < player2.getAGI()) {
                    val box: Player? = speedData[j]
                    speedData[j] = speedData[j + 1]
                    if (box != null) {
                        speedData[j + 1] = box
                    }
                }
            }
        }

        for (i in speedData) { // membersに速さ順に格納
            player = i
            pt.setMembers(player)
        }

        return speedData

    }

//    fun takeLog(){
//
//        val log = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//
//        listener?.upDateBattleLog(log)
//
//    }
    //------------------------------------------------------------------------
//    var mCallBack: BattleLogListener? = null
//
//    constructor(callBack: BattleLogListener){
//        this.mCallBack = callBack
//    }
//
//    fun battleLog(s: String){
//
//        this.mCallBack?.upDateBattleLog(s)
//
//
//    }
}