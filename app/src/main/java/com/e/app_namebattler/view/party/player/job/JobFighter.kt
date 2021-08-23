package com.e.app_namebattler.view.party.player.job

import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.party.skill.Assault
import com.e.app_namebattler.view.party.skill.IOwnSkill
import com.e.app_namebattler.view.view.music.SoundData

class JobFighter(name: String) : Player(name) , IOwnSkill {

    constructor(name: String, job: String, hp: Int, mp: Int, str: Int, def: Int, agi: Int, luck: Int) : this(name){
//        makePlayer(name, job, hp, mp, str, def, agi, luck)
        initSkills()
    }
    override fun initJob(){
        jobData = JobData.FIGHTER
    }

    override fun initSkills() {
        skills = mutableListOf(Assault())
    }

    override fun normalAttack(defender: Player): StringBuilder {

        log.clear()

        if (this.isParalysis) {// 麻痺している場合
            log.append("${this.getName()}は麻痺で動けない！！\n")
        } else {// 麻痺していない場合

            log.append("${this.getName()}の攻撃！\n${this.getName()}は剣で斬りつけた！\n")
            setAttackSoundEffect(SoundData.S_SWORD01.getSoundNumber())
            damage = calcDamage(defender) // 与えるダメージを求める
            damageProcess(defender, damage)
        }
        knockedDownCheck(defender)
        return log
    }

    override fun skillAttack(defender: Player): StringBuilder { // スキル攻撃処理

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
}