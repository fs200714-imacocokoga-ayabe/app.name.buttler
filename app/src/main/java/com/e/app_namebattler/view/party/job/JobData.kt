package com.e.app_namebattler.view.party.job

enum class JobData(

    private val jobNumber: Int,
    private val jobName: String) {

    FIGHTER(0,"戦士"),
    WIZARD(1,"魔法使い"),
    PRIEST(2,"僧侶"),
    NINJA(3,"忍者");

    fun getJobNumber(): Int {
        return jobNumber
    }

    fun getJobName(): String {
        return jobName
    }


}