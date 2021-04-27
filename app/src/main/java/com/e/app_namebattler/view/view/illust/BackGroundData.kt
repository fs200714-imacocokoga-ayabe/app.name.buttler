package com.e.app_namebattler.view.view.illust

import com.e.app_namebattler.R

enum class BackGroundData(private val bg: Int) {

    BG01(R.drawable.back_ground01),BG02(R.drawable.back_ground02),BG03(R.drawable.back_ground03),BG04(R.drawable.back_ground04),
    BG05(R.drawable.back_ground05),BG06(R.drawable.back_ground06),BG07(R.drawable.back_ground07),BG08(R.drawable.back_ground08);

    fun getBackGround(): Int{
        return  bg
    }
}