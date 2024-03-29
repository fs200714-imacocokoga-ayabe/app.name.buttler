package com.e.app_namebattler.view.party.player

import android.os.Build
import androidx.annotation.RequiresApi
import com.e.app_namebattler.controller.BattleRecordListener
import com.e.app_namebattler.view.party.herb.IEat
import com.e.app_namebattler.view.party.player.job.JobData
import com.e.app_namebattler.view.party.magic.IRecoveryMagic
import com.e.app_namebattler.view.party.magic.IUseMagic
import com.e.app_namebattler.view.party.magic.MagicData
import com.e.app_namebattler.view.party.skill.IUseSkill
import com.e.app_namebattler.view.party.status.Status
import com.e.app_namebattler.view.view.music.SoundData
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.collections.ArrayList

open class Player(private var name: String): IEat, BattleRecordListener() {
    constructor(
        name: String,
        job: String,
        hp: Int,
        mp: Int,
        str: Int,
        def: Int,
        agi: Int,
        luck: Int
    ):this(name){
makePlayer(name, job, hp, mp, str, def, agi, luck)
    }

    open fun makePlayer(
        name: String,
        job: String,
        hp: Int,
        mp: Int,
        str: Int,
        def: Int,
        agi: Int,
        luck: Int
    ) {

        this.name = name
        this.job = job
        this.hp = hp
        this.mp = mp
        this.str = str
        this.def = def
        this.agi = agi
        this.luck = luck
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    open fun randomInt(rangeFirstNum: Int, rangeLastNum: Int) {
        ThreadLocalRandom.current().nextInt(rangeFirstNum, rangeLastNum)
    }

    open var job: String? = "" // 職業
    open var hp: Int = 0 // HP
    open var mp: Int = 0 // MP
    open var str: Int = 0 // 攻撃力
    open var def: Int = 0 // 防御力
    open var agi: Int = 0 // すばやさ
    open var luck: Int = 0 // 運
    private var maxHp: Int = 0 // 最大HP
    private var maxMp: Int = 0 // 最大MP
    open var isPoison: Boolean = false
    open var isParalysis: Boolean = false
    open var isMark: Boolean = false
    private var characterImageType: Int = 0
    private var characterEffect = 0
    private var characterStatusEffect = 0
    private var attackSoundEffect = 0
    private var idNumber: Int = 0
    var damage = 0
    override var log = StringBuilder()
    var strongWord = false
    open lateinit var jobData: JobData

    open var record = ""

    var magics: List<IUseMagic>
    lateinit var magic: IUseMagic
    var skills: List<IUseSkill>
    lateinit var skill: IUseSkill

    init {
        magics = ArrayList()
        skills = ArrayList()
        initJob()
        makeCharacter(name)
    }

    open fun initJob() {}

    open fun getName(): String {
        return name
    }

    open fun getHP(): Int {
        return hp
    }

    open fun getMP(): Int {
        return mp
    }

    open fun getSTR(): Int {
        return str
    }

    open fun getDEF(): Int {
        return def
    }

    open fun getLUCK(): Int {
        return luck
    }

    open fun getAGI(): Int {
        return agi
    }

    open fun getMaxHp(): Int {
        return maxHp
    }

    open fun setMaxHp(hp: Int) {
        maxHp = hp
    }

    open fun getMaxMp(): Int {
        return maxMp
    }

    open fun setMaxMp(mp: Int) {
        maxMp = mp
    }

    fun setIdNumber(id: Int) {
        idNumber = id
    }

    open fun makeCharacter(name: String) {
        this.job = jobData.getJobName()
        this.hp = getNumber(jobData.getJobHp()) + jobData.getJobMinHp()
        this.mp = getNumber(jobData.getJobMp()) + jobData.getJobMinMp()
        this.str = getNumber(jobData.getJobStr()) + jobData.getJobMinStr()
        this.def = getNumber(jobData.getJobDef()) + jobData.getJobMinDef()
        this.agi = getNumber(jobData.getJobAgi()) + jobData.getJobMinAgi()
        this.luck = getNumber(jobData.getJobLuck()) + jobData.getJobMinLuck()
    }

    open fun getPoison(): String {

        return if (this.isPoison) {
            Status.POISON.getAbnormalStatusName()
        } else {
            Status.FINE.getAbnormalStatusName()
        }
    }

    open fun getParalysis(): String {

        return if (this.isParalysis) {
            Status.PARALYSIS.getAbnormalStatusName()
        } else {
            Status.FINE.getAbnormalStatusName()
        }
    }

    open fun getCharacterImageType(): Int {
        return characterImageType
    }

    open fun setCharacterImageType(characterImage: Int) {
        characterImageType = characterImage
    }

    open fun getPrintStatusEffect(): Int {
        return characterEffect
    }

    open fun setPrintStatusEffect(effect: Int) {
        characterEffect = effect
    }

    open fun getStatusSoundEffect(): Int {
        return characterStatusEffect
    }

    open fun setStatusSoundEffect(statusEffect: Int) {
        characterStatusEffect = statusEffect
    }

    open fun getAttackSoundEffect(): Int {
        return attackSoundEffect
    }

    open fun setAttackSoundEffect(soundEffect: Int) {
        attackSoundEffect = soundEffect
    }

    val isLive: Boolean
        get() = 0 < hp

    /**
     * 名前(name)からキャラクターに必要なパラメータを生成する
     */
    private fun getNumber(max: Int): Int {

        if(name.endsWith("@aya")){
            name = name.removeSuffix("@aya")// @ayaを抜いたハッシュ値を生成する名前からため削除する
            strongWord = true
        }

        val index = (0..19).random()

        try {
            // 名前からハッシュ値を生成する
            val result = MessageDigest.getInstance("SHA-1").digest(name.toByteArray())
            val digest = String.format("%040x", BigInteger(1, result))

            if (strongWord){
                var valBig = 0
                for (i in 1..digest.length / 2){
                    val hex = digest.substring((i - 1) * 2, (i - 1) * 2 + 2)
                    val `val` = hex.toInt(16)

                    if (valBig <= `val` ){
                        valBig = `val`
                    }
                }
                return valBig * max / 255

            }else {
                // ハッシュ値から指定された位置の文字列を取り出す（２文字分）
                val hex = digest.substring(index * 2, index * 2 + 2)
                // 取り出した文字列（16進数）を数値に変換する
                val `val` = hex.toInt(16)

                return `val` * max / 255
            }

        } catch (e: Exception) {
            // エラー
            e.printStackTrace()
        }
        return 0
    }


    // ジョブごとにオーバーライドして処理を記述してください
    open fun normalAttack(defender: Player): StringBuilder {
        return log
    }

    open fun skillAttack(defender: Player): StringBuilder {
        return log
    }

    open fun magicAttack(defender: Player): StringBuilder {
        return log
    }

    open fun healingMagic(defender: Player): StringBuilder {
        return log
    }

    open fun eat(): StringBuilder {
        selectEat(this)
        return log
    }

    /**
     * 対象プレイヤー(target)に対して与えるダメージを計算する
     * @param target: 対象プレイヤー
     * @return ダメージ値(0～)
     */
    open fun calcDamage(target: Player): Int {
        val power = (1..this.str).random()
        val luk = (1..100).random()
        if (luk <= this.luck) { // 乱数の値がlukの値の中なら
            log.append("会心の一撃!\n")
            damage = this.str
        } else {
            damage = power - target.def
            if (damage < 0) {
            //    log.append("${this.name}の攻撃はミス！\n")
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

        if (0 < damage) {
            log.append("${defender.name}に${damage}のダメージ！\n")
            defender.setPrintStatusEffect(1)
        } else {
            log.append("${defender.name}は攻撃をかわした！\n")
            defender.setPrintStatusEffect(3)
            this.setAttackSoundEffect(17)
        }
        defender.damage(damage) // 求めたダメージを対象プレイヤーに与える
    }

    /**
     * defenderが倒れたかの判定
     * @param defender 相手
     */
    open fun knockedDownCheck(defender: Player) {
        if (defender.hp <= 0) {
            log.append("${defender.name}は力尽きた...\n")
        } else {
            conditionCheck() // 状態異常チェック

            if (this.hp <= 0) {
                log.append("${this.name}は力尽きた...\n")
            }
        }
    }

    /**
     * 状態異常のチェック
     */
    private fun conditionCheck() {

        if (isParalysis) { // true:麻痺状態 false:麻痺していない
            if (MagicData.PARALYSIS.getContinuousRate() <= (1..100).random()) { // 麻痺の確立より乱数が上なら麻痺の解除
                isParalysis = false // 麻痺解除
                log.append("${this.name}は麻痺が解けた！\n")
            }
        }

        if (isPoison) { // true:毒状態 false:無毒状態
            damage(MagicData.POISON.getMaxDamage()) // 毒のダメージ計算
            log.append("${this.name}は毒のダメージを${MagicData.POISON.getMaxDamage()}受けた！\n")
            setStatusSoundEffect(12)
        }
    }

    /**
     * ダメージを受ける
     * @param damage: ダメージ値
     */
    open fun damage(damage: Int) {
        // ダメージ値分、HPを減少させる
        hp = (this.hp - damage).coerceAtLeast(0)
    }
    
    open fun recoveryProcess(defender: Player, heal: Int): Int {

        var healValue = heal

        healValue = defender.maxHp.coerceAtMost(defender.hp + healValue)
        healValue -= defender.hp
        log.append("${defender.name}はHPが${healValue}回復した！\n")
        defender.recovery(healValue)
        defender.setPrintStatusEffect(2)
        setAttackSoundEffect(SoundData.S_HEAL01.getSoundNumber())

        return healValue
    }

     private fun recovery(healValue: Int) {
        hp += healValue
    }

    open fun downMp(mpCost: Int) {
        mp -= mpCost // MPを消費
    }

    open fun choiceMagic(): IUseMagic {
        Collections.shuffle(magics)

        for (magic in magics) {

            if (magic !is IRecoveryMagic) {
                return magic
            }
        }
        return magic
    }
}

