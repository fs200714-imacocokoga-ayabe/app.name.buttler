package com.e.app_namebattler

class Ninja (name:String):Player(name){

    constructor(name: String,job: String,hp: Int,mp: Int,str: Int,def: Int,agi: Int,luck: Int): this(name)

    override fun makeCharacter(name: String) {
        // 忍者のパラメータを名前から生成する
        this.hp = getNumber(0, 100) + 70 // 70-170
        this.mp = getNumber(1, 20) + 10 // 10-30
        this.str = getNumber(2, 50) + 20 // 20-70
        this.def = getNumber(3, 49) + 1  // 1-50
        this.luck = getNumber(4, 99) + 1 // 1-100
        this.agi = getNumber(5, 40) + 40 // 40-80
    }

    /**
     * 対象プレイヤーに攻撃を行う
     * @param defender
     * : 対象プレイヤー
     */
    override fun attack(defender: Player?): MutableList<String> {

        if (!isParalysis) { // 麻痺していない
            when ((1..5).random()) {
                1 -> if (defender != null) {// 直接攻撃,魔法攻撃
                    baseAttack(defender)
                }

                2 -> if (defender != null) {// 回復優先
                    recoveryPreferred(defender)
                }

                3 -> if (defender != null) {// 直接攻撃
                    directAttack(defender)
                }

                4 -> if (defender != null) {// 魔法攻撃,直接攻撃
                    magicAttack(defender)
                }

                5 -> if (defender != null) {// スキル攻撃
                    skillAttack(defender)
                }
            }
        } else { // 麻痺している
            System.out.printf("%sは麻痺で動けない！！\n", getName())
            battleMessageRecord.add("${getName()}は麻痺で動けない！！\n")
        }
        super.fall(defender!!) // 倒れ判定

        return battleMessageRecord
    }

    /**
     * 自身に乱数0～2の処理をする
     * @param defender
     * :自身
     */
    private fun recoveryPreferred(defender: Player) {
        if (isPoison) { // 毒状態の場合
            super.eatGrass() // 草を食べる
        } else {
            baseAttack(defender) // 基本攻撃
        }
    }

    /**
     * 対象プレイヤーに基本攻撃(直接攻撃,魔法攻撃)をする
     *
     * @param defender
     * : 対象プレイヤー
     */
    private fun baseAttack(defender: Player) { // 基本攻撃処理
        if (getMP() >= 10) { // MPがあれば術を使用
            useScroll(defender) // 術を使用
        } else { // MPがない場合
            //     type = "A"
            System.out.printf("%sの攻撃！\n刀で斬りつけた！\n", getName())
            battleMessageRecord.add("${getName()}の攻撃！\n刀で斬りつけた！\n")
            damage = calcDamage(defender) // 与えるダメージを求める
            super.damageProcess(defender, damage) // ダメージ処理
        }
    }

    /**
     * 対象プレイヤーに直接攻撃する
     *
     * @param defender
     * : 対象プレイヤー
     */
    private fun directAttack(defender: Player) {
        // type = "A"
        System.out.printf("%sの攻撃！\n手裏剣を投げつけた！\n", getName())
        battleMessageRecord.add("${getName()}の攻撃！\n手裏剣を投げつけた！\n")
        damage = calcDamage(defender) // 与えるダメージを求める
        super.damageProcess(defender, damage) // ダメージ処理
    }

    /**
     * 対象プレイヤーに魔法攻撃する
     *
     * @param defender
     * : 対象プレイヤー
     */
    private fun magicAttack(defender: Player) {
        //type = "M"
        if (getMP() >= 10) { // MPがあれば術を使用
            useScroll(defender) // 術を使用
        } else { // MPがない場合
            //  type = "A"
            System.out.printf("%sの攻撃！\n刀で突きさした！\n", getName())
            battleMessageRecord.add("${getName()}の攻撃！\n刀で突きさした！\n")
            damage = calcDamage(defender) // 与えるダメージを求める
            super.damageProcess(defender, damage) // ダメージ処理
        }
    }

    /**
     * 対象プレイヤーにスキル攻撃する
     *
     * @param defender
     * : 対象プレイヤー
     */
    private fun skillAttack(defender: Player) { // スキル攻撃処理
        // type = "A"
        damage = 0

        val r = (1..100).random()

        if (r > 75) { // 25%で発動

            print("${getName()}は目にも止まらぬ速さで攻撃した！\n")
            battleMessageRecord.add("${getName()}は目にも止まらぬ速さで攻撃した！\n")

            for (i in 1..2) {
                System.out.printf("%d回目の攻撃\n", i)
                battleMessageRecord.add("${i}回目の攻撃\n")
                damage = calcDamage(defender) // 攻撃処理
                super.damageProcess(defender, damage) // ダメージ処理
                if (defender.getHP() <= 0) { // 倒れた判定
                    break
                }
            }
        } else { // 75%で不発
            System.out.printf("%sは転んだ！\n", getName())
            battleMessageRecord.add("${getName()}は転んだ！\n")
        }
    }

    /**
     * 火遁の術(10-30)を使う
     *
     * @param defender
     * : 対象プレイヤー
     */
    private fun useScroll(defender: Player) {
        //  type = "M"

        damage = (((0.. Magic.FIREROLL.getMaxDamage()).random()
                - Magic.FIREROLL.getMinDamage())
                + Magic.FIREROLL.getMinDamage()) // 乱数10～30

        this.mp = this.getMP() - Magic.FIREROLL.getMpCost() // MP消費

        System.out.printf("%sは%sを唱えた！\n火の球が飛んでいく！\n", getName(),
            Magic.FIREROLL.getName())
        battleMessageRecord.add("${getName()}は${Magic.FIREROLL.getName()}を唱えた！\n火の球が飛んでいく！\n")
        // super.damageProcess(type, defender, damage) // ダメージ処理
        super.damageProcess(defender, damage) // ダメージ処理
    }


}