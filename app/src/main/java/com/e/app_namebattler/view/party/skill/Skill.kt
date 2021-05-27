package com.e.app_namebattler.view.party.skill

enum class Skill (

    private val skillName: String,
    private val invocationRate: Int,
    private val damageRate: Int){

    ASSAULT("捨て身の突撃",35, 2),
    SWALLOW("燕返し",35,0),
    ;

    fun getSkillName(): String{
        return skillName
    }

    fun getInvocationRate(): Int{
        return invocationRate
    }

    fun getDamageRate():Int{
        return damageRate
    }
}
