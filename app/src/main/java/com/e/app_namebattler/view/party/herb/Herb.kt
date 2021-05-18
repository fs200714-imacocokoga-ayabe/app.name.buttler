package com.e.app_namebattler.view.party.herb

enum class Herb(private val herbRecoveryValue: Int) {

    HERB(30)
    ;

    fun getHerbRecoveryValue(): Int{
        return herbRecoveryValue
    }

}