package com.e.app_namebattler

import android.os.Handler
import android.os.Message

class MyHandler: Handler() {

    var myCallBack: BattleMainActivity? = null

    companion object{

        const val MSG = 1
    }

    override fun handleMessage(msg: Message) {

        super.handleMessage(msg)
    }



}