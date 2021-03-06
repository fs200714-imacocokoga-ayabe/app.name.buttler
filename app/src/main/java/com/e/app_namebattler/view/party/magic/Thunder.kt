package com.e.app_namebattler.view.party.magic

import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.view.music.SoundData

class Thunder: BaseUseMagic() {

    override fun effect(attacker: Player, defender: Player): StringBuilder {
        if (hasEnoughMp(attacker.mp)) {
            log.append("${attacker.getName()}は${MagicData.THUNDER.getName()}を唱えた！\n雷が地面を這っていく！\n")
            magicData.getMpCost().let { attacker.downMp(it) }
            attacker.setAttackSoundEffect(SoundData.S_THUNDER01.getSoundNumber())
            damage = (magicData.getMinDamage()..magicData.getMaxDamage()).random()// 乱数10～30
            damageProcess(attacker, defender, damage)

            return log
        } else {
            log.append("${attacker.getName()}は術を唱えようとしたが、MPが足りない！！\n")
        }
        return log
    }

    override fun initMagic() {
        magicData = MagicData.THUNDER
    }
}