package com.e.app_namebattler

class Ninja (name:String):Player(name) {

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
        // 忍者のパラメータを名前から生成する
        this.job = "忍者"
        this.hp = getNumber(0, 100) + 70 // 70-170
        this.mp = getNumber(1, 20) + 10 // 10-30
        this.str = getNumber(2, 50) + 20 // 20-70
        this.def = getNumber(3, 49) + 1  // 1-50
        this.luck = getNumber(4, 99) + 1 // 1-100
        this.agi = getNumber(5, 40) + 40 // 40-80
    }

    override fun attack(defender: Player, strategyNumber: Int): StringBuilder {
        super.attack(defender, strategyNumber)
        return bsb
    }

    override fun normalAttack(defender: Player) {
        bsb.append("${getName()}の攻撃！\n刀で突きさした！\n")
      //  bsb.append("${getName()}の攻撃！\n手裏剣を投げつけた！\n")
        damage = calcDamage(defender) // 与えるダメージを求める
        damageProcess(defender, damage) // ダメージ処理
    }

    override fun skillAttack(defender: Player) {
        val r: Int = random.nextInt(100) + 1
        damage = 0
        if (r > 75) { // 25%で発動
            bsb.append("${getName()}は目にも止まらぬ速さで攻撃した！\n")
            for (i in 1..2) {
                bsb.append("${i}回目の攻撃\n")
                damage = calcDamage(defender) // 攻撃処理
                super.damageProcess(defender, damage) // ダメージ処理
                if (defender.getHP() <= 0) { // 倒れた判定
                    break
                }
            }
        } else { // 75%で不発
            bsb.append("${getName()}は転んだ！\n")
        }
    }

    override fun magicAttack(defender: Player) {
        if (hasEnoughMp()) {
            damage = effect(defender)
            super.damageProcess(defender, damage)
        } else {
            bsb.append("MPが足りない！")
            normalAttack(defender)
        }
    }

    override fun eatGrass() {
        super.eatGrass()
    }

    private fun effect(defender: Player?): Int {
        damage = (random.nextInt(
            Magic.FIREROLL.getMaxDamage()
                    - Magic.FIREROLL.getMinDamage()
        )
                + Magic.FIREROLL.getMinDamage()) // 乱数10～30
        this.mp = this.getMP() - Magic.FIREROLL.getMpCost() // MP消費
        bsb.append("${getName()}は${Magic.FIREROLL.getName()}を唱えた！\n火の球が飛んでいく！\n")
        return damage
    }

    private fun hasEnoughMp(): Boolean {
        return this.getMP() >= 10
    }
}
