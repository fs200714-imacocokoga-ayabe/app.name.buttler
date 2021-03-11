package com.e.app_namebattler.view.view.activity

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.e.app_namebattler.R
import com.e.app_namebattler.view.strategy.StrategyName
import com.e.app_namebattler.view.view.music.MusicData
import kotlinx.android.synthetic.main.activity_strategy_change.*

class StrategyChangeActivity : AppCompatActivity() {

    lateinit var mp0: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_strategy_change)

       // mp0= MediaPlayer.create(this, R.raw.lastwar)
        mp0= MediaPlayer.create(this, MusicData.BGM01.getBgm())
        mp0.isLooping=true

        strategy_change_determination_button_id.setOnClickListener {

            setResult(Activity.RESULT_CANCELED)

            val radioGroupStrategy: RadioGroup = findViewById(R.id.strategy_change_strategy_radioGroup_id)
            val radioId = radioGroupStrategy.checkedRadioButtonId
            var strategyNumber = 0

         when(radioId){

                R.id.strategy_change_strategy01_radioButton_id -> strategyNumber = StrategyName.S0.getStrategyNumber()
                R.id.strategy_change_strategy02_radioButton_id -> strategyNumber = StrategyName.S1.getStrategyNumber()
                R.id.strategy_change_strategy03_radioButton_id -> strategyNumber = StrategyName.S2.getStrategyNumber()
                R.id.strategy_change_strategy04_radioButton_id -> strategyNumber = StrategyName.S3.getStrategyNumber()
                R.id.strategy_change_strategy05_radioButton_id -> strategyNumber = StrategyName.S4.getStrategyNumber()
            }

            val result = Intent()
            result.putExtra("strategy_key", strategyNumber)

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


