package com.e.app_namebattler.view.party.job

import com.e.app_namebattler.view.party.magic.*
import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.view.music.SoundData

class JobPriest(name: String) : Player(name), IMagicalUsable, IRecoveryMagic, IOwnMagic {

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
//        makePlayer(name, job, hp, mp, str, def, agi, luck)
        initMagics()
    }

    var isHeal = false

    override fun initJob() {
        jobData = JobData.PRIEST
    }

    override fun initMagics() {
        magics  = mutableListOf(Poison(), Paralysis(), Heal())
    }

    override fun normalAttack(defender: Player): StringBuilder {

        log.clear()

        if (this.isParalysis) {// 麻痺している場合

            log.append("${this.getName()}は麻痺で動けない！！\n")
            knockedDownCheck(defender)

        } else {// 麻痺していない場合
            log.append("${this.getName()}の攻撃！\n錫杖で突いた！\n")
            setAttackSoundEffect(SoundData.S_PUNCH01.getSoundNumber())
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
            knockedDownCheck(this)

        } else {// 麻痺していない場合

            if ((1..100).random() < MagicData.OPTICAL_ELEMENTAL.getInvocationRate()) {
                log.append("${this.getName()}は祈りを捧げて${MagicData.OPTICAL_ELEMENTAL.getName()}を召還した\n${MagicData.OPTICAL_ELEMENTAL.getName()}の祝福を受けた！\n")
                recoveryProcess(this, MagicData.OPTICAL_ELEMENTAL.getRecoveryValue())
                setAttackSoundEffect(SoundData.S_HEAL01.getSoundNumber())
            } else {
                log.append("${this.getName()}は祈りを捧げたが何も起こらなかった！\n")
            }
            knockedDownCheck(this)
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

    override fun healingMagic(defender: Player): StringBuilder {

        log.clear()
        magic = magics[2]

        if (this.isParalysis) {// 麻痺している場合
            log.append("${this.getName()}は麻痺で動けない！！\n")
            
        } else {// 麻痺していない場合

          //  isHeal = true
            log = (magic.effect(this, defender))

         //   isHeal = false
        }
        knockedDownCheck(this)

        return log
    }


}
