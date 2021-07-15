package com.e.app_namebattler.view.party.job

enum class JobData(

    private val jobNumber: Int,
    private val jobName: String,
    private val jobHp: Int,
    private val jobHpCorrectionValue: Int,
    private val jobMp: Int,
    private val jobMpCorrectionValue: Int,
    private val jobStr: Int,
    private val jobStrCorrectionValue: Int,
    private val jobDef: Int,
    private val jobDefCorrectionValue: Int,
    private val jobLuck: Int,
    private val jobLuckCorrectionValue: Int,
    private val jobAgi: Int,
    private val jobAgiCorrectionValue: Int,
    private val jobMemo: String
) {

    FIGHTER(0, "戦士", 200, 100, 0, 0, 70, 30, 70, 30,
        99, 1, 49, 1, "HP、STR、DEFが高く\nダメージが2倍になる突撃の\nスキルを持っています\n魔法は使えません"),
    WIZARD(1, "魔法使い", 100, 50, 50, 30, 49, 1, 49, 1,
        99, 1, 40, 20, "MPが高く\n火と雷の攻撃魔法が使え\n炎の精霊を召喚して敵を攻撃できる\nスキルを持っています"),
    PRIEST(2, "僧侶", 120, 80, 30, 20, 40, 10, 60, 10,
        99, 1, 40, 20, "毒や麻痺の魔法が使え\n光の精霊を召喚して\n自身を回復できる\nスキルを持っています"),
    NINJA(3, "忍者", 100, 70, 20, 10, 50, 20, 49, 1,
        99, 1, 40, 40, "AGIが高く\n火遁の術が使え\n２回攻撃するつばめ返しの\nスキルを持っています");

    fun getJobNumber(): Int {
        return jobNumber
    }

    fun getJobName(): String {
        return jobName
    }

    fun getJobHp(): Int {
        return jobHp
    }

    fun getJobMinHp(): Int {
        return jobHpCorrectionValue
    }

    fun getJobMp(): Int {
        return jobMp
    }

    fun getJobMinMp(): Int {
        return jobMpCorrectionValue
    }

    fun getJobStr(): Int {
        return jobStr
    }

    fun getJobMinStr(): Int {
        return jobStrCorrectionValue
    }

    fun getJobDef(): Int {
        return jobDef
    }

    fun getJobMinDef(): Int {
        return jobDefCorrectionValue
    }

    fun getJobLuck(): Int {
        return jobLuck
    }

    fun getJobMinLuck(): Int {
        return jobLuckCorrectionValue
    }

    fun getJobAgi(): Int {
        return jobAgi
    }

    fun getJobMinAgi(): Int {
        return jobAgiCorrectionValue
    }

    fun getJobMemo(): String {
        return jobMemo
    }
}