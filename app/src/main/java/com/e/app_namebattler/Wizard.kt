package com.e.app_namebattler

class Wizard (name:String):Player(name){

    constructor(name: String,job: String,hp: Int,mp: Int,str: Int,def: Int,agi: Int,luck: Int): this(name)

    override fun makeCharacter(name: String) {
        // 魔法使いのパラメータを名前から生成する
        this.hp = getNumber(0, 100) + 50 // 50-150
        this. mp = getNumber(1, 50) + 30 // 30-80
        this.str = getNumber(2, 49) + 1 // 1-50
        this.def = getNumber(3, 49) + 1 // 1-50
        this.luck = getNumber(4, 99) + 1 // 1-100
        this.agi = getNumber(5, 40) + 20 // 20-60
    }

    /**
     * 対象プレイヤーに攻撃を行う
     * @param defender : 対象プレイヤー
     * @param strategy : 作戦番号
     */
    override fun attack(defender: Player?): MutableList<String> {

        if (!isParalysis) { // 麻痺していない場合
            when ((1..5).random()) {
                1 -> if (defender != null) {// 魔法攻撃,直接攻撃
                    baseAttack(defender)
                }

                2 ->if (defender != null) {// 回復優先
                    recoveryPreferred(defender)
                }

                3 -> if (defender != null) {// 直接攻撃
                    directAttack(defender)
                }

                4 -> if (defender != null) {// 魔法攻撃,直接攻撃
                    baseAttack(defender)
                }

                5 -> if(defender != null) {// スキル攻撃
                    skillAttack(defender)
                }
            }
        } else { // 麻痺している場合
            System.out.printf("%sは麻痺で動けない！！\n", getName())
            battleMessageRecord.add("${getName()}は麻痺で動けない！！\n")
        }
        super.fall(defender!!) // 倒れた判定

        return battleMessageRecord
    }

    /**
     * 自身に乱数0～2の処理をする
     * @param defender :自身
     */
    private fun recoveryPreferred(defender: Player?) {
        if (isPoison) { // 毒状態の場合
            super.eatGrass() // 草を食べる
        } else { // 毒にかかっていない場合
            baseAttack(defender) // 直接攻撃
        }
    }

    /**
     * 対象プレイヤーに基本攻撃(魔法と直接攻撃)をする
     * @param defender : 対象プレイヤー
     */
    private fun baseAttack(defender: Player?) {
        if (getMP() >= 10) { // MPがあれば魔法を使用
            //  type = "M" // 攻撃タイプ(魔法攻撃)
            damage = useMagic()
        } else { // MPがない場合
            // type = "A"
            System.out.printf("%sの攻撃！\n%sは杖を振り回した！\n", getName(), getName())
            battleMessageRecord.add("${getName()}の攻撃！\n${getName()}は杖を振り回した！\n")
            damage = calcDamage(defender!!) // 与えるダメージを求める
        }
        super.damageProcess(defender!!, damage) // ダメージ処理
    }

    /**
     * 対象プレイヤーに直接攻撃する
     * @param defender : 対象プレイヤー
     */
    private fun directAttack(defender: Player?) {
        //  type = "A" // 直接攻撃タイプ
        System.out.printf("%sの攻撃！\n%sは杖を投げつけた！\n", getName(), getName())
        battleMessageRecord.add("${getName()}の攻撃！\n${getName()}は杖を投げつけた！\n")
        damage = calcDamage(defender!!) // 与えるダメージを求める
        super.damageProcess(defender, damage) // ダメージ処理
    }

    /**
     * 対象プレイヤーにスキル攻撃する
     * @param defender : 対象プレイヤー
     */
    private fun skillAttack(defender: Player?) {
        //  type = "M" // 魔法攻撃タイプ
        val r = (1..100).random()

        if (r > 75) { // 25％で発動

            print("${getName()}は魔法陣を描いて${Magic.FIREELEMENTAL.getName()}を召還した\n${getName()}の攻撃！\n")
            battleMessageRecord.add("${getName()}は魔法陣を描いて${Magic.FIREELEMENTAL.getName()}を召還した\n${getName()}の攻撃！\n")

            super.damageProcess(defender!!, Magic.FIREELEMENTAL.getMinDamage()) // ダメージ処理

        } else { // 75%で不発

            System.out.printf("%sの攻撃だがスキルは発動しなかった！\n", getName())
            battleMessageRecord.add("${getName()}の攻撃だがスキルは発動しなかった！\n")
        }
    }

    /**
     * 魔法攻撃する
     * @return 求めたダメージ
     */
    private fun useMagic(): Int {

        if (getMP() >= 20) { // MPが20以上の場合

            val r = (1..2).random() // 乱数1～2

            if (r == 1) { // 1の場合サンダーを使用
                damage = useThunder()
            } else if (r == 2) { // 2の場合ファイアを使用
                damage = useFire()
            }
        } else if (getMP() in 10..19) { // MPが10以上20未満の場合ファイアを使用する
            damage = useFire() // ファイアを使用
        }
        return damage
    }

    /**
     * 魔法ファイアを使う
     * @return ダメージ値(10-30)
     */
    private fun useFire(): Int {

        print("${getName()}は${Magic.FIRE.getName()}を唱えた！\n炎が渦を巻いた！\n")
        battleMessageRecord.add("${getName()}は${Magic.FIRE.getName()}を唱えた！\n炎が渦を巻いた！\n")

        damage = (((0..Magic.FIRE.getMaxDamage()).random()
                - Magic.FIRE.getMinDamage())
                + Magic.FIRE.getMinDamage()) // 乱数10～30
        mp = getMP() - Magic.FIRE.getMpCost() // MPを消費
        return damage
    }

    /**
     * 魔法サンダーを使う
     * @return ダメージ値(20-50)
     */
    private fun useThunder(): Int { // 魔法サンダーの処理

        System.out.printf("%sは%sを唱えた！\n雷が地面を這っていく！\n", getName(), Magic.THUNDER
            .getName())
        battleMessageRecord.add("${getName()}は${Magic.THUNDER.getName()}を唱えた！\n雷が地面を這っていく！\n")
        damage = (((0..Magic.THUNDER.getMaxDamage()).random()
                - Magic.THUNDER.getMinDamage())
                + Magic.THUNDER.getMinDamage()) // 乱数20～50

        mp = getMP() - Magic.THUNDER.getMpCost() // MPを消費
        return damage
    }


}