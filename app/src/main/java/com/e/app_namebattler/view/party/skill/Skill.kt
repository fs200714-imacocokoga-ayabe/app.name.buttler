package com.e.app_namebattler.view.party.skill

enum class Skill (

    private val skillName: String,
    private val invocationRate: Int){

    ASSAULT("突撃", 30),
    SWALLOW("燕返し",30),
    ;

    fun getSkillName(): String{
        return skillName
    }

    fun getInvocationRate(): Int{
        return invocationRate
    }
}
