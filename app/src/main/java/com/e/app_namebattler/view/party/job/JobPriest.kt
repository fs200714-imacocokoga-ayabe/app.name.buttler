package com.e.app_namebattler.view.party.job

import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.party.magic.IMagicalUsable
import com.e.app_namebattler.view.party.magic.IRecoveryMagic
import com.e.app_namebattler.view.party.magic.MagicData
import com.e.app_namebattler.view.view.music.SoundData

class JobPriest(name: String) : Player(name), IMagicalUsable, IRecoveryMagic {

    constructor(name: String, job: String, hp: Int, mp: Int, str: Int, def: Int, agi: Int, luck: Int) : this(name){
        makePlayer(name, job, hp, mp, str, def, agi, luck)
    }

    var isHeal = false

    override fun makeCharacter(name: String) {

        // 僧侶のパラメータを名前から生成する
        this.job = JobData.PRIEST.getJobName()
        this.hp = getNumber(120) + 80 // 80-200
        this.mp = getNumber(30) + 20 // 20-50
        this.str = getNumber(40) + 10 // 10-50
        this.def = getNumber(60) + 10 // 10-70
        this.luck = getNumber(99) + 1 // 1-100
        this.agi = getNumber(40) + 20 // 20-60
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

        if (this.isParalysis) {// 麻痺している場合

            log.append("${this.getName()}は麻痺で動けない！！\n")
            knockedDownCheck(defender)

        } else {// 麻痺していない場合
            if (hasEnoughMp()) {
                damage = effect(defender)
                knockedDownCheck(defender)
            } else {
                log.append("{getName()}は魔法を唱えようとしたが、MPが足りない！！\n")
                normalAttack(defender)
            }
        }
        return log
    }

    override fun healingMagic(defender: Player): StringBuilder {

        log.clear()

        if (this.isParalysis) {// 麻痺している場合

            log.append("${this.getName()}は麻痺で動けない！！\n")
            knockedDownCheck(this)

        } else {// 麻痺していない場合

            isHeal = true

            if (hasEnoughMp()) { // MPが20以上の場合ヒールを使用
                this.mp = this.mp - MagicData.HEAL.getMpCost() // MP消費
                log.append("${this.getName()}は${MagicData.HEAL.getName()}を唱えた！\n光が${defender.getName()}を包む\n")
                setAttackSoundEffect(SoundData.S_HEAL01.getSoundNumber())

                recoveryProcess(
                    defender, MagicData.HEAL
                        .getRecoveryValue()
                )

            } else { // MPが20未満の場合
                log.append("${this.getName()}はヒールを唱えようとしたが、MPが足りない！！\n")
                log.append("${this.getName()}の攻撃！\n錫杖を振りかざした！\n")
                setAttackSoundEffect(SoundData.S_PUNCH01.getSoundNumber())
                damage = calcDamage(defender) // 与えるダメージを求める
                super.damageProcess(defender, damage) // ダメージ処理
            }
            knockedDownCheck(this)
            isHeal = false
        }
        return log
    }

    private fun effect(defender: Player): Int {
        if (!defender.isPoison && !defender.isParalysis) { // 相手が毒,麻痺状態にかかっていない場合
            if ((1..2).random() == 1) { // 乱数1の場合パライズを使用
                useParalysis(defender)
            } else { // 乱数2の場合ポイズンを使用
                usePoison(defender)
            }

        } else if (defender.isPoison || defender.isParalysis) { // 相手が毒または麻痺にかかっている場合

            if (defender.isPoison) { // 相手が毒にかかっている場合パライズを使う
                useParalysis(defender)
            } else if (defender.isParalysis) { // 相手が麻痺にかかっている場合パライズを使う
                usePoison(defender)
            }
        }
        return damage
    }

    private fun usePoison(defender: Player) {
        this.mp = this.mp - MagicData.POISON.getMpCost() // MP消費
        log.append("${this.getName()}は${MagicData.POISON.getName()}を唱えた！\n瘴気が相手を包んだ！\n")
        defender.isPoison = true
        log.append("${defender.getName()}は毒状態になった！\n")
        setAttackSoundEffect(SoundData.S_POISON01.getSoundNumber())
    }

    private fun useParalysis(defender: Player) {
        this.mp = this.mp - MagicData.PARALYSIS.getMpCost() // MP消費
        log.append("${this.getName()}は${MagicData.PARALYSIS.getName()}を唱えた！\n蒼い霧が相手を包んだ！\n")
        setAttackSoundEffect(SoundData.S_PARALYSIS01.getSoundNumber())

        if ((1..100).random() <= MagicData.PARALYSIS.getContinuousRate()) { // 乱数がPARALYSISの値以下の場合麻痺状態になる
            defender.isParalysis = true
            log.append("${defender.getName()}は麻痺を受けた！\n")
        } else { // 麻痺を状態にならなかった場合
            log.append("${defender.getName()}は麻痺を受けなかった！\n")
        }
    }

    private fun hasEnoughMp(): Boolean {

        return if (PRIEST_USE_MAGIC_LOW_MP <= this.mp && !isHeal) {
            true
        } else PRIEST_USE_HEAL_MAGIC_LOW_MP <= this.mp && isHeal
    }
}
