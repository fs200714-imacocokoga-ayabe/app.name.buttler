package com.e.app_namebattler.view.party.job

import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.party.skill.Skill

class JobFighter(name: String): Player(name) {

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

    override fun makeCharacter(name: String) {
        // 戦士のパラメータを名前から生成する
        this.job = "戦士"
        this.hp = getNumber(0, 200) + 100 // 100-300
        this.mp = getNumber(1, 0) // 0
        this.str = getNumber(2, 70) + 30 // 30-100
        this.def = getNumber(3, 70) + 30 // 30-100
        this.luck = getNumber(4, 99) + 1 // 1-100
        this.agi = getNumber(5, 49) + 1 // 1-50
    }

    override fun normalAttack(defender: Player): StringBuilder {

        log.clear()

        log.append("${getName()}の攻撃！\n${getName()}は剣で斬りつけた！\n")

        damage = calcDamage(defender) // 与えるダメージを求める
        damageProcess(defender, damage)
        knockedDownCheck(defender)

        return log
    }

    override fun skillAttack(defender: Player): StringBuilder { // スキル攻撃処理

        log.clear()

        if ((1..100).random() < Skill.ASSAULT.getInvocationRate()) { // 乱数値が30より小さい時
            log.append("${getName()}の捨て身の突撃！\n")
            damage = calcDamage(defender) // 与えるダメージを求める
            damage *= 2 // ダメージ2倍
            super.damageProcess(defender, damage) // ダメージ処理

        } else {
            log.append("${getName()}の捨て身の突撃はかわされた！！\n")
        }
        knockedDownCheck(defender)
        return log
    }
}