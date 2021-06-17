package com.e.app_namebattler.view.view.illust

import com.e.app_namebattler.R

enum class BackGroundData(
    private val backGround: Int,
    private val textColor: String) {

    BG01(R.drawable.back_ground01, "#FFFFFF"), BG02(R.drawable.back_ground02, "#FFFFFF"),
    BG04(R.drawable.back_ground04, "#FFFFFF"),
    BG05(R.drawable.back_ground05, "#FFFFFF"), BG06(R.drawable.back_ground06, "#FFFFFF"),
    BG08(R.drawable.back_ground08, "#FFFACD");

    fun getBackGround(): Int {
        return backGround
    }

    fun getTextColor(): String{
        return textColor
    }


}