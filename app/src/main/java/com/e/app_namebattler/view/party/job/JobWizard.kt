package com.e.app_namebattler.view.party.job

import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.party.magic.IMagicalUsable
import com.e.app_namebattler.view.party.magic.Magic

class JobWizard (name:String): Player(name), IMagicalUsable {

//    init {
//        makeCharacter(name)
//    }
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
        // 魔法使いのパラメータを名前から生成する
        this.job = "魔法使い"
        this.hp = getNumber(0, 100) + 50 // 50-150
        this.mp = getNumber(1, 50) + 30 // 30-80
        this.str = getNumber(2, 49) + 1 // 1-50
        this.def = getNumber(3, 49) + 1 // 1-50
        this.luck = getNumber(4, 99) + 1 // 1-100
        this.agi = getNumber(5, 40) + 20 // 20-60
    }

    override fun attack(defender: Player, strategyNumber: Int): StringBuilder {
        super.attack(defender, strategyNumber)
        return log
    }

    override fun normalAttack(defender: Player) {

        log.append("${getName()}の攻撃！\n${getName()}は杖を振り回した！\n")
        damage = calcDamage(defender) // 与えるダメージを求める
        damageProcess(defender, damage)
    }

    override fun skillAttack(defender: Player) {

        if ((1..100).random() > 75) { // 25％で発動

            log.append("${getName()}は魔法陣を描いて${Magic.FIREELEMENTAL.getName()}を召還した\n${getName()}の攻撃！\n")

            super.damageProcess(
                defender,
                Magic.FIREELEMENTAL.getMinDamage()
            ) // ダメージ処理
        } else { // 75%で不発
            log.append("${getName()}の攻撃だがスキルは発動しなかった！\n")
        }
    }

    override fun magicAttack(defender: Player) {
        if (hasEnoughMp()) {
            damage = effect()
            super.damageProcess(defender, damage)
        } else {
            log.append("MPが足りない！")
            normalAttack(defender)
        }
    }

    private fun effect(): Int {

        if (getMP() >= 20) { // MPが20以上の場合

            damage = if ((1..2).random() == 1) { // 1の場合サンダーを使用
                useThunder()
            } else { // 2の場合ファイアを使用
                useFire()
            }
        } else if (getMP() in 10..19) { // MPが10以上20未満の場合ファイアを使用する
            damage = useFire() // ファイアを使用
        }
        return damage
    }

    private fun useThunder(): Int {

        log.append("${getName()}は${Magic.THUNDER.getName()}を唱えた！\n雷が地面を這っていく！\n")
        damage = (Magic.THUNDER.getMinDamage()..Magic.THUNDER.getMaxDamage()).random() // 乱数20～50
        this.mp = this.getMP() - Magic.THUNDER.getMpCost() // MPを消費
        return damage
    }

    private fun useFire(): Int {
      
        log.append("${getName()}は${Magic.FIRE.getName()}を唱えた！\n炎が渦を巻いた！\n")
        damage = (Magic.FIRE.getMinDamage()..Magic.FIRE.getMaxDamage()).random()// 乱数10～30
        this.mp = this.getMP() - Magic.FIRE.getMpCost() // MPを消費
        return damage
    }

    private fun hasEnoughMp(): Boolean {
        return this.getMP() >= 10
    }
}
