package com.e.app_namebattler.view.view.music

import com.e.app_namebattler.R

enum class SoundData(private val number: Int, private val sound: Int) {

    S_SWORD01(1,R.raw.s_sword01),S_KATANA01(2, R.raw.s_katana01),S_PUNCH01(3, R.raw.s_punch01),S_SYURIKEN01(4, R.raw.s_syuriken01),
    S_FIRE01(5, R.raw.s_fire01),S_THUNDER01(6, R.raw.s_thunder01),S_POISON01(7, R.raw.s_poison01),S_PARALYSIS01(8, R.raw.s_paralysis),
    S_HEAL01(9, R.raw.s_heal01),S_RECOVERY01(10, R.raw.s_recovery01),S_KATANA02(11, R.raw.s_katana02),S_POISON_DAMAGE(12, R.raw.s_poison_damage),
    S_SLIDE01(13, R.raw.s_slide01),S_SWORD02(14, R.raw.s_sword02),S_SWORD01_AIR_SHOT(15, R.raw.s_sword01_air_shot),S_SWORD02_AIR_SHOT(16, R.raw.s_sword02_air_shot);

    fun getSoundNumber(): Int{
        return number
    }

    fun getSound(): Int{
        return  sound
    }
}