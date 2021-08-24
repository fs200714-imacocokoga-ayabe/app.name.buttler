package com.e.app_namebattler.view.party.player.job

import com.e.app_namebattler.view.party.magic.Fire
import com.e.app_namebattler.view.party.magic.IOwnMagic
import com.e.app_namebattler.view.party.magic.Thunder
import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.party.skill.FierElemental
import com.e.app_namebattler.view.party.skill.IOwnSkill
import com.e.app_namebattler.view.view.music.SoundData

class Wizard(name: String) : Player(name), IOwnMagic, IOwnSkill {

    constructor(
        name: String,
        job: String,
        hp: Int,
        mp: Int,
        str: Int,
        def: Int,
        agi: Int,
        luck: Int
    ) : this(name) {
        makePlayer(name, job, hp, mp, str, def, agi, luck)
        initMagics()
        initSkills()
    }

    override fun initJob() {
        jobData = JobData.WIZARD
    }

    override fun initSkills() {
        skills = mutableListOf(FierElemental())
    }

    override fun initMagics() {
        magics = mutableListOf(Fire(), Thunder())
    }

    override fun normalAttack(defender: Player): StringBuilder {

        log.clear()

        if (this.isParalysis) {// 麻痺している場合
            log.append("${getName()}は麻痺で動けない！！\n")
        } else {// 麻痺していない場合
            log.append("${this.getName()}の攻撃！\n${getName()}は杖を振り回した！\n")
            setAttackSoundEffect(SoundData.S_PUNCH01.getSoundNumber())
            damage = calcDamage(defender) // 与えるダメージを求める
            damageProcess(defender, damage)
        }
        knockedDownCheck(defender)
        return log
    }

    override fun skillAttack(defender: Player): StringBuilder {

        log.clear()
        skill = skills[0]

        if (this.isParalysis) {// 麻痺している場合
            log.append("${this.getName()}は麻痺で動けない！！\n")
        } else {// 麻痺していない場合
            log = skill.effect(this, defender)
        }
        knockedDownCheck(defender)
        return log
    }

    override fun magicAttack(defender: Player): StringBuilder {

        log.clear()
        magic = choiceMagic()

        if (this.isParalysis) {// 麻痺している場合
            log.append("${this.getName()}は麻痺で動けない！！\n")
        } else {// 麻痺していない場合
            log = (magic.effect(this, defender))
        }
        knockedDownCheck(defender)
        return log
    }
}
