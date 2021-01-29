package com.e.app_namebattler

class Priest (name:String):Player(name), IRecoveryMagic {

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

    override fun attack(defender: Player, strategyNumber: Int): StringBuilder {
        super.attack(defender, strategyNumber)
        return bsb
    }

    override fun normalAttack(defender: Player) {
        bsb.append("${getName()}の攻撃！\n錫杖で突いた！\n")
        damage = calcDamage(defender) // 与えるダメージを求める
        damageProcess(defender, damage) // ダメージ処理
    }

    override fun skillAttack(defender: Player) {
        val r: Int = random.nextInt(100) + 1
        if (r > 50) {
            bsb.append("${getName()}は祈りを捧げて${Magic.OPTICALELEMENTAL.getName()}を召還した\n${Magic.OPTICALELEMENTAL.getName()}の祝福を受けた！\n")
            bsb.append("${getName()}はHPが${recoveryProcess(this, Magic.OPTICALELEMENTAL.getRecoveryValue())}回復した！\n")

        } else {
            bsb.append("${getName()}は祈りを捧げたが何も起こらなかった！\n")
        }
    }

    override fun magicAttack(defender: Player) {
        if (hasEnoughMp()) {
            damage = effect(defender)
        } else {
            bsb.append("MPが足りない！")
            normalAttack(defender)
        }
    }

    override fun healingMagic(defender: Player) {
        isHeal = true
        if (hasEnoughMp()) { // MPが20以上の場合ヒールを使用
            this.mp = this.getMP() - Magic.HEAL.getMpCost() // MP消費
                        bsb.append("${getName()}は${Magic.HEAL.getName()}を唱えた！\n光が${getName()}を包む\nHPが${Magic.HEAL
                .getRecoveryValue()}回復した！\n")

            recoveryProcess(
                defender, Magic.HEAL
                    .getRecoveryValue()
            )
        } else { // MPが20未満の場合
           bsb.append("${getName()}の攻撃！\n錫杖を振りかざした！\n")
            damage = calcDamage(defender) // 与えるダメージを求める
            super.damageProcess(defender, damage) // ダメージ処理
        }
        isHeal = false
    }

    override fun eatGrass() {
        super.eatGrass()
    }

    private fun effect(defender: Player): Int {
        if (!defender.isPoison && !defender.isParalysis) { // 相手が毒,麻痺状態にかかっていない場合
            val magic: Int = random.nextInt(2) + 1 // 乱数1～2
            if (magic == 1) { // 乱数1の場合パライズを使用
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
        bsb.append("${getName()}は${Magic.POISON.getName()}を唱えた！\n瘴気が相手を包んだ！\n")
        defender.isPoison = true
        bsb.append("${defender.getName()}は毒状態になった！\n")
    }

    private fun useParalysis(defender: Player) {
        this.mp = this.getMP() - Magic.PARALYSIS.getMpCost() // MP消費
        bsb.append("${getName()}は${Magic.PARALYSIS.getName()}を唱えた！\n蒼い霧が相手を包んだ！\n")
        if (random.nextInt(100) + 1 <= Magic.PARALYSIS.getContinuousRate()) { // 乱数がPARALYSISの値以下の場合麻痺状態になる
            defender.isParalysis = true
            bsb.append("${defender.getName()}は麻痺を受けた！\n")
        } else { // 麻痺を状態にならなかった場合
            bsb.append("${defender.getName()}は麻痺を受けなかった！\n")
        }
    }

    private fun hasEnoughMp(): Boolean {
        return if (this.getMP() >= 10 && !isHeal) {
            true
        } else this.getMP() >= 20 && isHeal
    }
}
