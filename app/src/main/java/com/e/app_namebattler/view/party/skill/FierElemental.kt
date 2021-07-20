package com.e.app_namebattler.view.party.skill

import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.view.music.SoundData

class FierElemental : BaseUseSkill(){

    override fun effect(attacker: Player, defender: Player): StringBuilder {

        if ((1..100).random() < skillData.getInvocationRate()) {

            log.append("${attacker.getName()}の攻撃！\n${attacker.getName()}は魔法陣を描いて${skillData.getSkillName()}を召還した！\n")
            attacker.setAttackSoundEffect(SoundData.S_FIRE01.getSoundNumber())
            damageProcess(attacker, defender, skillData.getDamageRate()) // ダメージ処理
        } else { // 60%で不発
            log.append("${attacker.getName()}の攻撃だがスキルは発動しなかった！\n")
        }
        return log
    }

    override fun initSkill() {
        skillData = SkillData.FIRE_ELEMENTAL
    }
}