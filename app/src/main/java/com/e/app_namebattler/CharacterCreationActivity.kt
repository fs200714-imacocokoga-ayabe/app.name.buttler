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
            val radiogroupJob: RadioGroup = findViewById(R.id.character_select_radiogroup_id)
            val radioId = radiogroupJob.checkedRadioButtonId
            val createJobValues: RadioButton = radiogroupJob.findViewById(radioId)
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

//         fun onCreate(savedInstanceState: Bundle?){
//            super.onCreate(savedInstanceState)
//            setContentView(R.layout.activity_character_creation)
//            // ラジオグループのオブジェクトを取得
//            val rg = findViewById<RadioGroup>(R.id.character_select_radiogroup_id)
//             // チェックされているラジオボタンの ID を取得
//             val id = rg.checkedRadioButtonId
//             // チェックされているラジオボタンオブジェクトを取得
//           //  val radioButton = findViewById<RadioButton>(id)
//             // チェック状態変更時に呼び出されるメソッド
//             rg.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
//                 // チェック状態時の処理を記述
//                 // チェックされたラジオボタンオブジェクトを取得
//                 override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
//                     val radioButton = findViewById<RadioButton>(id)
//                 }
//             })
//        }

//         fun onDestroy(){
//             //ヘルパーオブジェクトの開放。
//            helper.close()
//            super.onDestroy()
//        }


//
//        fun onCreateCharacterButtonClick(){
//
//            val name_input_text_field_id = findViewById<EditText>(R.id.name_input_field_text_id)
//            val Name = name_input_text_field_id.text.toString()
//
//
//
//
//          //  player?.makeCharacter()
////        //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得。
////            val db = helper.writableDatabase
//        }

//
//        int checkedId = radioGroup.getCheckedRadioButtonId(character_select_radiogroup_id)
//
//        if (checkedId != -1) {
//            // 選択されているラジオボタンの取得
//            RadioButton radioButton =(RadioButton) findViewById(checkedId);// (Fragmentの場合は「getActivity().findViewById」にする)
//
//            // ラジオボタンのテキストを取得
//            String text = radioButton.getText().toString();
//
//            Log.v("checked", text);
//        } else {
//            // 何も選択されていない場合の処理
//        }

//
//        val radioGroup = findViewById<View>(R.id.character_select_radiogroup_id) as RadioGroup
//        radioGroup.setOnCheckedChangeListener { radioGroup, checkedRadioButtonId ->
//            val checkedButton = findViewById<View>(checkedRadioButtonId) as RadioButton
//            Toast.makeText(this@CharacterCreationActivity, checkedButton.text, Toast.LENGTH_SHORT).show()
//        }
//        radioGroup = findViewById<View>(R.id.character_select_radiogroup) as RadioGroup
//        radioGroup.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener() {
//            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
//                if (checkedId != -1) {
//                    // 選択されているラジオボタンの取得
//                    val radioButton = findViewById<View>(checkedId) as RadioButton
//
//                    // ラジオボタンのテキストを取得
//                    val text = radioButton.text.toString()
//                    Log.v("checked", text)
//                } else {
//                    // 何も選択されていない場合の処理
//                }
//            }
//        })

//        override fun onCreate(savedInstanceState: Bundle?) {
//            super.onCreate(savedInstanceState)
//            setContentView(R.layout.activity_main)
//            Toast.makeText(applicationContext, "これはトーストです", Toast.LENGTH_SHORT).show();
//        }

