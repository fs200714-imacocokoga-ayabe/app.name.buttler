package com.e.app_namebattler.view.party.job

enum class JobData(

    private val jobName: String,
    private val jobNumber: Int) {

    FIGHTER("戦士", 0),
    WIZARD("魔法使い", 1),
    PRIEST("僧侶", 2),
    NINJA("忍者", 3);

    fun getJobName(): String {
        return jobName
    }

    fun getJobNumber(): Int {
        return jobNumber
    }
}