package com.e.app_namebattler

import android.app.Application

class CharacterData : Application()  {

    var ally01: Player? = null
    var ally02: Player? = null
    var ally03: Player? = null
    var enemy01: Player? = null
    var enemy02: Player? = null
    var enemy03: Player? = null

    companion object{

        private var instance: CharacterData? = null

        fun getInstance(): CharacterData{
            if (instance == null)
                instance = CharacterData()

            return instance!!
        }
    }
}

