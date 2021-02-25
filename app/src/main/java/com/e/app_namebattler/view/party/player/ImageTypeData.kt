package com.e.app_namebattler.view.party.player

import com.e.app_namebattler.R

enum class ImageTypeData(

    private val imageTypeNumber01: Int,
    private val imageTypeNumber02: Int,
    private val imageTypeNumber03: Int,
    private val imageTypeNumber04: Int

){

        ALLY_FIGHTER_IMAGE(R.drawable.fighter_ally01,R.drawable.fighter_ally02,R.drawable.fighter_ally03,R.drawable.fighter_ally04),
        ALLY_WIZAARD_IMAGE(R.drawable.wizard_ally01, R.drawable.wizard_ally02, R.drawable.wizard_ally03,R.drawable.wizard_ally04),
        ALLY_PRIEST_IMAGE(R.drawable.priest_ally01, R.drawable.priest_ally02, R.drawable.priest_ally03,R.drawable.priest_ally04),
        ALLY_NINJA_IMAGE(R.drawable.ninja_ally01, R.drawable.ninja_ally02, R.drawable.ninja_ally03,0),
        ENEMY_FIGHTER_IMAGE(R.drawable.fighter_enemy01, R.drawable.fighter_enemy02, R.drawable.fighter_enemy03,R.drawable.fighter_enemy04),
        ENEMY_WIZAARD_IMAGE(R.drawable.wizard_enemy01, R.drawable.wizard_enemy02, R.drawable.wizard_enemy03,R.drawable.wizard_enemy04),
        ENEMY_PRIEST_IMAGE(R.drawable.priest_enemy01, R.drawable.priest_enemy02, R.drawable.priest_enemy03,R.drawable.priest_enemy04),
        ENEMY_NINJA_IMAGE(R.drawable.ninja_enemy01, R.drawable.ninja_enemy02, R.drawable.ninja_enemy03,0);

        fun getImageTypeNumber01(): Int{
            return imageTypeNumber01
        }

        fun getImageTypeNumber02(): Int{
            return imageTypeNumber02
        }

        fun getImageTypeNumber03(): Int{
            return imageTypeNumber03
        }

        fun getImageTypeNumber04(): Int{
            return imageTypeNumber04
        }
    }