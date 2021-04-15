package com.e.app_namebattler.view.party.skill

enum class Skill (

    private val skillName: String,
    private val invocationRate: Int){

    ASSAULT("捨て身の突撃", 35),
    SWALLOW("燕返し",35),
    ;

    fun getSkillName(): String{
        return skillName
    }

    fun getInvocationRate(): Int{
        return invocationRate
    }
}
