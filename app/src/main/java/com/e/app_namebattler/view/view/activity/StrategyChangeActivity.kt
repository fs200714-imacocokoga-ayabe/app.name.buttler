package com.e.app_namebattler.view.view.activity

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.e.app_namebattler.R
import com.e.app_namebattler.view.strategy.StrategyData
import com.e.app_namebattler.view.view.music.MusicData
import kotlinx.android.synthetic.main.activity_strategy_change.*

class StrategyChangeActivity : AppCompatActivity() {

    lateinit var mp0: MediaPlayer
    private var ts: Toast? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_strategy_change)
        
        mp0= MediaPlayer.create(this, MusicData.BGM01.getBgm())
        mp0.isLooping=true

        val strategy01TextView: TextView = findViewById(R.id.strategy_change_strategy01_name_text_id)
        strategy01TextView.text = StrategyData.S0.getStrategyName()
        val strategy02TextView: TextView = findViewById(R.id.strategy_change_strategy02_name_text_id)
        strategy02TextView.text = StrategyData.S1.getStrategyName()
        val strategy03TextView: TextView = findViewById(R.id.strategy_change_strategy03_name_text_id)
        strategy03TextView.text = StrategyData.S2.getStrategyName()
        val strategy04TextView: TextView = findViewById(R.id.strategy_change_strategy04_name_text_id)
        strategy04TextView.text = StrategyData.S3.getStrategyName()
        val strategy05TextView: TextView = findViewById(R.id.strategy_change_strategy05_name_text_id)
        strategy05TextView.text = StrategyData.S4.getStrategyName()

        strategy_change_determination_button_id.setOnClickListener {

            setResult(Activity.RESULT_CANCELED)

            val radioGroupStrategy: RadioGroup = findViewById(R.id.strategy_change_strategy_radioGroup_id)
            val radioId = radioGroupStrategy.checkedRadioButtonId
            var strategyNumber = 0

         when(radioId){

                R.id.strategy_change_strategy01_radioButton_id -> strategyNumber = StrategyData.S0.getStrategyNumber()
                R.id.strategy_change_strategy02_radioButton_id -> strategyNumber = StrategyData.S1.getStrategyNumber()
                R.id.strategy_change_strategy03_radioButton_id -> strategyNumber = StrategyData.S2.getStrategyNumber()
                R.id.strategy_change_strategy04_radioButton_id -> strategyNumber = StrategyData.S3.getStrategyNumber()
                R.id.strategy_change_strategy05_radioButton_id -> strategyNumber = StrategyData.S4.getStrategyNumber()
            }

            val result = Intent()
            result.putExtra("strategy_key", strategyNumber)

            setResult(Activity.RESULT_OK, result)
            mp0.reset()
            ts!!.cancel()
            finish()
        }
    }

    fun onClickStrategy01(view: View) {
        strategyComment(StrategyData.S0.getStrategyComment())
    }

    fun onClickStrategy02(view: View) {
        strategyComment(StrategyData.S1.getStrategyComment())
    }

    fun onClickStrategy03(view: View) {
        strategyComment(StrategyData.S2.getStrategyComment())
    }

    fun onClickStrategy04(view: View) {
        strategyComment(StrategyData.S3.getStrategyComment())
    }

    fun onClickStrategy05(view: View) {
        strategyComment(StrategyData.S4.getStrategyComment())
    }

    private fun strategyComment(message: String){

        if (ts != null){
            ts!!.cancel()
        }

        val layoutInflater = layoutInflater
        val customToastView: View = layoutInflater.inflate(R.layout.toast_layout_strategy_memo,null)
        ts = Toast.makeText(customToastView.context, "", Toast.LENGTH_SHORT)
        ts!!.setGravity(Gravity.BOTTOM,0,600)
        (customToastView.findViewById(R.id.toast_layout_strategy_comment_message_id) as TextView).text = message
        ts!!.setView(customToastView)
        ts!!.show()
    }

    override fun onDestroy() {
        mp0.release()
        super.onDestroy()
    }

}


