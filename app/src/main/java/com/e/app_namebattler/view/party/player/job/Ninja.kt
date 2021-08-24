package com.e.app_namebattler.view.party.player.job

import com.e.app_namebattler.view.party.magic.FireRoll
import com.e.app_namebattler.view.party.magic.IOwnMagic
import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.party.skill.IOwnSkill
import com.e.app_namebattler.view.party.skill.Swallow
import com.e.app_namebattler.view.view.music.SoundData

class Ninja(name: String) : Player(name), IOwnMagic, IOwnSkill {

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
        jobData = JobData.NINJA
    }

    override fun initMagics() {
        magics = mutableListOf(FireRoll())
    }

    override fun initSkills() {
        skills = mutableListOf(Swallow())
    }

    override fun normalAttack(defender: Player): StringBuilder {

        log.clear()

        if (this.isParalysis) {// 麻痺している場合
            log.append("${this.getName()}は麻痺で動けない！！\n")
        } else {// 麻痺していない場合
            log.append("${this.getName()}の攻撃！\n刀で突きさした！\n")
            setAttackSoundEffect(SoundData.S_KATANA01.getSoundNumber())
            damage = calcDamage(defender) // 与えるダメージを求める
            damageProcess(defender, damage) // ダメージ処理
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


