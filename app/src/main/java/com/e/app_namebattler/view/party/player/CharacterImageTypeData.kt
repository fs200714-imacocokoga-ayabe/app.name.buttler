package com.e.app_namebattler.view.party.player

import com.e.app_namebattler.R

enum class AllyFighterImageData(private val characterImage: Int){

    AF01(R.drawable.fighter_ally01),AF02(R.drawable.fighter_ally02),AF03(R.drawable.fighter_ally03),AF04(R.drawable.fighter_ally04);

    fun getCharacterImage(): Int{
        return characterImage
    }
}

enum class AllyWizardImageData(private val characterImage: Int){

      AW01(R.drawable.wizard_ally01),AW02(R.drawable.wizard_ally02),AW03(R.drawable.wizard_ally03),AW04(R.drawable.wizard_ally04);

    fun getCharacterImage(): Int{
        return characterImage
    }
}

enum class AllyPriestImageData(private val characterImage: Int){

        AP01(R.drawable.priest_ally01),AP02(R.drawable.priest_ally02),AP03(R.drawable.priest_ally03),AP04(R.drawable.priest_ally04);

    fun getCharacterImage(): Int{
        return characterImage
    }
}

enum class AllyNinjaImageData(private val characterImage: Int){

        AN01(R.drawable.ninja_ally01),AN02(R.drawable.ninja_ally02),AN03(R.drawable.ninja_ally03);

    fun getCharacterImage(): Int{
        return characterImage
    }
}

enum class EnemyFighterImageData(private val characterImage: Int){

         EF01(R.drawable.fighter_enemy01),EF02(R.drawable.fighter_enemy02),AF03(R.drawable.fighter_enemy03),EF04(R.drawable.fighter_enemy04);

    fun getCharacterImage(): Int{
        return characterImage
    }
}

enum class EnemyWizardImageData(private val characterImage: Int){

       EW01(R.drawable.wizard_enemy01),EW02(R.drawable.wizard_enemy02),EW03(R.drawable.wizard_enemy03),EW04(R.drawable.wizard_enemy04);

    fun getCharacterImage(): Int{
        return characterImage
    }
}

enum class EnemyPriestImageData(private val characterImage: Int){

    EP01(R.drawable.priest_enemy01),EP02(R.drawable.priest_enemy02),EP03(R.drawable.priest_enemy03),EP04(R.drawable.priest_enemy04);

    fun getCharacterImage(): Int{
        return characterImage
    }
}

enum class EnemyNinjaImageData(private val characterImage: Int){

          EN01(R.drawable.ninja_enemy01),EN02(R.drawable.ninja_enemy02),EN03(R.drawable.ninja_enemy03);

    fun getCharacterImage(): Int{
        return characterImage
    }
}