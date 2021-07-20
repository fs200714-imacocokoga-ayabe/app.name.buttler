package com.e.app_namebattler.view.party.job

import com.e.app_namebattler.view.party.magic.*
import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.view.music.SoundData


class JobWizard(name: String) : Player(name), IMagicalUsable,IOwnMagic {

    constructor(
        name: String,
        job: String,
        hp: Int,
        mp: Int,
        str: Int,
        def: Int,
        agi: Int,
        luck: Int
    ) : this(name){
//        makePlayer(name, job, hp, mp, str, def, agi, luck)
        initMagics()
    }
    override fun initJob(){
        jobData = JobData.WIZARD
    }

    override fun initMagics(){
        magics  = mutableListOf(Fire(),Thunder())
    }

    override fun normalAttack(defender: Player): StringBuilder {

        log.clear()

        if (this.isParalysis) {// 麻痺している場合

            log.append("${getName()}は麻痺で動けない！！\n")
            knockedDownCheck(defender)

        } else {// 麻痺していない場合

            log.append("${this.getName()}の攻撃！\n${getName()}は杖を振り回した！\n")
            setAttackSoundEffect(SoundData.S_PUNCH01.getSoundNumber())
            damage = calcDamage(defender) // 与えるダメージを求める
            damageProcess(defender, damage)
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

            if ((1..100).random() < MagicData.FIRE_ELEMENTAL.getInvocationRate()) { // 40％で発動

                log.append("${this.getName()}の攻撃！\n${this.getName()}は魔法陣を描いて${MagicData.FIRE_ELEMENTAL.getName()}を召還した！\n")
                setAttackSoundEffect(SoundData.S_FIRE01.getSoundNumber())

                super.damageProcess(
                    defender,
                    MagicData.FIRE_ELEMENTAL.getMinDamage()
                ) // ダメージ処理
            } else { // 60%で不発
                log.append("${this.getName()}の攻撃だがスキルは発動しなかった！\n")
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
