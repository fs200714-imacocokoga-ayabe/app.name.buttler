package com.e.app_namebattler

import android.widget.TextView




//class BattleLog(callBattleLog: BattleLogText){

//    var callback: BattleLogText? = null
//
//        fun doSomething(s: String) {
//
//            val logText: String = s
//
//            callback?.updateBattleLog(s)
//        }
//
//        init {
//            callback = callBattleLog
//        }


//}

class BattleLog{
    fun update(tv: TextView) {
        tv.text = "Hello"
    }
}