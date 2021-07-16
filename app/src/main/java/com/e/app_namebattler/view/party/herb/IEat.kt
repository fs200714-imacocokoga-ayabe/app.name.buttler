package com.e.app_namebattler.view.party.herb

import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.view.music.SoundData

interface IEat {

    val log: StringBuilder

    fun selectEat(attacker: Player): java.lang.StringBuilder {

        log.clear()

        if (attacker.isParalysis) {// 麻痺している場合
            log.append("${attacker.getName()}は麻痺で動けない！！\n")
            attacker.setSoundStatusEffect(1)

        } else {// 麻痺していない場合
            log.append("${attacker.getName()}は革袋の中にあった草を食べた！\n")

            when ((0..2).random()) {
                0 -> eatPoisonousGrass(attacker)
                1 -> eatHerb(attacker)
                2 -> eatGrass(attacker)
            }
        }
        attacker.knockedDownCheck(attacker)

        return log
    }

    fun eatPoisonousGrass(attacker: Player) {
        if (attacker.isPoison) { // 毒状態の場合
            log.append("毒消し草だった！！\n${attacker.getName()}は毒が消えた！\n")
            attacker.isPoison = false
            attacker.setAttackSoundEffect(SoundData.S_RECOVERY01.getSoundNumber())
        }else{
            log.append("毒消し草だった！！\n${attacker.getName()}はなにも起こらなかった！\n")
        }
    }

    fun eatHerb(attacker: Player) {
        log.append("薬草だった！\n")
        attacker.recoveryProcess(attacker, Herb.HERB.getHerbRecoveryValue())
        attacker.setAttackSoundEffect(SoundData.S_RECOVERY01.getSoundNumber())
    }

    fun eatGrass(attacker: Player) {
        log.append("ただの草だった！\n${attacker.getName()}は何も起こらなかった！\n")
    }
}
