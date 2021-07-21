package com.e.app_namebattler.view.party.skill

import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.view.music.SoundData

class OpticalElemental : BaseUseSkill(){

    override fun effect(attacker: Player, defender: Player): StringBuilder {

        if ((1..100).random() < skillData.getInvocationRate()) {
            log.append("${attacker.getName()}は祈りを捧げて${skillData.getSkillName()}を召還した\n${skillData.getSkillName()}の祝福を受けた！\n")
            recoveryProcess(attacker, skillData.getRecoveryValue())
            attacker.setAttackSoundEffect(SoundData.S_HEAL01.getSoundNumber())
        } else {
            log.append("${attacker.getName()}は祈りを捧げたが何も起こらなかった！\n")
        }
        return log
    }

    override fun initSkill() {
        skillData = SkillData.OPTICAL_ELEMENTAL
    }
}