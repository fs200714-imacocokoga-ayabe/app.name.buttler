package com.e.app_namebattler.view.strategy.enemystrategy

import com.e.app_namebattler.view.party.player.job.JobData
import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.strategy.BaseStrategy

open class StrategyEnemyAttackPatternByJob : BaseStrategy() {

    var enemyContext: EnemyContext? = null
    lateinit var cpuPlayer: Player
    lateinit var cpuPlayer2: Player
    lateinit var userPlayer: Player
    var userParty: MutableList<Player> = ArrayList()
    var cpuParty: MutableList<Player> = ArrayList()

    override fun attackStrategy(
        player1: Player,
        party1: List<Player>,
        party2: List<Player>
    ): StringBuilder {

        cpuPlayer = player1
        userParty.addAll(party1) // party1を入れる
        cpuParty.addAll(party2) // party2を入れる

        when (cpuPlayer.job) {

            JobData.FIGHTER.getJobName() -> {
                enemyContext = EnemyContext(EnemyFighterAttackPattern())
            }

            JobData.WIZARD.getJobName() -> {
                enemyContext = EnemyContext(EnemyWizardAttackPattern())
            }

            JobData.PRIEST.getJobName() -> {
                enemyContext = EnemyContext(EnemyPriestAttackPattern())
            }

            JobData.NINJA.getJobName() -> {
                enemyContext = EnemyContext(EnemyNinjaAttackPattern())
            }
        }
        return enemyContext!!.attackStrategy(cpuPlayer, userParty, cpuParty) // バトルログを返す
    }

    /**
     * HPの低い相手を選んで返す
     * @return userPlayer : HPの低い相手
     */
    open fun selectLowerHp(): Player {

        userParty.sortBy { it.hp }//sortByDescending: 降順でソート、 sortBy: 昇順でソート
        userPlayer = userParty[0]
        userParty.clear()

        return userPlayer
    }

    /**
     * HPの高い相手を選んで返す
     * @return userPlayer : HPの高い相手
     */
    open fun selectHighHp(): Player {

        userParty.sortByDescending { it.hp }//sortByDescending: 降順でソート、 sortBy: 昇順でソート
        userPlayer = userParty[0]
        userParty.clear()

        return userPlayer
    }

    /**
     * HPの低い味方を返す
     * @return cpuPlayer2:HPの低い味方
     */
    open fun lifeImportance(): Player {

        cpuPlayer2 = cpuPlayer

        for (i in cpuParty.indices) {
            if (cpuParty[i].getMaxHp() - cpuParty[i].hp < cpuPlayer.getMaxHp() - cpuPlayer.hp) {
                cpuPlayer2 = cpuParty[i]
            }
        }
        cpuParty.clear()

        return cpuPlayer2
    }
}
