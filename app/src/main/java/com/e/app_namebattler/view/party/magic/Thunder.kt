package com.e.app_namebattler.view.party.magic

import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.view.music.SoundData

class Thunder: BaseUseMagic() {

    override fun effect(attacker: Player, defender: Player): StringBuilder {

            log.append("${attacker.getName()}は${MagicData.THUNDER.getName()}を唱えた！\n雷が地面を這っていく！\n")
            attacker.setAttackSoundEffect(SoundData.S_THUNDER01.getSoundNumber())
            magicData.getMpCost().let { attacker.downMp(it) }
            damage = (magicData.getMinDamage()..magicData.getMaxDamage()).random()// 乱数10～30
            damageProcess(attacker, defender, damage)

            return log
    }

    override fun initMagic() {
        magicData = MagicData.THUNDER
    }
}