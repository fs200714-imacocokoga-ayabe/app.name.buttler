package com.e.app_namebattler

import android.os.Build
import androidx.annotation.RequiresApi


class CreateEnemy {

    lateinit var helper: MyOpenHelper

    private lateinit var enemy: Player
    var enemyPartyList = arrayListOf<CharacterAllData>()
    private val enemyNameList = ArrayList<EnemyName>()
    private val jobList = ArrayList<EnemyJob>()
    val enemyList = ArrayList<Player>()

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

        enemyNameList.removeAt(nNumber)

        return a
    }

    // 職業を選択する
    private fun selectJob(): String {

        val jNumber = (1..jobList.size).random()

        val b = jobList[jNumber - 1].getEnemyJob()

        return b
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun makeEnemy(): ArrayList<CharacterAllData> {

        for (i in 1 .. 3) {

            val enemyName = selectName()

            val enemyJob = selectJob()

            when (enemyJob) {

                "戦士" -> Fighter(enemyName).let { enemy = it }
                "魔法使い" -> Wizard(enemyName).let { enemy = it }
                "僧侶" -> Priest(enemyName).let { enemy = it }
                "忍者" -> Ninja(enemyName).let { enemy = it }
            }

            enemyPartyList.add(
                CharacterAllData(
                    enemyName, selectJob(),
                    enemy.hp, enemy.mp,enemy.str,enemy.def,
                    enemy.agi,enemy.luck,""
                )
            )



        }

        return enemyPartyList

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

