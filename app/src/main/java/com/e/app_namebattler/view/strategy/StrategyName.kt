package com.e.app_namebattler.view.strategy

enum class StrategyName(

    private val strategyNumber: Int,
    private val strategyName: String) {

    S0(0,"武器でたたかおう"),
    S1(1,"攻撃魔法をつかおう"),
    S2(2,"スキルをつかおう"),
    S3(3,"回復魔法をつかおう"),
    S4(4,"薬草をつかおう");

    fun getStrategyNumber(): Int{
        return strategyNumber
    }
    fun getStrategyName(): String{
        return  strategyName
    }

}
