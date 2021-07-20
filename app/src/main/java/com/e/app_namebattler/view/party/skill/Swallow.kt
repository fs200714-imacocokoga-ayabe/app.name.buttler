package com.e.app_namebattler.view.party.skill

import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.view.music.SoundData

class Swallow : BaseUseSkill() {

    override fun effect(attacker: Player, defender: Player): StringBuilder {

        log.append("${attacker.getName()}の${skillData.getSkillName()}！\n")

        if ((1..100).random() < SkillData.SWALLOW.getInvocationRate()) { // 確率で発動
            attacker.setAttackSoundEffect(SoundData.S_KATANA02.getSoundNumber())

            log.append("${attacker.getName()}は目にも止まらぬ速さで2回攻撃した！\n")
            damage = attacker.calcDamage(defender) // 攻撃処理
            damage *= skillData.getDamageRate()
            attacker.setAttackSoundEffect(SoundData.S_KATANA02.getSoundNumber())
            damageProcess(attacker, defender, damage) // ダメージ処理

        } else {
            attacker.setAttackSoundEffect(SoundData.S_SLIDE01.getSoundNumber())
            log.append("${attacker.getName()}は石につまづいて転んだ！\n")

        }
        return log
    }

    override fun initSkill() {
        skillData = SkillData.ASSAULT
    }
}