package com.e.app_namebattler.view.party.status

enum class Status(private val abnormalStatusName: String) {

    FINE(""),
    POISON("毒"),
    PARALYSIS("麻痺")
    ;

    fun getAbnormalStatusName(): String {
        return abnormalStatusName
    }

}