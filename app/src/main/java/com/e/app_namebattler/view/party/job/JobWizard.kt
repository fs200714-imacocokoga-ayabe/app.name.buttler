package com.e.app_namebattler.view.party.job

import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.party.magic.IMagicalUsable
import com.e.app_namebattler.view.party.magic.Magic
import com.e.app_namebattler.view.view.music.SoundData

class JobWizard (name:String): Player(name), IMagicalUsable {

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

    override fun normalAttack(defender: Player): StringBuilder {

        log.clear()

        if (this.isParalysis){// 麻痺している場合

            log.append("${getName()}は麻痺で動けない！！\n")
            knockedDownCheck(defender)

        }else {// 麻痺していない場合

            log.append("${this.getName()}の攻撃！\n${getName()}は杖を振り回した！\n")
            damage = calcDamage(defender) // 与えるダメージを求める
            damageProcess(defender, damage)
            knockedDownCheck(defender)
            setAttackSoundEffect(SoundData.S_PUNCH01.getSoundNumber())
        }
        return log
    }

    override fun skillAttack(defender: Player): StringBuilder {

        log.clear()

        if (this.isParalysis){// 麻痺している場合

            log.append("${this.getName()}は麻痺で動けない！！\n")
            knockedDownCheck(defender)

        }else {// 麻痺していない場合

            if ((1..100).random() < Magic.FIRE_ELEMENTAL.getInvocationRate()) { // 40％で発動

                log.append("${this.getName()}の攻撃！\n${this.getName()}は魔法陣を描いて${Magic.FIRE_ELEMENTAL.getName()}を召還した！\n")
                setAttackSoundEffect(SoundData.S_FIRE01.getSoundNumber())

                super.damageProcess(
                    defender,
                    Magic.FIRE_ELEMENTAL.getMinDamage()
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

        if (this.isParalysis){// 麻痺している場合

            log.append("${this.getName()}は麻痺で動けない！！\n")
            knockedDownCheck(defender)

        }else {// 麻痺していない場合

            if (hasEnoughMp()) {
                damage = effect()
                super.damageProcess(defender, damage)
                knockedDownCheck(defender)
            } else {
                log.append("${this.getName()}は魔法を唱えようとしたが、MPが足りない！！\n")
                normalAttack(defender)
            }
        }
        return log
    }

    private fun effect(): Int {

        if (20 <= this.mp) { // MPが20以上の場合

            damage = if ((1..2).random() == 1) { // 1の場合サンダーを使用
                useThunder()
            } else { // 2の場合ファイアを使用
                useFire()
            }
        } else if (this.mp in 10..19) { // MPが10以上20未満の場合ファイアを使用する
            damage = useFire() // ファイアを使用
        }
        return damage
    }

    private fun useThunder(): Int {

        log.append("${this.getName()}は${Magic.THUNDER.getName()}を唱えた！\n雷が地面を這っていく！\n")
        damage = (Magic.THUNDER.getMinDamage()..Magic.THUNDER.getMaxDamage()).random() // 乱数20～50
        this.mp = this.mp - Magic.THUNDER.getMpCost() // MPを消費
        setAttackSoundEffect(SoundData.S_THUNDER01.getSoundNumber())
        return damage
    }

    private fun useFire(): Int {
      
        log.append("${this.getName()}は${Magic.FIRE.getName()}を唱えた！\n炎が渦を巻いた！\n")
        damage = (Magic.FIRE.getMinDamage()..Magic.FIRE.getMaxDamage()).random()// 乱数10～30
        this.mp = this.mp - Magic.FIRE.getMpCost() // MPを消費
        setAttackSoundEffect(SoundData.S_FIRE01.getSoundNumber())
        return damage
    }

    private fun hasEnoughMp(): Boolean {
        return 10 <= this.mp
    }
}
