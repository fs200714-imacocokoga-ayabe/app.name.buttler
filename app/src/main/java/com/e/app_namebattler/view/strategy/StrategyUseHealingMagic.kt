package com.e.app_namebattler.view.strategy

import com.e.app_namebattler.view.party.magic.IRecoveryMagic
import com.e.app_namebattler.view.party.player.Player
import java.util.*


class StrategyUseHealingMagic : BaseStrategy() {// 回復優先の作戦

    private var _party1: MutableList<Player> = ArrayList()
    private var _party2: MutableList<Player> = ArrayList()

    override fun attackStrategy(
        player1: Player,
        party1: List<Player>,
        party2: List<Player>
    ): IntArray {
        if (player1.isMark) { // player1がtrueの場合
            _party1.addAll(party1) // _party1にparty1を入れる
            _party2.addAll(party2) // _party2にparty2を入れる
        } else { // player1がfalseの場合
            _party1.addAll(party2) // party1にparty2を入れる
            _party2.addAll(party1) // party2にparty1を入れる
        }

        if (player1 is IRecoveryMagic) {

            player = player1

            for (i in _party1.indices) { // HPの低い味方を選ぶ
                if ((_party1[i].getMaxHp() - _party1[i].getHP()) > player!!.getMaxHp() - player!!.hp) {
                    player = _party1[i]
                }
            }

            if (player1.getMP() >= 20) { // 自身のMPが20以上の場合
                data[0] = player!!.getIdNumber() // 対象プレイヤーにHPの低い味方のIDを入れる

            } else if (_party2.size > 0) { // 自身のMPが20未満の場合

                val a = (1.._party2.size).random()
                player2 = _party2[a - 1]
                data[0] = player2!!.getIdNumber() // 乱数で出た敵のIDを返す
            }

            data[1] = 4 // 作戦番号4を入れる

        } else { // player1が僧侶ではない場合

            if (_party2.size > 0) {

                val a = (1.._party2.size).random()
                player2 = _party2[a - 1]
                data[0] = player2!!.getIdNumber() // 乱数で出た敵のIDを返す
                data[1] = 4 // 作戦番号4を入れる
            }
        }
        _party1.clear() // _party1をクリア
        _party2.clear() // _party2をクリア
        return data // player1が僧侶の場合HPの低い味方IDと味方IDと作戦番号を返す
    }
}

