package com.e.app_namebattler.view.party.magic

import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.view.music.SoundData

open class BaseUseMagic: IUseMagic {

     var damage = 0
     var log = StringBuilder()
      lateinit var magicData: MagicData

    init {
        initMagic()
    }

    override fun initMagic() {}

    override fun effect(attacker: Player, defender: Player): StringBuilder {
        return log
    }

    override fun hasEnoughMp(mp: Int): Boolean {
        return magicData.getMpCost() <= mp
    }

    override fun damageProcess(attacker: Player, defender: Player, damage: Int) {

        log.append("${defender.getName()}に${damage}のダメージ！\n")

        if (0 < damage) {
            defender.setPrintStatusEffect(1)
        } else {
            defender.setPrintStatusEffect(3)
            attacker.setAttackSoundEffect(0)
        }
        defender.damage(damage) // 求めたダメージを対象プレイヤーに与える
    }

    override fun recoveryProcess(attacker: Player, defender: Player, heal: Int) {

        var healValue = heal

        healValue = defender.getMaxHp().coerceAtMost(defender.hp + healValue)
        healValue -= defender.hp
        log.append("${defender.getName()}はHPが${healValue}回復した！\n")
        defender.hp += healValue
        defender.setPrintStatusEffect(2)
        attacker.setAttackSoundEffect(SoundData.S_HEAL01.getSoundNumber())
    }
}


