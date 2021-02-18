package com.e.app_namebattler

class JobFighter(name: String):Player(name) {

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

    override fun attack(defender: Player, strategyNumber: Int): StringBuilder {
        super.attack(defender, strategyNumber)

        return bsb
    }

    override fun normalAttack(defender: Player) {
        bsb.append("${getName()}の攻撃！\n${getName()}は剣で斬りつけた！\n")
        damage = calcDamage(defender) // 与えるダメージを求める
        damageProcess(defender, damage)
    }

    override fun skillAttack(defender: Player) { // スキル攻撃処理

        val r = (1..100).random()
        if (r > 75) { // 乱数値が75より大きいなら

            bsb.append("${getName()}の捨て身の突撃！\n")
            damage = calcDamage(defender) // 与えるダメージを求める
            damage *= 2 // ダメージ2倍

            super.damageProcess(defender, damage) // ダメージ処理

        } else {

            bsb.append("${getName()}の捨て身の突撃はかわされた！！\n")
        }
    }
}