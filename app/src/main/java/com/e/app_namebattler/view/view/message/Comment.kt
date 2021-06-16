package com.e.app_namebattler.view.view.message

enum class Comment(
    private val comment: String
) {

    M_NEXT_TURN_FAST_COMMENT("次のターンのメッセージ速度：早い"),
    M_NEXT_TURN_SLOW_COMMENT("次のターンのメッセージ速度：遅い"),
    M_SPEED_CHANGE_COMMENT("バトルスタートの後\n\nメッセージエリア内をタップすると\n\n速度変更が出来ます"),
    M_SAME_NAME_COMMENT("同じ名前がすでに存在しています"),
    M_NAME_NUMBER_COMMENT("名前は10文字までしか入力できません"),
    M_SELECT_MEMBER_COMMENT("3人選択してください"),
    M_MAX_REGISTER_CHARACTER_COMMENT("登録できるキャラクター数が最大です"),
    M_PARTY_MEMBER_NUMBER_COMMENT("パーティメンバーは３人です"),
    M_BATTLE_START_COMMENT("バトルスタート"),
    M_BATTLE_NEXT_TURN_COMMENT("次のターン"),
    M_IN_BATTLE_COMMENT("戦闘中・・・");

    fun getComment(): String {
        return comment
    }
}