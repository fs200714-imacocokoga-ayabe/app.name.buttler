package com.e.app_namebattler

class Strategy1 : BaseStrategy() {
    // 通常攻撃
    /**
     * player,party1,party2を受け取りdata(味方ID,敵ID,作戦番号)を返す
     * @param player1 :自身
     * @param party1 :パーティ1
     * @param party2 :パーティ2
     * @return  :味方ID,敵ID,作戦番号3
     */
    override fun attackStrategy(
        player1: Player,
        party1: List<Player>,
        party2: List<Player>
    ): IntArray {
        this.player1 = player1
        if (player1.isMark()!!) { // player1がtrueの場合
            party.addAll(party2) // partyにparty2を入れる
        } else { // player1がfalseの場合
            party.addAll(party1) // partyにparty1を入れる
        }
        val a = random.nextInt(party.size)
        player2 = party[a]
        data[0] = player2!!.getIdNumber() // ランダムで出た相手ID
        data[1] = 1 // 作戦番号1を入れる
        party.clear()
        return data // playerIDとランダムで出た相手IDと作戦番号を返す
    }
}

