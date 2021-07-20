package com.e.app_namebattler.view.party.skill

enum class SkillData(

    private val skillName: String,
    private val invocationRate: Int,
    private val damageRate: Int,
    private val recoveryValue: Int
) {

    ASSAULT("捨て身の突撃", 35, 2, 0),
    SWALLOW("燕返し", 35, 0, 0),
    FIRE_ELEMENTAL("炎の精霊", 40, 60, 0),
    OPTICAL_ELEMENTAL("光の精霊", 40,  0,  70);

    fun getSkillName(): String {
        return skillName
    }

    fun getInvocationRate(): Int {
        return invocationRate
    }

    fun getDamageRate(): Int {
        return damageRate
    }

    fun getRecoveryValue(): Int{
        return recoveryValue
    }
}
