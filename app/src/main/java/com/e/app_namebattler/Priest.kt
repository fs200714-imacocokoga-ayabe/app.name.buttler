package com.e.app_namebattler

class Priest (name:String):Player(name){

    constructor(name: String,job: String,hp: Int,mp: Int,str: Int,def: Int,agi: Int,luck: Int): this(name)

    override fun makeCharacter(name: String) {
        // 僧侶のパラメータを名前から生成する
        this.hp = getNumber(0, 120) + 80 // 80-200
        this.mp = getNumber(1, 30) + 20 // 20-50
        this.str = getNumber(2, 40) + 10 // 10-50
        this.def = getNumber(3, 60) + 10 // 10-70
        this.luck = getNumber(4, 99) + 1 // 1-100
        this.agi = getNumber(5, 40) + 20 // 20-60
    }

    /**
     * 対象プレイヤーに攻撃を行う
     * @param defender : 対象プレイヤー
     */
    override fun attack(defender: Player?): StringBuilder {

        bsb.clear()

        if (!isParalysis) { // 麻痺していない場合
            when ((1..5).random()) {

                1 -> if (defender != null) {// 直接攻撃,回復魔法,攻撃魔法
                    baseAttack(defender)
                }

                2 -> if (defender != null) {// 回復魔法,直接攻撃
                    recoveryPreferred(defender)
                }

                3 -> if(defender != null) {// 直接攻撃
                    directAttack(defender)
                }

                4 -> if (defender != null) {// 直接攻撃,攻撃魔法
                    magicAttack(defender)
                }

                5 -> if(defender != null) {// スキル攻撃
                    skillAttack()
                }
            }
        } else { // 麻痺している

       //     System.out.printf("%sは麻痺で動けない！！\n", getName())
            bsb.append("${getName()}は麻痺で動けない！！\n")
        }
        super.fall(defender!!) // 倒れた判定

        return java.lang.StringBuilder()
    }

    /**
     * 自身に乱数0～2の処理をする
     * @param defender :自身
     */
    private fun recoveryPreferred(defender: Player?) {
        if (isPoison) { // 毒状態の場合
            super.eatGrass() // 草を食べる
        } else {
            if (getMP() >= 20) { // MPが20以上の場合ヒールを使用

                mp = getMP() - Magic.HEAL.getMpCost() // MP消費
//                System.out.printf(
//                    "%sはヒールを唱えた！\n光が%sを包んだ\n%sはHP%d回復した！\n",
//                    getName(),
//                    defender!!.getName(),
//                    defender.getName(),
//                    recovery(defender, Magic.HEAL
//                        .getRecoveryValue()))
                if (defender != null) {
                    bsb.append("${getName()}はヒールを唱えた！\n光が${defender.getName()}を包んだ\n${defender.getName()}はHP${recovery(defender, Magic.HEAL.getRecoveryValue())}回復した！\n")
                }
            } else { // MPが20未満の場合
                // type = "A"
//                System.out.printf("%sの攻撃！\n錫杖を振りかざした！\n", getName())
                bsb.append("${getName()}の攻撃！\n錫杖を振りかざした！\n")

                damage = calcDamage(defender!!) // 与えるダメージを求める
                super.damageProcess(defender, damage) // ダメージ処理
            }
        }
    }

    /**
     * 対象プレイヤーに直接攻撃する
     * @param defender  : 対象プレイヤー
     */
    private fun directAttack(defender: Player?) { // 直接攻撃処理
        //  type = "A"
      //  System.out.printf("%sの攻撃！\n錫杖で突いた！\n", getName())
        bsb.append("${getName()}の攻撃！\n錫杖で突いた！\n")
        damage = calcDamage(defender!!) // 与えるダメージを求める
        super.damageProcess(defender, damage) // ダメージ処理
    }

    /**
     * 対象プレイヤーに基本攻撃(直接攻撃,回復魔法,攻撃魔法)をする
     * @param defender : 対象プレイヤー
     */
    private fun baseAttack(defender: Player?) { // 基本攻撃処理

        // MPが20以上30未満またはMPが10以上で相手が麻痺または毒にかかっていない場合
        if (getMP() >= 20 && getHP() < 30
            || getMP() >= 10 && (!defender!!.isParalysis || !defender
                .isPoison)) {
            useMagic(defender) // 魔法を使用
        } else { // 通常攻撃
            // type = "A"
        //    System.out.printf("%sの攻撃！\nの攻撃！\n錫杖で叩いた！\n", getName())
            bsb.append("${getName()}の攻撃！\n錫杖で叩いた！\n")
            damage = calcDamage(defender!!) // 与えるダメージを求める
            super.damageProcess(defender, damage) // ダメージ処理
        }
    }

    /**
     * 対象プレイヤーに魔法攻撃する
     * @param defender : 対象プレイヤー
     */
    private fun magicAttack(defender: Player?) { // 魔法攻撃処理

        // MPが20以上30未満またはMPが10以上で相手が麻痺または毒にかかっていない場合
        if (getMP() >= 10 && (!defender!!.isParalysis || !defender.isPoison)) {
            attackMagic(defender) // 魔法を使用
        } else { // 通常攻撃
            //    type = "A"
          //  System.out.printf("%sの攻撃！\n錫杖を振り回した！！\n", getName())
            bsb.append("${getName()}の攻撃！\n錫杖を振り回した！！\n")
            damage = calcDamage(defender!!) // 与えるダメージを求める
            super.damageProcess(defender, damage) // ダメージ処理
        }
    }

    /**
     *光の精霊を召喚する
     */
    private fun skillAttack() { // スキル攻撃処理
        //type = "M"
        val r = (1..100).random()

        if (r > 50) {

         //   System.out.printf("%sは祈りを捧げて%sを召還した\n%sの祝福を受けた！\n", getName(),
          //      Magic.OPTICALELEMENTAL.getName(), Magic.OPTICALELEMENTAL.getName())
            bsb.append("${getName()}%sは祈りを捧げて${Magic.OPTICALELEMENTAL.getName()}を召還した\n${Magic.OPTICALELEMENTAL.getName()}%sの祝福を受けた！\n")
          //  System.out.printf("%sはHPが%d回復した！\n", getName(),
         //       recovery(this, Magic.OPTICALELEMENTAL.getRecoveryValue()))
            bsb.append("${getName()}はHPが${recovery(this, Magic.OPTICALELEMENTAL.getRecoveryValue())}回復した！\n")
        } else {
         //   System.out.printf("%sは祈りを捧げたが何も起こらなかった！\n", getName())
            bsb.append("${getName()}は祈りを捧げたが何も起こらなかった！\n")
        }
    }

    /**
     * 対象プレイヤーに魔法攻撃する
     * @param defender :対象プレイヤー
     */
    private fun attackMagic(defender: Player?) { // 魔法攻撃処理
        if (!defender!!.isPoison && !defender.isParalysis) { // 相手が毒,麻痺状態にかかっていない場合
            val magic = (1..2).random() // 乱数1～2
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
    }

    /**
     * 魔法を使う
     * @param defender : 対象プレイヤー
     */
    private fun useMagic(defender: Player?) {
        if (getHP() < 30 && getMP() >= 20) { // 自身のHPが30未満でMPが20以上の場合ヒールを使用

            mp = getMP() - Magic.HEAL.getMpCost() // MP消費
            hp = getHP() + Magic.HEAL.getRecoveryValue() // healの数値回復
          //  System.out.printf("%sは%sを唱えた！\n光が%sを包む\nHPが%d回復した！\n", getName(),
          //      Magic.HEAL.getName(), getName(), Magic.HEAL
          //          .getRecoveryValue())
            bsb.append("${getName()}は${Magic.HEAL.getName()}を唱えた！\n光が${getName()}を包む\nHPが${Magic.HEAL
                .getRecoveryValue()}回復した！\n")
        } else {
            attackMagic(defender) // 魔法を使用
        }
    }

    /**
     * パライズを使う
     * @param defender : 対象プレイヤー
     */
    private fun useParalysis(defender: Player?) { // 魔法パライズ処理

        mp = getMP() - Magic.PARALYSIS.getMpCost() // MP消費
        //System.out.printf("%sは%sを唱えた！\n蒼い霧が相手を包んだ！\n", getName(), Magic.PARALYSIS
         //   .getName())
        bsb.append("${getName()}は${Magic.PARALYSIS.getName()}を唱えた！\n蒼い霧が相手を包んだ！\n")
        if ((1..100).random() <= Magic.PARALYSIS.getContinuousRate()) { // 乱数がPARALYSISの値以下の場合麻痺状態になる
            defender!!.isParalysis = true // 相手に麻痺をセット
          //  System.out.printf("%sは麻痺を受けた！\n", defender.getName())
            bsb.append("${defender.getName()}は麻痺を受けた！\n")
        } else { // 麻痺を状態にならなかった場合
           // System.out.printf("%sは麻痺を受けなかった！\n", defender!!.getName())
            if (defender != null) {
                bsb.append("${defender.getName()}は麻痺を受けなかった！\n")
            }
        }
    }

    /**
     * ポイズンを使う
     * @param defender : 対象プレイヤー
     */
    private fun usePoison(defender: Player?) { // 魔法ポイズン処理

        mp = getMP() - Magic.POISON.getMpCost() // MP消費
       // System.out.printf("%sは%sを唱えた！\n瘴気が相手を包んだ！\n", getName(), Magic.POISON.getName())
        bsb.append("${getName()}は${Magic.POISON.getName()}を唱えた！\n瘴気が相手を包んだ！\n")
        defender!!.isPoison = true // 相手に毒をセット
      //  System.out.printf("%sは毒状態になった！\n", defender.getName())
        bsb.append("${defender.getName()}は毒状態になった！\n")
    }

}