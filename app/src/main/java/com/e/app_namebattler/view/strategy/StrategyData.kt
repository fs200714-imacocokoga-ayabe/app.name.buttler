package com.e.app_namebattler.view.strategy

enum class StrategyData(

    private val strategyNumber: Int,
    private val strategyName: String,
    private val strategyComment: String) {

    S0(0,"武器でたたかおう", "MPを消費しないで攻撃をします　　　　　　（全員）"),
    S1(1,"攻撃魔法をつかおう", "MPを消費して魔法攻撃します　　　　　　　（魔法使い、僧侶、忍者）"),
    S2(2,"スキルをつかおう", "MPを消費しないで特別な攻撃をします　　　（全員、確率発動）"),
    S3(3,"回復魔法をつかおう", "MPを消費して回復魔法を使用します　　　　（僧侶)"),
    S4(4,"薬草をつかおう", "一定確率で毒やHPを回復できます　　　　　（全員）");

    fun getStrategyNumber(): Int{
        return strategyNumber
    }
    fun getStrategyName(): String{
        return  strategyName
    }

    fun getStrategyComment(): String{
        return strategyComment
    }

}
