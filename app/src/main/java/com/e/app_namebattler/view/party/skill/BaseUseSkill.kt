package com.e.app_namebattler.view.party.skill

import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.view.music.SoundData

open class BaseUseSkill: IUseSkill{

    lateinit var skillData: SkillData
    var damage = 0
    var log = StringBuilder()

    init {
        initSkill()
    }

    override fun initSkill() {}

    override fun effect(attacker: Player, defender: Player): StringBuilder {
        return log
    }

    override fun damageProcess(attacker: Player, defender: Player, damage: Int) {

        log.append("${defender.getName()}に${damage}のダメージ！\n")

        if (0 < damage) {
            defender.setPrintStatusEffect(1)
        } else {
            defender.setPrintStatusEffect(3)
            attacker.setAttackSoundEffect(0)
        }
        defender.damage(damage) // 求めたダメージを対象プレイヤーに与える
    }

    override fun recoveryProcess(attacker: Player, heal: Int) {

        var healValue = heal

        healValue = attacker.getMaxHp().coerceAtMost(attacker.hp + healValue)
        healValue -= attacker.hp
        log.append("${attacker.getName()}はHPが${healValue}回復した！\n")
        attacker.hp += healValue
        attacker.setPrintStatusEffect(2)
        attacker.setAttackSoundEffect(SoundData.S_HEAL01.getSoundNumber())
    }
}