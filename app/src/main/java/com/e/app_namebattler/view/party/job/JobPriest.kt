package com.e.app_namebattler.view.party.job

import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.party.magic.IMagicalUsable
import com.e.app_namebattler.view.party.magic.IRecoveryMagic
import com.e.app_namebattler.view.party.magic.Magic

class JobPriest (name:String): Player(name), IMagicalUsable, IRecoveryMagic {

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

    var isHeal = false

    override fun makeCharacter(name: String) {
        // 僧侶のパラメータを名前から生成する
        this.job = "僧侶"
        this.hp = getNumber(0, 120) + 80 // 80-200
        this.mp = getNumber(1, 30) + 20 // 20-50
        this.str = getNumber(2, 40) + 10 // 10-50
        this.def = getNumber(3, 60) + 10 // 10-70
        this.luck = getNumber(4, 99) + 1 // 1-100
        this.agi = getNumber(5, 40) + 20 // 20-60
    }

    override fun normalAttack(defender: Player): StringBuilder {

        log.clear()

        log.append("${getName()}の攻撃！\n錫杖で突いた！\n")
        damage = calcDamage(defender) // 与えるダメージを求める
        damageProcess(defender, damage) // ダメージ処理
        knockedDownCheck(defender)
        return log
    }

    override fun skillAttack(defender: Player): StringBuilder {

        log.clear()

        if ((1..100).random() < Magic.OPTICAL_ELEMENTAL.getInvocationRate()) {
            log.append("${getName()}は祈りを捧げて${Magic.OPTICAL_ELEMENTAL.getName()}を召還した\n${Magic.OPTICAL_ELEMENTAL.getName()}の祝福を受けた！\n")
            recoveryProcess(this, Magic.OPTICAL_ELEMENTAL.getRecoveryValue())
        } else {
            log.append("${getName()}は祈りを捧げたが何も起こらなかった！\n")
        }
        knockedDownCheck(this)
        return log
    }

    override fun magicAttack(defender: Player): StringBuilder {

        log.clear()

        if (hasEnoughMp()) {
            damage = effect(defender)
            knockedDownCheck(defender)
        } else {
            log.append("{getName()}は魔法を唱えようとしたが、MPが足りない！！\n")
            normalAttack(defender)
        }
        return log
    }

    override fun healingMagic(defender: Player): StringBuilder {

        isHeal = true

        if (hasEnoughMp()) { // MPが20以上の場合ヒールを使用
            this.mp = this.getMP() - Magic.HEAL.getMpCost() // MP消費
                        log.append("${getName()}は${Magic.HEAL.getName()}を唱えた！\n光が${getName()}を包む\n")

            recoveryProcess(
                defender, Magic.HEAL
                    .getRecoveryValue()
            )
            knockedDownCheck(this)
        } else { // MPが20未満の場合
            log.append("{getName()}はヒールを唱えようとしたが、MPが足りない！！\n")
            log.append("${getName()}の攻撃！\n錫杖を振りかざした！\n")
            damage = calcDamage(defender) // 与えるダメージを求める
            super.damageProcess(defender, damage) // ダメージ処理
            knockedDownCheck(defender)
        }
        isHeal = false
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
        this.mp = this.getMP() - Magic.POISON.getMpCost() // MP消費
        log.append("${getName()}は${Magic.POISON.getName()}を唱えた！\n瘴気が相手を包んだ！\n")
        defender.isPoison = true
        log.append("${defender.getName()}は毒状態になった！\n")
    }

    private fun useParalysis(defender: Player) {
        this.mp = this.getMP() - Magic.PARALYSIS.getMpCost() // MP消費
        log.append("${getName()}は${Magic.PARALYSIS.getName()}を唱えた！\n蒼い霧が相手を包んだ！\n")

        if ((1..100).random() <= Magic.PARALYSIS.getContinuousRate()) { // 乱数がPARALYSISの値以下の場合麻痺状態になる
            defender.isParalysis = true
            log.append("${defender.getName()}は麻痺を受けた！\n")
        } else { // 麻痺を状態にならなかった場合
            log.append("${defender.getName()}は麻痺を受けなかった！\n")
        }
    }

    private fun hasEnoughMp(): Boolean {
        return if (10 <= this.getMP() && !isHeal) {
            true
        } else 20 <= this.getMP() && isHeal
    }
}
