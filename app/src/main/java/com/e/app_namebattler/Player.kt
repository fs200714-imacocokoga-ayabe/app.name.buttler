package com.e.app_namebattler

import android.os.Build
import androidx.annotation.RequiresApi
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import java.util.concurrent.ThreadLocalRandom


open class Player{

    var random = Random()

    open var bsb = StringBuilder()

    private var name:String = ""

    constructor(name: String){

        this.name = name

        makeCharacter(name)

    }

    constructor(name: String, job: String, hp: Int, mp: Int, str: Int, def: Int, agi: Int, luck: Int){
//      this.name = name
//      this.job = job
//      this.hp = hp
//      this.mp = mp
//      this.str = str
//      this.def = def
//      this.agi = agi
//      this.luck = luck
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
    open var isPoison: Boolean = false
    open var isParalysis: Boolean = false
    open var isMark: Boolean = false
    private var characterAppearance:Int = 0
    private var idNumber: Int = 0
    var damage = 0

    private val herbRecoveryValue = 30

    /**
     * コンストラクタ
     * @param name
     * : プレイヤー名
     * */
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

    fun getIdNumber(): Int {
        return idNumber
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

    fun setIdNumber(id: Int) {
        idNumber = id
    }

    open fun makeCharacter(name: String){
    }

    open fun getPoison(): String{

        return if(this.isPoison){
            "毒"
        }else{
            ""
        }
    }

    open fun getParalysis(): String{

        return if(this.isParalysis){
            "麻痺"
        }else{
            ""
        }
    }

    open fun getCharacterAppearance(): Int {

        return characterAppearance
    }

    open fun setCharacterAppearance(appearance: Int){

        characterAppearance = appearance
    }

    val isLive: Boolean
        get() = hp > 0

    /**
     * 名前(name)からキャラクターに必要なパラメータを生成する
     */
    fun getNumber(index: Int, max: Int): Int {
        try {

            // 名前からハッシュ値を生成する
            val result = MessageDigest.getInstance("SHA-1").digest(name.toByteArray())
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
        open fun attack(defender: Player, strategyNumber: Int): StringBuilder {

        bsb.clear()

            if (!isParalysis) { // 麻痺していない
                when (strategyNumber) {
                    1 -> normalAttack(defender)
                    2 -> if (this is IMagicalUsable) {
                            magicAttack(defender)
                         } else {
                            normalAttack(defender)
                         }
                    3 -> skillAttack(defender)
                    4 -> if (this is IRecoveryMagic) {
                             healingMagic(defender)
                         } else {
                             normalAttack(defender)
                         }
                    5 -> eatGrass()
                }
            } else {
                bsb.append("${getName()}は麻痺で動けない！！\n")
            }

            fall(defender) // 倒れた判定

        return bsb
    }

    open fun normalAttack(defender: Player) {}
    open fun skillAttack(defender: Player) {}
    open fun magicAttack(defender: Player) {}
    open fun healingMagic(defender: Player) {}

    /**
     * 対象プレイヤー(target)に対して与えるダメージを計算する
     * @param target: 対象プレイヤー
     * @return ダメージ値(0～)
     */
    open fun calcDamage(target: Player): Int {
        val power = (1 ..getSTR()).random()
        val luk = (1..100).random()
        if (luk <= getLUCK()) { // 乱数の値がlukの値の中なら
            bsb.append("会心の一撃!\n")
            damage = getSTR()
        } else {
            damage = power - target.getDEF()
            if (damage < 0) {
                bsb.append("${getName()}の攻撃はミス！\n")
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
        bsb.append("${defender.getName()}に${damage}のダメージ！\n")
        defender.damage(damage) // 求めたダメージを対象プレイヤーに与える
    }

    /**
     * defenderが倒れたかの判定
     * @param defender 相手
     */
    open fun fall(defender: Player) {
        if (defender.getHP() <= 0) {
            bsb.append("${defender.getName()}は力尽きた...\n")
            abnormalState() // 状態異常チェック
        } else {
            abnormalState() // 状態異常チェック
            if (getHP() <= 0) { // playerの倒れた判定
                bsb.append("${getName()}は力尽きた...\n")
            }
        }
    }

    /**
     * 状態異常のチェック
     */
    private fun abnormalState() {

        if (isPoison) { // true:毒状態 false:無毒状態
            damage(Magic.POISON.getMaxDamage()) // 毒のダメージ計算
            bsb.append("${getName()}は毒のダメージを${Magic.POISON.getMaxDamage()}受けた！\n")
        }
        if (isParalysis) { // true:麻痺状態 false:麻痺していない
            if ((1..100).random() > Magic.PARALYSIS.getContinuousRate()) { // 麻痺の確立より乱数が上なら麻痺の解除
                isParalysis = false // 麻痺解除
                bsb.append("${getName()}は麻痺が解けた！\n")
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

        bsb.append("${getName()}は革袋の中にあった薬草を食べた！\n")
        when ((0..2).random()) {
            0 -> {
                recoveryProcess(this, herbRecoveryValue)
            }

            1 -> {

                if (isPoison) {
                    bsb.append("${getName()}は毒が消えた！\n")
                    isPoison = false
                }else{

                    recoveryProcess(this, herbRecoveryValue)
                }
            }

            2 -> {
                bsb.append("${getName()}は何も起こらなかった！\n")
            }
        }
    }

   open fun recoveryProcess(defender: Player, healValue: Int): Int {
        var healValue = healValue
        healValue = defender.getMaxHp().coerceAtMost(defender.getHP() + healValue)
       healValue -= defender.getHP()
        bsb.append("${defender.getName()}はHPが${healValue}回復した！\n")
        defender.recovery(healValue)
        return healValue
    }

    private fun recovery(healValue: Int) {
        hp += healValue
    }
}

