package com.e.app_namebattler.view.party.job

import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.party.magic.IMagicalUsable
import com.e.app_namebattler.view.party.magic.MagicData
import com.e.app_namebattler.view.party.skill.Skill
import com.e.app_namebattler.view.view.music.SoundData

class JobNinja (name:String): Player(name), IMagicalUsable {

    constructor(
        name: String,
        job: String,
        hp: Int,
        mp: Int,
        str: Int,
        def: Int,
        agi: Int,
        luck: Int
    ) : this(name)

    // 忍者のパラメータを名前から生成する
    override fun makeCharacter(name: String) {

        this.job = JobData.NINJA.getJobName()
        this.hp = getNumber(100) + 70 // 70-170
        this.mp = getNumber(20) + 10 // 10-30
        this.str = getNumber(50) + 20 // 20-70
        this.def = getNumber(49) + 1  // 1-50
        this.luck = getNumber(99) + 1 // 1-100
        this.agi = getNumber(40) + 40 // 40-80
    }

    override fun normalAttack(defender: Player): StringBuilder {

        log.clear()

        if (this.isParalysis){// 麻痺している場合

            log.append("${this.getName()}は麻痺で動けない！！\n")
            knockedDownCheck(defender)

        }else {// 麻痺していない場合
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

        if (this.isParalysis){// 麻痺している場合

            log.append("${this.getName()}は麻痺で動けない！！\n")
            knockedDownCheck(defender)

        }else {// 麻痺していない場合

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

        if (this.isParalysis){// 麻痺している場合

            log.append("${this.getName()}は麻痺で動けない！！\n")
            knockedDownCheck(defender)

        }else {// 麻痺していない場合

            if (hasEnoughMp()) {
                damage = effect()
                super.damageProcess(defender, damage)
                knockedDownCheck(defender)

            } else {
                log.append("${this.getName()}は術を唱えようとしたが、MPが足りない！！\n")
                normalAttack(defender)
            }
        }
        return log
    }

    private fun effect(): Int {

        damage = (MagicData.FIRE_ROLL.getMinDamage()..MagicData.FIRE_ROLL.getMaxDamage()).random() // 乱数10～30
        this.mp = this.mp - MagicData.FIRE_ROLL.getMpCost() // MP消費
        log.append("${this.getName()}は${MagicData.FIRE_ROLL.getName()}を唱えた！\n火の球が飛んでいく！\n")
        setAttackSoundEffect(SoundData.S_FIRE01.getSoundNumber())
        return damage
    }

    private fun hasEnoughMp(): Boolean {
        return NINJA_USE_MAGIC_LOW_MP <= this.mp
    }
}
