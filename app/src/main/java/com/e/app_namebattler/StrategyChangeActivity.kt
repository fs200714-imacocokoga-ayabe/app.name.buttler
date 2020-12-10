package com.e.app_namebattler

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_strategy_change.*

class StrategyChangeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_strategy_change)

        strategy_determination_button_id.setOnClickListener {

            val radioId = radiogrupe_strategy_id.checkedRadioButtonId
            val strategyValue: RadioButton = radiogrupe_strategy_id.findViewById(radioId)
            val selectStrategy: String = strategyValue.text.toString()
            val data = Intent()
            val bundle = Bundle()
            bundle.putString("strategy_key",selectStrategy)
            data.putExtras(bundle)

            //intent.putExtra("strategy_key", selectStrategy)
            finish()
        }
    }

}