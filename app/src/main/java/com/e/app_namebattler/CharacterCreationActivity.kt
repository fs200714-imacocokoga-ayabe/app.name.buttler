package com.e.app_namebattler


import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_character_creation.*


class CharacterCreationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_creation)

        //val characterNumberExtra = intent.getIntExtra("characterNumber_key", 0)

        create_character_back_button_id.setOnClickListener {
            val intent = Intent(this, CharacterListActivity::class.java)
            startActivity(intent)
        }

        create_character_button_id.setOnClickListener {
            //名前を取得
            val createNameValues = findViewById<EditText>(R.id.name_input_field_text_id)
            //職業を取得
            val radioGroupJob: RadioGroup = findViewById(R.id.character_select_radiogroup_id)
            val radioId = radioGroupJob.checkedRadioButtonId
            val createJobValues: RadioButton = radioGroupJob.findViewById(radioId)
            //表示させる形式に変数を変換
            val nameValue: String = createNameValues.text.toString()
            val jobValue: String = createJobValues.text.toString()
            // 入力した名前と選択した職業を渡す
            val intent = Intent(this, CharacterCreatedActivity::class.java)
            intent.putExtra("name_key", nameValue)
            intent.putExtra("job_key", jobValue)
            //intent.putExtra("characterNumber_key", characterNumberExtra)
            startActivity(intent)
        }

    }
}


