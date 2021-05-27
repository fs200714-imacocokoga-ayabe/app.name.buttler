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
    private val jobAgiCorrectionValue: Int) {

    FIGHTER(0,"戦士", 200, 100, 0, 0, 70, 30, 70, 30, 99, 1, 49, 1),
    WIZARD(1,"魔法使い", 100, 50, 50, 30, 49, 1, 49, 1,99, 1, 40, 20),
    PRIEST(2,"僧侶", 120, 80, 30, 20, 40, 10, 60, 10, 99, 1, 40, 20),
    NINJA(3,"忍者",100, 70, 20, 10, 50, 20, 49, 1, 99, 1, 40, 40);

    fun getJobNumber(): Int {
        return jobNumber
    }

    fun getJobName(): String {
        return jobName
    }

    fun getJobHp(): Int{
        return jobHp
    }

    fun getJobHpCorrectionValue(): Int{
        return  jobHpCorrectionValue
    }

    fun getJobMp():Int{
        return  jobMp
    }

    fun getJobMpCorrectionValue(): Int{
        return  jobMpCorrectionValue
    }

    fun getJobStr(): Int{
        return jobStr
    }

    fun getJobStrCorrectionValue(): Int{
        return jobStrCorrectionValue
    }

    fun getJobDef(): Int{
        return jobDef
    }

    fun getJobDefCorrectionValue(): Int{
        return jobDefCorrectionValue
    }

    fun getJobluck(): Int{
        return jobLuck
    }

    fun getJobluckCorrectionValue(): Int{
        return jobLuckCorrectionValue
    }

    fun getJobAgi(): Int{
        return jobAgi
    }

    fun getJobAgiCorrectionValue(): Int{
        return jobAgiCorrectionValue
    }
}