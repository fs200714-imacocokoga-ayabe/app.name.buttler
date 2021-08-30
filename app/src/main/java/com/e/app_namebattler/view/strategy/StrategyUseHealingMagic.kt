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
    ): StringBuilder {

        battleLog.clear()

        if (player1.isMark) { // player1がtrueの場合
            _party1.addAll(party1) // _party1にparty1を入れる
            _party2.addAll(party2) // _party2にparty2を入れる
        } else { // player1がfalseの場合
            _party1.addAll(party2) // party1にparty2を入れる
            _party2.addAll(party1) // party2にparty1を入れる
        }

        if (0 < _party2.size) {

            player2 = _party2[((1.._party2.size).random()) - 1] // 敵をランダムで選択
        }

        if (player1 is IRecoveryMagic) { //player1が僧侶の場合

            player = player1

            for (i in _party1.indices) { // HPの低い味方を選ぶ

                if (player!!.getMaxHp() - player!!.hp < _party1[i].getMaxHp() - _party1[i].hp) {
                    player = _party1[i]
                }
            }

            if (20 <= player1.mp) {
                battleLog.append(player1.healingMagic(player!!))
            } else {
                battleLog.append(player1.healingMagic(player2!!))
            }

        } else { // player1が僧侶ではない場合

            battleLog.append(player1.normalAttack(player2!!))
        }

        _party1.clear() // _party1をクリア
        _party2.clear() // _party2をクリア

        return battleLog // バトルログを返す
    }
}

