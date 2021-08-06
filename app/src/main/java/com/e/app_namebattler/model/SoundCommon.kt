package com.e.app_namebattler.model

import android.app.Application
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build
import androidx.annotation.RequiresApi
import com.e.app_namebattler.view.view.music.SoundData

class SoundCommon : Application() {

    private lateinit var sp0: SoundPool
    private var snd0 = 0
    private var snd1 = 0
    private var snd2 = 0
    private var snd3 = 0
    private var snd4 = 0
    private var snd5 = 0
    private var snd6 = 0
    private var snd7 = 0
    private var snd8 = 0
    private var snd9 = 0
    private var snd10 = 0
    private var snd11 = 0
    private var snd12 = 0
    private var snd13 = 0
    private var snd14 = 0
    private var snd15 = 0

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate() {
        super.onCreate()
        initSound()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initSound() {

        val aa0 = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(
            AudioAttributes.CONTENT_TYPE_SPEECH).build()

        sp0 = SoundPool.Builder().setAudioAttributes(aa0).setMaxStreams(1).build()
        snd0 = sp0.load(this, SoundData.S_SWORD01.getSound(), 1)
        snd1 = sp0.load(this, SoundData.S_KATANA01.getSound(), 1)
        snd2 = sp0.load(this, SoundData.S_PUNCH01.getSound(), 1)
        snd3 = sp0.load(this, SoundData.S_SYURIKEN01.getSound(), 1)
        snd4 = sp0.load(this, SoundData.S_FIRE01.getSound(), 1)
        snd5 = sp0.load(this, SoundData.S_THUNDER01.getSound(), 1)
        snd6 = sp0.load(this, SoundData.S_POISON01.getSound(), 1)
        snd7 = sp0.load(this, SoundData.S_PARALYSIS01.getSound(), 1)
        snd8 = sp0.load(this, SoundData.S_HEAL01.getSound(), 1)
        snd9 = sp0.load(this, SoundData.S_RECOVERY01.getSound(), 1)
        snd10 = sp0.load(this, SoundData.S_KATANA02.getSound(), 1)
        snd11 = sp0.load(this, SoundData.S_POISON_DAMAGE.getSound(), 1)
        snd12 = sp0.load(this, SoundData.S_SLIDE01.getSound(), 1)
        snd13 = sp0.load(this, SoundData.S_SWORD02.getSound(), 1)
        snd14 = sp0.load(this, SoundData.S_SWORD01_AIR_SHOT.getSound(), 1)
        snd15 = sp0.load(this, SoundData.S_SWORD02_AIR_SHOT.getSound(), 1)

    }

    fun sound(sound: Int) {

        when (sound) {

            0 -> { println() }
            1 -> sp0.play(snd0, 1.0f, 1.0f, 0, 0, 1.0f)// sword
            2 -> sp0.play(snd1, 1.0f, 1.0f, 0, 0, 1.0f)// katana
            3 -> sp0.play(snd2, 1.0f, 1.0f, 0, 0, 1.0f)// punch
            4 -> sp0.play(snd3, 1.0f, 1.0f, 0, 0, 1.0f)// syuriken
            5 -> sp0.play(snd4, 1.0f, 1.0f, 0, 0, 1.0f)// fire
            6 -> sp0.play(snd5, 1.0f, 1.0f, 0, 0, 1.0f)// thunder
            7 -> sp0.play(snd6, 1.0f, 1.0f, 0, 0, 1.0f)// poison
            8 -> sp0.play(snd7, 1.0f, 1.0f, 0, 0, 1.0f)// paralysis
            9 -> sp0.play(snd8, 1.0f, 1.0f, 0, 0, 1.0f)// heal
            10 -> sp0.play(snd9, 1.0f, 1.0f, 0, 0, 1.0f)// recovery
            11 -> sp0.play(snd10, 1.0f, 1.0f, 0, 0, 1.0f)// katana02
            12 -> sp0.play(snd11, 1.0f, 1.0f, 0, 0, 1.0f)// poison-damage
            13 -> sp0.play(snd12, 1.0f, 1.0f, 0, 0, 1.0f)// 滑る
            14 -> sp0.play(snd13, 1.0f, 1.0f, 0, 0, 1.0f)//
            15 -> sp0.play(snd14, 1.0f, 1.0f, 0, 0, 1.0f)//
            16 -> sp0.play(snd15, 1.0f, 1.0f, 0, 0, 1.0f)//
        }
    }
}