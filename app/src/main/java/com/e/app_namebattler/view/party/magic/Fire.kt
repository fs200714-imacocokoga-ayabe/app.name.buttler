package com.e.app_namebattler.view.party.magic

import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.view.music.SoundData

class Fire : BaseUseMagic() {

    override fun effect(attacker: Player, defender: Player): StringBuilder {
        if (hasEnoughMp(attacker.mp)) {
            log.append("${attacker.getName()}は${MagicData.FIRE.getName()}を唱えた！\n炎が渦を巻いた！\n")
            attacker.setAttackSoundEffect(SoundData.S_FIRE01.getSoundNumber())
            magicData.getMpCost().let { attacker.downMp(it) }
            damage = ((magicData.getMinDamage())..magicData.getMaxDamage()).random()// 乱数10～30
            damageProcess(attacker, defender, damage)
            return log
        } else {
            log.append("${attacker.getName()}は術を唱えようとしたが、MPが足りない！！\n")
            attacker.normalAttack(defender)
    }
    return log
    }

    override fun initMagic() {
        magicData = MagicData.FIRE
    }
}

