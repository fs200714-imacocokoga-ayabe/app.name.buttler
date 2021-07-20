package com.e.app_namebattler.view.party.magic

import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.view.music.SoundData

class Poison : BaseUseMagic() {

    override fun effect(attacker: Player, defender: Player): StringBuilder {

        if (hasEnoughMp(attacker.mp)) {
            log.append("${attacker.getName()}は${magicData.getName()}を唱えた！\n瘴気が相手を包んだ！\n")

            if ((1..100).random() <= MagicData.PARALYSIS.getContinuousRate()) { // 乱数がPARALYSISの値以下の場合麻痺状態になる

                defender.isPoison = true // 相手に毒をセット
                log.append("${defender.getName()}は毒状態になった！\n")
                magicData.getMpCost().let { attacker.downMp(it) }
                attacker.setAttackSoundEffect(SoundData.S_POISON01.getSoundNumber())

            } else {
                log.append("${defender.getName()}は毒を受けなかった！\n")
            }
            return log

        } else {
            log.append("${attacker.getName()}は術を唱えようとしたが、MPが足りない！！\n")
            attacker.normalAttack(defender)
        }
        return log
    }

    override fun initMagic() {
        magicData = MagicData.POISON
    }
}
