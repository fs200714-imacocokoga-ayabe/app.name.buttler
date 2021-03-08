package com.e.app_namebattler.view.view.music

import com.e.app_namebattler.R

enum class MusicData(private val bgm: Int) {

    BGM01(R.raw.lastwar),BGM02(R.raw.neighofwar),BGM03(R.raw.newworld),BGM04(R.raw.yokoku);

    fun getBgm(): Int{
        return  bgm
    }
}