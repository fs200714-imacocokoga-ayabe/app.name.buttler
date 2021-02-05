package com.e.app_namebattler


import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_character_creation.*


class CharacterCreationActivity : AppCompatActivity() ,TextWatcher{

    lateinit var mp0: MediaPlayer
    lateinit var helper: MyOpenHelper
    var isSameName :Boolean = false
    private var inputStr: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_creation)

        mp0= MediaPlayer.create(this, R.raw.yokoku)
        mp0.isLooping=true
        mp0.start()

        val createNameValues = findViewById<EditText>(R.id.name_input_field_text_id)
        createNameValues.addTextChangedListener(this)

        create_character_back_button_id.setOnClickListener {
            val intent = Intent(this, CharacterListActivity::class.java)
            mp0.reset()
            startActivity(intent)
        }

        create_character_button_id.setOnClickListener {
          //職業を取得
            val radioGroupJob: RadioGroup = findViewById(R.id.character_select_radiogroup_id)
            val radioId = radioGroupJob.checkedRadioButtonId
            val createJobValues: RadioButton = radioGroupJob.findViewById(radioId)
            //表示させる形式に変数を変換
            val nameValue: String = createNameValues.text.toString()

            if (sameNameCheck(nameValue)){

                Toast.makeText(this, "同じ名前がすでに存在しています", Toast.LENGTH_SHORT).show()

            }else {

                val jobValue: String = createJobValues.text.toString()
                // 入力した名前と選択した職業を渡す
                val intent = Intent(this, CharacterCreatedActivity::class.java)
                intent.putExtra("name_key", nameValue)
                intent.putExtra("job_key", jobValue)
                //intent.putExtra("characterNumber_key", characterNumberExtra)
                mp0.reset()
                startActivity(intent)
            }
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    @SuppressLint("ResourceAsColor")
    override fun afterTextChanged(s: Editable?) {

        inputStr = s.toString()

        if (inputStr.length > 20) {
            val ic = Toast.makeText(this,
                "名前は20文字までです",
                Toast.LENGTH_SHORT)
            ic.setGravity(Gravity.CENTER, 0, 0)
            //  ic.view?.setBackgroundColor(R.color.design_default_color_primary_dark)
            ic.show()

            val editText = findViewById<View>(R.id.name_input_field_text_id) as EditText
            editText.setText("")
        }
    }

    private fun sameNameCheck(nameValue: String?): Boolean{

        helper = MyOpenHelper(applicationContext)//DB作成

        val db = helper.readableDatabase

        try {
            // rawQueryというSELECT専用メソッドを使用してデータを取得する
            var c = db.rawQuery(
                "select * from CHARACTER WHERE name = '$nameValue'",
                null
            )

            isSameName = c.moveToFirst()
            return isSameName

        } finally {
            // finallyは、tryの中で例外が発生した時でも必ず実行される
            // dbを開いたら確実にclose
            db.close()
        }
    }
    override fun onDestroy() {
        mp0.release()
        super.onDestroy()
    }

    override fun onBackPressed() {}

}


