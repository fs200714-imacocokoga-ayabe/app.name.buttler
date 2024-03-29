package com.e.app_namebattler.view.party.magic

import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.view.music.SoundData

class Heal : BaseUseMagic(), IRecoveryMagic{

    override fun effect(attacker: Player, defender: Player): StringBuilder {

            log.append("${attacker.getName()}は${MagicData.HEAL.getName()}を唱えた！\n光が${defender.getName()}を包む\n")
            attacker.setAttackSoundEffect(SoundData.S_HEAL01.getSoundNumber())
            magicData.getMpCost().let { attacker.downMp(it) }
            recoveryProcess(attacker, defender, magicData.getRecoveryValue())
            return log
    }
    
    override fun initMagic() {
        magicData = MagicData.HEAL
    }
}
