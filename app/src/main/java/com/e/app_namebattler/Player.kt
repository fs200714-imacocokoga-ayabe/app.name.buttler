package com.e.app_namebattler

import android.os.Build
import androidx.annotation.RequiresApi
import java.math.BigInteger
import java.security.MessageDigest
import java.util.concurrent.ThreadLocalRandom

open class Player {

    var name:String = ""

    constructor(name: String){

       this.name = name

        makeCharacter(name)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    open fun randomInt(rangeFirstNum: Int, rangeLastNum: Int) {
        ThreadLocalRandom.current().nextInt(rangeFirstNum, rangeLastNum)
    }

    // var name: String? = "" // 名前

     var job: String? = "" // 職業

     var hp: Int = 0 // HP

     var mp: Int = 0 // MP

     var str:Int = 0 // 攻撃力

     var def:Int = 0 // 防御力

     var luck: Int = 0 // 運

     var agi:Int = 0 // すばやさ

     var mark: Boolean? = false

    /**
     * コンストラクタ
     * @param name
     * : プレイヤー名
     * */
//    open fun Player(name: String?) {
//        this.name = name
//        if (name != null) {
//            makeCharacter(name)
//        } // キャラクターのパラメータ生成
//    }

    open fun Player() {}

    /**
     * 現在HPを取得する
     * @return hp :現在HP
     */
    open fun getHP(): Int {
        return hp
    }

    /**
     * 現在MPを取得する
     * @return mp :現在MP
     */
    open fun getMP(): Int {
        return mp
    }

    /**
     * 攻撃力を取得する
     * @return atk :攻撃力
     */
    open fun getSTR(): Int {
        return str
    }

    /**
     * 防御力を取得する
     * @return def :防御力
     */
    open fun getDEF(): Int {
        return def
    }

    /**
     * 運を取得する
     * @return luck :運
     */
    open fun getLUCK(): Int {
        return luck
    }

    /**
     * 素早さを取得する
     * @return agi :素早さ
     */
    open fun getAGI(): Int {
        return agi
    }

    /**
     * 敵味方の識別をセットする
     * @param mark
     */
    open fun setMark(mark: Boolean) {
        this.mark = mark
    }

    /**
     * @return mark :敵味方識別を取得する
     */
    open fun isMark(): Boolean? {
        return mark
    }

    open fun makeCharacter(name: String){

    }

    /**
     * 名前(name)からキャラクターに必要なパラメータを生成する
     */
    fun getNumber(index: Int, max: Int): Int {
        try {

            // 名前からハッシュ値を生成する
            val result = MessageDigest.getInstance("SHA-1").digest(name!!.toByteArray())
            val digest = String.format("%040x", BigInteger(1, result))

            // ハッシュ値から指定された位置の文字列を取り出す（２文字分）
            val hex = digest.substring(index * 2, index * 2 + 2)

            // 取り出した文字列（16進数）を数値に変換する
            val `val` = hex.toInt(16)
            return `val` * max / 255
        } catch (e: Exception) {
            // エラー
            e.printStackTrace()
        }
        return 0
    }



}

