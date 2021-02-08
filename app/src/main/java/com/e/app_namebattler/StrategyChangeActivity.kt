package com.e.app_namebattler

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_strategy_change.*

class StrategyChangeActivity : AppCompatActivity() {

    lateinit var mp0: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_strategy_change)

        mp0= MediaPlayer.create(this,R.raw.lastwar)
        mp0.isLooping=true

        strategy_determination_button_id.setOnClickListener {

            setResult(Activity.RESULT_CANCELED)

            val radioGroupStrategy: RadioGroup = findViewById(R.id.radio_grupe_strategy_id)
            val radioId = radioGroupStrategy.checkedRadioButtonId
            var strategyName = ""

         when(radioId){

                R.id.radiobutton_strategy01 -> strategyName = "武器でたたかおう"
                R.id.radiobutton_strategy02 -> strategyName = "攻撃魔法をつかおう"
                R.id.radiobutton_strategy03 -> strategyName = "スキルをつかおう"
                R.id.radiobutton_strategy04 -> strategyName = "回復魔法をつかおう"
                R.id.radiobutton_strategy05 -> strategyName = "薬草をつかおう"
            }

            val result = Intent()
            result.putExtra("strategy_key", strategyName)

            setResult(Activity.RESULT_OK, result)
            mp0.reset()
            finish()
        }
    }

    override fun onDestroy() {
        mp0.release()
        super.onDestroy()
    }

    override fun onBackPressed() {}
}
