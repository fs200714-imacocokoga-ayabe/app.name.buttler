package com.e.app_namebattler

class CharacterAllData(
    val name: String,
    val job: String,
    val hp: Int,
    val mp: Int,
    val str: Int,
    val def: Int,
    val agi: Int,
    val luck: Int,
    val create_at: String
)

class CharacterBattleStatusData(
    val name: String,
    val job: String,
    val hp: Int,
    val maxHp: Int,
    val mp: Int,
    val maxMp: Int,
    val str: Int,
    val def: Int,
    val agi: Int,
    val luck: Int,
    status: String
)
