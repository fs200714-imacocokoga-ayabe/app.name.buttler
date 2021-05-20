package com.e.app_namebattler.view.party.magic

enum class MagicData (

    private val magicName: String, // 魔法の名前
    private val mpCost: Int, // 消費MP
    private val minDamage: Int, // 最小ダメージ
    private val maxDamage: Int, // 最大ダメージ
    private val recoveryValue: Int,// 回復量
    private val continuousRate: Int, // 継続率
    private val invocationRate: Int){// 発動率

        FIRE("ファイア", 10, 10, 30, 0, 0,0),
        THUNDER("サンダー", 20, 20, 50, 0, 0,0),
        HEAL("ヒール", 20, 0, 0, 50, 0,0),
        PARALYSIS("パライズ", 10, 0, 0, 0, 20,0),
        POISON("ポイズン", 10, 0, 20, 0, 0,0),
        FIRE_ROLL("火遁の術", 10, 10, 30, 0, 0,0),
        FIRE_ELEMENTAL("炎の精霊", 0, 60, 60, 0, 0,40),
        OPTICAL_ELEMENTAL("光の精霊", 0, 0, 0, 70, 0,40);

        /**
         * 魔法の名前を取得する
         * @return name :魔法の名前
         */
        fun getName(): String {
            return magicName
        }

        /**
         * 消費MPを取得する
         * @return mpCost :消費MP
         */
        fun getMpCost(): Int {
            return mpCost
        }

        /**
         * 最小ダメージを取得する
         * @return minDamage :最小ダメージ
         */
        fun getMinDamage(): Int {
            return minDamage
        }

        /**
         * 最大ダメージを取得する
         * @return maxDamage :最大ダメージ
         */
        fun getMaxDamage(): Int {
            return maxDamage
        }

        /**
         * 回復量を取得する
         * @return recoveryValue :回復量
         */
        fun getRecoveryValue(): Int {
            return recoveryValue
        }

        /**
         * 継続率を取得する
         * @return continuousRate :継続率
         */
        fun getContinuousRate(): Int {
            return continuousRate
        }

        /**
         * 発動率を取得する
         * @return invocationRate
         */
        fun getInvocationRate():Int{
            return invocationRate
        }
}