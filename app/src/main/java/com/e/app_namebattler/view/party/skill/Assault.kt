package com.e.app_namebattler.view.party.skill

import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.view.music.SoundData

class Assault : BaseUseSkill() {

    override fun effect(attacker: Player, defender: Player):  StringBuilder  {
        if ((1..100).random() < SkillData.ASSAULT.getInvocationRate()) { // 確率で発動
            log.append("${attacker.getName()}の${skillData.getSkillName()}！\n")
            damage = attacker.calcDamage(defender) // 与えるダメージを求める
            damage *= skillData.getDamageRate() // ダメージ2倍
            attacker.setAttackSoundEffect(SoundData.S_SWORD02.getSoundNumber())
            damageProcess(attacker, defender, damage) // ダメージ処理

        } else {
            log.append("${attacker.getName()}の${SkillData.ASSAULT.getSkillName()}はかわされた！！\n")
            attacker.setAttackSoundEffect(SoundData.S_SWORD02_AIR_SHOT.getSoundNumber())
        }
        return log
    }

    override fun initSkill() {
        skillData = SkillData.ASSAULT
    }
}
