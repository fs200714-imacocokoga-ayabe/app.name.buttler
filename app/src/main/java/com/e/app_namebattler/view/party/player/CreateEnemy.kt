package com.e.app_namebattler.view.party.player

import android.os.Build
import androidx.annotation.RequiresApi
import com.e.app_namebattler.view.party.job.JobFighter
import com.e.app_namebattler.view.party.job.JobNinja
import com.e.app_namebattler.view.party.job.JobPriest
import com.e.app_namebattler.view.party.job.JobWizard
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class CreateEnemy {

    private lateinit var enemy: Player
    var enemyPartyList = arrayListOf<CharacterAllData>()
    var enemyPartyList02 = arrayListOf<CharacterAllData>()
    private val enemyNameList = ArrayList<EnemyName>()
    private val jobList = ArrayList<EnemyJob>()
    private val enemyFighterImageList = ArrayList<EnemyFighterImageData>()
    private val enemyWizardImageList = ArrayList<EnemyWizardImageData>()
    private val enemyPriestImageList = ArrayList<EnemyPriestImageData>()
    private val enemyNinjaImageList = ArrayList<EnemyNinjaImageData>()
    private var enemyImage: Int = 0


    // 相手の名前をenemyListに格納する
   fun setName() {

        enemyNameList.clear()
        jobList.clear()

       for (e in EnemyName.values()) {
           enemyNameList.add(e)
       }

        for (j in EnemyJob.values()) {
            jobList.add(j)
        }
   }

    // 名前を選択する
    private fun selectName(): String {

        val nNumber = (1..enemyNameList.size).random()
        val a = enemyNameList[nNumber - 1].getEnemyName()
        enemyNameList.removeAt(nNumber - 1)

        return a
    }

    // 職業を選択する
    private fun selectJob(): String {

        val jNumber = (1..jobList.size).random()
        val b = jobList[jNumber - 1].getEnemyJob()

        return b
    }

    // 敵キャラクターのイメージ画像をランダムで取得
    private fun selectImage(enemyJob: String): Int {

        for (e in EnemyFighterImageData.values()) {
            enemyFighterImageList.add(e)
        }

        for (e in EnemyWizardImageData.values()) {
            enemyWizardImageList.add(e)
        }

        for (e in EnemyPriestImageData.values()) {
            enemyPriestImageList.add(e)
        }

        for (e in EnemyNinjaImageData.values()) {
            enemyNinjaImageList.add(e)
        }

        when(enemyJob){
            "戦士" -> enemyImage = enemyFighterImageList[(1..enemyFighterImageList.size).random() - 1].getCharacterImage()
            "魔法使い" -> enemyImage = enemyWizardImageList[(1..enemyWizardImageList.size).random() - 1].getCharacterImage()
            "僧侶" -> enemyImage = enemyPriestImageList[(1..enemyPriestImageList.size).random() - 1].getCharacterImage()
            "忍者" -> enemyImage = enemyNinjaImageList[(1..enemyNinjaImageList.size).random() - 1].getCharacterImage()
        }
        return  enemyImage
    }

    // 敵キャラクターを作成
    @RequiresApi(Build.VERSION_CODES.O)
    fun makeEnemy(): ArrayList<CharacterAllData> {

        for (i in 1 .. 3) {

            val enemyName = selectName()
            val enemyJob = selectJob()
            val enemyImage = selectImage(enemyJob)

            when (enemyJob) {

                "戦士" -> JobFighter(enemyName).let { enemy = it }
                "魔法使い" -> JobWizard(enemyName).let { enemy = it }
                "僧侶" -> JobPriest(enemyName).let { enemy = it }
                "忍者" -> JobNinja(enemyName).let { enemy = it }
            }

            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:m")
            val createAt = current.format(formatter).toString()

            enemyPartyList.add(
                CharacterAllData(
                    enemyName, enemyJob,
                    enemy.hp, enemy.mp, enemy.str, enemy.def,
                    enemy.agi, enemy.luck, createAt, enemyImage
                )
            )
        }

        return enemyPartyList
    }

    fun setEnemyParty(enemyPartyList: ArrayList<CharacterAllData>) {

        enemyPartyList02.addAll(enemyPartyList)
    }

    enum class EnemyName(private val enemyName: String) {

        E01("ドリアール"),E02("オロバリル"),E03("エルヴィラ"),E04("アーミュー"),E05("ウァサゴー"),
        E06("アトリエット"),E7("ジャスカー"),E8("ベリアモン"),E9("アイヴィス"),E10("トバイモン"),
        E11("エギビゴル"),E12("ヴィヴェラ"),E13("ジャイシー"),E14("アドラース"),E15("クラリーナ"),
        E16("ベネテリー"),E17("フィステマ"),E18("ダーリジット"),E19("ゲイブラッド"),E20("ダンタムズ"),
        E21("ヴェランコ"),E22("デーヴィッド"),E23("ウリクサス"),E24("ヴェネデット"),E25("ニコラリー"),
        E26("ベルファス"),E27("エラルタコ"), E28("ジョナンド"),E29("リバイモン"),E30("ターヴィオ"),
        E31("パトリック"),E32("ウェパズズ"),E33("アンニコラ"),E34("ルフレット"),E35("アグナック"),
        E36("ベネディオ"), E37("クスタント"),E38("セエレ"),E39("ティアーノ"), E40("ホレス"),
        E41("ダイアニー"),E42("サラディオ"),E43("フェビアン"),E44("シャローナ"),E45("ルフレート"),
        E46("アーローム"),E47("ドライーズ"),E48("ルメネーア"),E49("ヴァレッド"),E50("リンジャー"),
        E51("アンセスト"),E52("ルドウエン"),E53("カーラ"),E54("アポリスタ"),E55("エセルイス"),
        E56("リザベティ"),E57("ミントーレ"),E58("コーニエル"),E59("ケイ"),E60("アンセルモ"),
        E61("モイモレク"),E62("アントニア"),E63("バルダンテ"),E64("ルコシエル"),E65("ブリジェマ"),
        E66("アナスパレ"),E67("ルーレプト"),E68("キャエレン"),E69("ルクレンゾ"),E70("ラシアダド"),
        E71("ローレイン"),E72("ジルベルト"),E73("ベザル"),E74("ジョセアラ"),E75("ヴァレーモ"),
        E76("アメルカス"),E77("ディアリー"),E78("ファエーレ"),E79("アムニエン"),E80("コール");

        fun getEnemyName():String{

            return enemyName
        }
    }

    enum class EnemyJob(private val enemyJob: String) {

        J01("戦士"), J02("魔法使い"), J03("僧侶"), J04("忍者");

        fun getEnemyJob(): String {

            return enemyJob
        }
    }
}

