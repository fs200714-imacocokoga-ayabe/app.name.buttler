package com.e.app_namebattler.view.party.magic

import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.view.music.SoundData

class Paralysis : BaseUseMagic() {

    override fun effect(attacker: Player, defender: Player): StringBuilder {

        if (hasEnoughMp(attacker.mp)) {
            log.append("${attacker.getName()}は${magicData.getName()}を唱えた！\n蒼い霧が相手を包んだ！\n")

            if ((1..100).random() <= MagicData.PARALYSIS.getContinuousRate()) { // 乱数がPARALYSISの値以下の場合麻痺状態になる
                defender.isParalysis = true
                log.append("${defender.getName()}は麻痺を受けた！\n")
                magicData.getMpCost().let { attacker.downMp(it) }
                attacker.setAttackSoundEffect(SoundData.S_PARALYSIS01.getSoundNumber())

            } else { // 麻痺を状態にならなかった場合
                log.append("${defender.getName()}は麻痺を受けなかった！\n")
            }
            return log

        } else {
            log.append("${attacker.getName()}は術を唱えようとしたが、MPが足りない！！\n")
            attacker.normalAttack(defender)
        }
        return log
    }

    override fun initMagic() {
        magicData = MagicData.PARALYSIS
    }
}
