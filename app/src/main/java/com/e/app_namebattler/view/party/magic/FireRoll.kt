package com.e.app_namebattler.view.party.magic

import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.view.music.SoundData

class FireRoll: BaseUseMagic() {

    override fun effect(attacker: Player, defender: Player): StringBuilder {
     //   if (hasEnoughMp(attacker.mp)) {
            log.append("${attacker.getName()}は${MagicData.FIRE_ROLL.getName()}を唱えた！\n火の球が飛んでいく！\n")
            magicData.getMpCost().let { attacker.downMp(it) }
            attacker.setAttackSoundEffect(SoundData.S_FIRE01.getSoundNumber())
            damage = (magicData.getMinDamage()..magicData.getMaxDamage()).random()
            damageProcess(attacker, defender, damage)
          //  return log
//        } else {
//            log.append("${attacker.getName()}は術を唱えようとしたが、MPが足りない！！\n")
//           // log.append(attacker.normalAttack(defender))
//        }
        return log
    }

    override fun initMagic() {
        magicData = MagicData.FIRE_ROLL
    }
}