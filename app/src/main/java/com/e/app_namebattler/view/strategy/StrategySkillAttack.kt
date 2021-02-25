package com.e.app_namebattler.view.strategy

import com.e.app_namebattler.view.party.player.Player

class StrategySkillAttack : BaseStrategy() { // スキル攻撃

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
        data[1] = 3 // 作戦番号3を入れる
        party.clear()
        return data
    }
}
