package com.e.app_namebattler.view.strategy.enemystrategy

import com.e.app_namebattler.view.party.job.JobData
import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.strategy.BaseStrategy

open class StrategyEnemyAttackPatternByJob : BaseStrategy() {

    var enemyContext: EnemyContext? = null
    var cpuPlayer: Player? = null
    var cpuPlayer2: Player? = null
    var userPlayer: Player? = null
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

        when (player1.job) {

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
        return enemyContext?.attackStrategy(cpuPlayer, userParty, cpuParty)!! // バトルログを返す
    }
}
