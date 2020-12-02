package com.e.app_namebattler

import android.os.Build
import androidx.annotation.RequiresApi
import java.math.BigInteger
import java.security.MessageDigest
import java.util.concurrent.ThreadLocalRandom

open class Player {

    private var name:String = ""

    constructor(name: String){

       this.name = name

        makeCharacter(name)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    open fun randomInt(rangeFirstNum: Int, rangeLastNum: Int) {
        ThreadLocalRandom.current().nextInt(rangeFirstNum, rangeLastNum)
    }

     open var job: String? = "" // 職業

     open var hp: Int = 0 // HP

     open var mp: Int = 0 // MP

     open var str:Int = 0 // 攻撃力

     open var def:Int = 0 // 防御力

    open var agi:Int = 0 // すばやさ

     open var luck: Int = 0 // 運

    private var maxHp: Int = 0 // 最大HP

    private var maxMp: Int = 0 // 最大MP

     var isPoison: Boolean = false

     var isParalysis: Boolean = false

    private var mark: Boolean? = false

    var damage = 0

    private val herbRecoveryValue = 30

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

    open fun getName(): String{
        return name
    }

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

    open fun getMaxHp(): Int{
        return maxHp
    }

    open fun setMaxHp(hp: Int){

        maxHp = hp
    }

    open fun getMaxMp(): Int{
        return  maxMp
    }

    open fun setMaxMp(mp: Int){

        maxMp = mp
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

    /**
     * 対象プレイヤーに攻撃を行う
     * @param defender: 対象プレイヤー
     */
    open fun attack(defender: Player?){

        // ジョブごとにオーバーライドして処理を記述してください
    }

    /**
     * 対象プレイヤー(target)に対して与えるダメージを計算する
     *
     * @param target: 対象プレイヤー
     * @return ダメージ値(0～)
     */
    open fun calcDamage(target: Player): Int {
        val power = (1 ..getSTR()).random()
        val luk = (1..100).random()
        if (luk <= getLUCK()) { // 乱数の値がlukの値の中なら
            println("会心の一撃!")
            damage = getSTR()
        } else {
            damage = power - target.getDEF()
            if (damage < 0) {
                System.out.printf("%sの攻撃はミス！\n", getName())
                damage = 0
            }
        }
        return damage
    }

    /**
     * type,defender,damageから属性処理を加えて求めたダメージを対象プレイヤーに与える
     * @param defender :対象プレイヤー
     * @param damage :ダメージ
     */
    open fun damageProcess(defender: Player, damage: Int) {
       // var damage = damage
        //damage = attributeDecision(type, defender, damage) // 属性処理
        System.out.printf("%sに%dのダメージ！\n", defender.getName(), damage)
        defender.damage(damage) // 求めたダメージを対象プレイヤーに与える
    }

    /**
     * defenderが倒れたかの判定
     * @param defender 相手
     */
    open fun fall(defender: Player) {
        if (defender.getHP() <= 0) {
            System.out.printf("%sは力尽きた...\n", defender.getName())
            abnormalState() // 状態異常チェック
        } else {
            abnormalState() // 状態異常チェック
            if (getHP() <= 0) { // playerの倒れた判定
                System.out.printf("%sは力尽きた...\n", getName())
            }
        }
    }

    /**
     * 状態異常のチェック
     */
    private fun abnormalState() {

        if (isPoison) { // true:毒状態 false:無毒状態
            damage(Magic.POISON.getMaxDamage()) // 毒のダメージ計算
            System.out.printf("%sは毒のダメージを%d受けた！\n", getName(),
                Magic.POISON.getMaxDamage())
        }
        if (isParalysis) { // true:麻痺状態 false:麻痺していない
            if ((1..100).random() > Magic.PARALYSIS.getContinuousRate()) { // 麻痺の確立より乱数が上なら麻痺の解除
                isParalysis = false // 麻痺解除
                System.out.printf("%sは麻痺が解けた！\n", getName())
            }
        }
    }

    /**
     * ダメージを受ける
     * @param damage: ダメージ値
     */
    open fun damage(damage: Int) {
        // ダメージ値分、HPを減少させる
        hp = (getHP() - damage).coerceAtLeast(0)

    }

    /**
     * 草を食べる処理
     */
    open fun eatGrass() {

        System.out.printf("%sは革袋の中にあった草を食べた！\n", getName())
        when ((1..3).random()) {
            0 -> { System.out.printf("%sは体力が%d回復した！\n", getName(), herbRecoveryValue)
                    recovery(this, herbRecoveryValue)}

            1 -> { System.out.printf("%sは毒が消えた！\n", getName())
                 isPoison = false }

            2 -> {System.out.printf("%sは何も起こらなかった！\n", getName())}
        }
    }

    /**
     * player,healから対象プレイヤーを回復させ回復に使用した値を返す
     * @param player :対象プレイヤー
     * @param heal :回復値
     * @return 回復に使用した値
     */
    open fun recovery(player: Player, heal: Int): Int {
        var heal = heal
     //   heal = player.getMaxHP().coerceAtMost(player.getHP() + heal) 保留
        heal -= player.getHP()
        player.hp = player.getHP() + heal
        return heal
    }




}

