package com.e.app_namebattler.view.strategy

import com.e.app_namebattler.view.party.player.Player


class StrategyUseHerb : BaseStrategy() { // 薬草を食べる

    override fun attackStrategy(
        player1: Player,
        party1: List<Player>,
        party2: List<Player>
    ): IntArray {
        this.player1 = player1
        if (player1.isMark) { // player1がtrueの場合
                party.addAll(party2) // partyにparty2を入れる
            } else { // player1がfalseの場合
                party.addAll(party1) // partyにparty1を入れる
            }

        val a = (1..party.size).random()
        player2 = party[a - 1]
        data[0] = player2!!.getIdNumber() // ランダムで出た相手ID
        data[1] = 5 // 作戦番号5を入れる
        party.clear()
        return data // playerIDとランダムで出た相手IDと作戦番号を返す
    }
}
