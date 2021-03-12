package com.e.app_namebattler.view.party.job

import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.party.magic.IMagicalUsable
import com.e.app_namebattler.view.party.magic.Magic
import com.e.app_namebattler.view.party.skill.Skill

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

        this.job = "忍者"
        this.hp = getNumber(0, 100) + 70 // 70-170
        this.mp = getNumber(1, 20) + 10 // 10-30
        this.str = getNumber(2, 50) + 20 // 20-70
        this.def = getNumber(3, 49) + 1  // 1-50
        this.luck = getNumber(4, 99) + 1 // 1-100
        this.agi = getNumber(5, 40) + 40 // 40-80
    }

    override fun normalAttack(defender: Player): StringBuilder {

        log.clear()

        if (this.isParalysis){// 麻痺している場合

            log.append("${getName()}は麻痺で動けない！！\n")
            knockedDownCheck(defender)

        }else {// 麻痺していない場合
            log.append("${getName()}の攻撃！\n刀で突きさした！\n")
            damage = calcDamage(defender) // 与えるダメージを求める
            damageProcess(defender, damage) // ダメージ処理
            knockedDownCheck(defender)
        }
        return log
    }

    override fun skillAttack(defender: Player): StringBuilder {

        log.clear()

        if (this.isParalysis){// 麻痺している場合

            log.append("${getName()}は麻痺で動けない！！\n")
            knockedDownCheck(defender)

        }else {// 麻痺していない場合

            if ((1..100).random() < Skill.SWALLOW.getInvocationRate()) { // 30%の確率で発動

                log.append("${getName()}は目にも止まらぬ速さで攻撃した！\n")

                for (i in 1..2) {
                    log.append("${i}回目の攻撃\n")
                    damage = calcDamage(defender) // 攻撃処理
                    super.damageProcess(defender, damage) // ダメージ処理

                    if (defender.getHP() <= 0) { // 倒れた判定
                        break
                    }
                }
            } else { // 70%で不発
                log.append("${getName()}は転んだ！\n")
            }
            knockedDownCheck(defender)
        }
        return log
    }

    override fun magicAttack(defender: Player): StringBuilder {

        log.clear()

        if (this.isParalysis){// 麻痺している場合

            log.append("${getName()}は麻痺で動けない！！\n")
            knockedDownCheck(defender)

        }else {// 麻痺していない場合

            if (hasEnoughMp()) {
                damage = effect()
                super.damageProcess(defender, damage)
                knockedDownCheck(defender)

            } else {
                log.append("${getName()}は術を唱えようとしたが、MPが足りない！！\n")
                normalAttack(defender)
            }
        }
        return log
    }

    private fun effect(): Int {

        damage = (Magic.FIRE_ROLL.getMinDamage()..Magic.FIRE_ROLL.getMaxDamage()).random() // 乱数10～30
        this.mp = this.getMP() - Magic.FIRE_ROLL.getMpCost() // MP消費
        log.append("${getName()}は${Magic.FIRE_ROLL.getName()}を唱えた！\n火の球が飛んでいく！\n")
        return damage
    }

    private fun hasEnoughMp(): Boolean {
        return 10 <= this.getMP()
    }
}
