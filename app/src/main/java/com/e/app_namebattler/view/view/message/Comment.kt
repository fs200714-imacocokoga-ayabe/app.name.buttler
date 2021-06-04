package com.e.app_namebattler.view.view.message

enum class Comment (
    private val comment: String) {

        M_CHANGE_MESSAGE_SPEED("タップで次のターンの速度を変更できます"),
        M_NEXT_TURN_FAST("次のターンのメッセージ速度：早い"),
        M_NEXT_TURN_SLOW("次のターンのメッセージ速度：遅い"),
        M_NOT_END_TURN("ターンが終了していません"),
        M_BATTLE_START_COMMENT("画面をタップするとスタートします\n\n速度変更もタップで出来ます");

        fun getComment(): String{
            return comment
        }
    }