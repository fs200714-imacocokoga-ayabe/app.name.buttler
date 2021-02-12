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

        strategy_change_determination_button_id.setOnClickListener {

            setResult(Activity.RESULT_CANCELED)

            val radioGroupStrategy: RadioGroup = findViewById(R.id.strategy_change_strategy_radioGroup_id)
            val radioId = radioGroupStrategy.checkedRadioButtonId
            var strategyName = ""

         when(radioId){

                R.id.strategy_change_strategy01_radioButton_id -> strategyName = "武器でたたかおう"
                R.id.strategy_change_strategy02_radioButton_id -> strategyName = "攻撃魔法をつかおう"
                R.id.strategy_change_strategy03_radioButton_id -> strategyName = "スキルをつかおう"
                R.id.strategy_change_strategy04_radioButton_id -> strategyName = "回復魔法をつかおう"
                R.id.strategy_change_strategy05_radioButton_id -> strategyName = "薬草をつかおう"
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
