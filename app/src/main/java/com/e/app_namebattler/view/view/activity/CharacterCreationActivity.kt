package com.e.app_namebattler.view.view.activity


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
import com.e.app_namebattler.model.MyOpenHelper
import com.e.app_namebattler.R
import kotlinx.android.synthetic.main.activity_character_creation.*


class CharacterCreationActivity : AppCompatActivity() ,TextWatcher{

    lateinit var mp0: MediaPlayer
    lateinit var helper: MyOpenHelper
    var isSameName :Boolean = false // 同じ名前かどうか true:同じ名前 false:違う名前
    private var inputStr: String = "" // editTextに入力された文字を格納

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_creation)

        mp0= MediaPlayer.create(this, R.raw.yokoku)
        mp0.isLooping=true
        mp0.start()

        // textWatcherのリスナーを登録　
        val createNameValues = findViewById<EditText>(R.id.character_creation_name_input_field_editText_id)
        createNameValues.addTextChangedListener(this)

        // 戻るボタンを押した時の処理
        character_creation_create_character_back_button_id.setOnClickListener {
            val intent = Intent(this, CharacterListActivity::class.java)
            mp0.reset()
            startActivity(intent)
        }

        // 作成するボタンを押したときの処理
        character_creation_create_character_button_id.setOnClickListener {
          //職業を取得
            val radioGroupJob: RadioGroup = findViewById(R.id.character_creation_character_select_radioGroup_id)
            val radioId = radioGroupJob.checkedRadioButtonId
            val createJobValues: RadioButton = radioGroupJob.findViewById(radioId)
            //表示させる形式に変数を変換
            val nameValue: String = createNameValues.text.toString()
            // データベースの中に同じ名前が存在するかのチェック
            if (sameNameCheck(nameValue)){

                Toast.makeText(this, "同じ名前がすでに存在しています", Toast.LENGTH_SHORT).show()

            }else {

                val jobValue: String = createJobValues.text.toString()
                // 入力した名前と選択した職業を渡す
                val intent = Intent(this, CharacterCreatedActivity::class.java)
                // 名前と職業をCharacterCreateActivityに渡す
                intent.putExtra("name_key", nameValue)
                intent.putExtra("job_key", jobValue)
                mp0.reset()
                startActivity(intent)
            }
        }
    }
    // ---textWatcher---
    // 文字列が修正される直前に呼び出される
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    // 文字１つを入力した時に呼び出される
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    // 最後にこのメソッドが呼び出される
    @SuppressLint("ResourceAsColor")
    override fun afterTextChanged(s: Editable?) {

        inputStr = s.toString()

        if (inputStr.length > 10) {
            val ic = Toast.makeText(this,
                "名前は10文字までです",
                Toast.LENGTH_SHORT)
            ic.setGravity(Gravity.TOP, -0, 400)
             // ic.view?.setBackgroundColor(R.color.design_default_color_primary_dark)
            ic.show()

            val editText = findViewById<View>(R.id.character_creation_name_input_field_editText_id) as EditText
            editText.setText("")
        }
    }

    // 同じ名前がデータベースに存在しているかのチェック
    private fun sameNameCheck(nameValue: String?): Boolean{

        helper = MyOpenHelper(applicationContext)//DB作成

        val db = helper.readableDatabase

        try {
            // rawQueryというSELECT専用メソッドを使用してデータを取得する
            val c = db.rawQuery(
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


