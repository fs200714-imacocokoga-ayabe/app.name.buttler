package com.e.app_namebattler.view.party.job

import com.e.app_namebattler.view.party.magic.*
import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.party.skill.Skill
import com.e.app_namebattler.view.view.music.SoundData

class JobNinja(name: String) : Player(name), IMagicalUsable, IOwnMagic {

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
      //  makePlayer(name, job, hp, mp, str, def, agi, luck)
        initMagics()
    }

    override fun initJob() {
        jobData = JobData.NINJA
    }

    override fun initMagics() {
        magics  = mutableListOf(FireRoll())
    }

    override fun normalAttack(defender: Player): StringBuilder {

        log.clear()

        if (this.isParalysis) {// 麻痺している場合

            log.append("${this.getName()}は麻痺で動けない！！\n")
            knockedDownCheck(defender)

        } else {// 麻痺していない場合
            log.append("${this.getName()}の攻撃！\n刀で突きさした！\n")
            setAttackSoundEffect(SoundData.S_KATANA01.getSoundNumber())
            damage = calcDamage(defender) // 与えるダメージを求める
            damageProcess(defender, damage) // ダメージ処理
            knockedDownCheck(defender)

        }
        return log
    }

    override fun skillAttack(defender: Player): StringBuilder {

        log.clear()

        if (this.isParalysis) {// 麻痺している場合
            log.append("${this.getName()}は麻痺で動けない！！\n")
            knockedDownCheck(defender)
        } else {// 麻痺していない場合

            log.append("${this.getName()}の${Skill.SWALLOW.getSkillName()}！\n")

            if ((1..100).random() < Skill.SWALLOW.getInvocationRate()) { // 確率で発動

                setAttackSoundEffect(SoundData.S_KATANA02.getSoundNumber())

                for (i in 1..2) {
                    log.append("${i}回目の攻撃\n")
                    damage = calcDamage(defender) // 攻撃処理
                    super.damageProcess(defender, damage) // ダメージ処理

                    if (defender.hp <= 0) { // 倒れた判定
                        break
                    }
                }
            } else { // 70%で不発
                setAttackSoundEffect(SoundData.S_SLIDE01.getSoundNumber())
                log.append("${this.getName()}は石につまづいて転んだ！\n")
            }
            knockedDownCheck(defender)
        }
        return log
    }

    override fun magicAttack(defender: Player): StringBuilder {

        log.clear()
        magic = choiceMagic()

        if (this.isParalysis) {// 麻痺している場合
            log.append("${this.getName()}は麻痺で動けない！！\n")
            knockedDownCheck(defender)
        } else {// 麻痺していない場合
            log = (magic.effect(this, defender))
        }
        return log
    }
}


