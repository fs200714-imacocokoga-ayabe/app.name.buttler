package com.e.app_namebattler

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_strategy_change.*

class StrategyChangeActivity : AppCompatActivity() {

    val gm = GameManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_strategy_change)

        strategy_determination_button_id.setOnClickListener {

            setResult(Activity.RESULT_CANCELED)

            val radioGroupStrategy: RadioGroup = findViewById(R.id.radiogrupe_strategy_id)

            val radioId = radioGroupStrategy.checkedRadioButtonId

            val strategyValues: RadioButton = radioGroupStrategy.findViewById(radioId)

            val strategyName:String = strategyValues.text.toString()

            val result = Intent()

            result.putExtra("strategy_key", strategyName)

            setResult(Activity.RESULT_OK, result)

            finish()
        }
    }
}
