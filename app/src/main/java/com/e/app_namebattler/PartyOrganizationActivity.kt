package com.e.app_namebattler

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.AbsListView.CHOICE_MODE_MULTIPLE
import android.widget.ListView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_party_orgnization.*
import kotlinx.android.synthetic.main.data_party_organization_character_status.view.*


class PartyOrganizationActivity : AppCompatActivity() {

    lateinit var mp0: MediaPlayer

    private var nameValue01: String? = null
    private var nameValue02: String? = null
    private var nameValue03: String? = null

    lateinit var helper: MyOpenHelper
    var characterList = arrayListOf<CharacterAllData>()
    private lateinit var mListAdapter: PartyListAdapter
    var array = ArrayList<String>()

    var name = ""
    var job = ""
    var hp = 0
    var mp = 0
    var str = 0
    var def = 0
    var agi = 0
    var luck = 0
    var radioNumber = 0
    var arrayRadioId = ArrayList<Int>()


    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_party_orgnization)

        mp0= MediaPlayer.create(this,R.raw.neighofwar)
        mp0.isLooping=true
        mp0.start()

        helper = MyOpenHelper(applicationContext)//DB作成
        val db = helper.readableDatabase
        characterList.clear()

        try {
            // rawQueryというSELECT専用メソッドを使用してデータを取得する
            val c = db.rawQuery(
                "select NAME, JOB, HP, MP, STR, DEF, AGI, LUCK, CREATE_AT from CHARACTER",
                null
            )
            // Cursorの先頭行があるかどうか確認
            var next = c.moveToFirst()

            // 取得した全ての行を取得
            while (next) {
                // 取得したカラムの順番(0から始まる)と型を指定してデータを取得する
                characterList.add(
                    CharacterAllData(
                        c.getString(0), (occupationConversion(c.getInt(1))),
                        c.getInt(2), c.getInt(3), c.getInt(4), c.getInt(5),
                        c.getInt(6), c.getInt(7), c.getString(8)
                    )
                )
                next = c.moveToNext()
            }

        } finally {
            // finallyは、tryの中で例外が発生した時でも必ず実行される
            // dbを開いたら確実にclose
            db.close()
        }

        val listView = findViewById<ListView>(R.id.list_View_party)

        listView.choiceMode = CHOICE_MODE_MULTIPLE
        mListAdapter = PartyListAdapter(this, characterList)
        listView.adapter = mListAdapter

        // 項目をタップしたときの処理
//        listView.setOnItemClickListener { parent, view, position, id ->
//        }

        // このパーティで開始
        this_party_start.setOnClickListener {

            radioNumber = 0
            arrayRadioId.clear()
            val textView = findViewById<TextView>(R.id.this_party_start)
            textView.text = "このパーティで開始（".plus(radioNumber).plus("/3）")

            for (i in 1..listView.count) {

                val radioName: RadioButton = listView.getChildAt(i - 1)
                    .findViewById(R.id.data_party_organization_radio_button_id)

                val characterName = listView.getChildAt(i - 1).data_party_organization_character_name_id

                if (radioName.isChecked) {
                    array.add(characterName.text as String)
                }
            }

            if (array.size == 3) {

                nameValue01 = array[0].trim()//trim:文字列の先頭と末尾の半角空白を取り除く
                nameValue02 = array[1].trim()
                nameValue03 = array[2].trim()

                val intent = Intent(this, BattleStartActivity::class.java)

                intent.putExtra("name_key01", nameValue01)
                intent.putExtra("name_key02", nameValue02)
                intent.putExtra("name_key03", nameValue03)

                mp0.reset()
                startActivity(intent)
            } else {
                for (i in 1..listView.count) {
                    val radioName: RadioButton = listView.getChildAt(i - 1)
                        .findViewById(R.id.data_party_organization_character_name_id)

                    if (radioName.isChecked) {
                        radioName.isChecked = false
                    }
                }

                array.clear()

                Toast.makeText(this, "3人選択してください", Toast.LENGTH_SHORT).show()
            }
        }


        // 戻るボタン
        party_organizetion_back_button.setOnClickListener {
            val intent = Intent(this, TopScreenActivity::class.java)
            mp0.reset()
            startActivity(intent)
        }
    }

    fun radioCheck(view: View) {

        var rNum = 0
        radioNumber  = 0
        val radioButton = view as RadioButton

        //radioButton.isChecked = true

        val listView = findViewById<ListView>(R.id.list_View_party)

        for (i in 1..listView.count) {

            val radioName: RadioButton = listView.getChildAt(i - 1)
                .findViewById(R.id.data_party_organization_radio_button_id)

            if (radioName.isChecked) {
                rNum += 1

            }
        }

        if (rNum > 3){
            radioButton.isChecked = false
            //  radioNumber -= 1
            rNum -= 1
            Toast.makeText(applicationContext, "パーティは３人です", Toast.LENGTH_SHORT).show()


        }else{
            radioButton.isChecked = true
            // radioNumber += 1
        }
//
//        if (radioButt){
//            radioButton.isChecked = false
//            rNum -= 1
//        }
//
//                val radioName: RadioButton = listView.getChildAt(i - 1)
//                    .findViewById(R.id.data_party_organization_character_name_radiobutton_id)
//        if(radioNumber <= 2) {
//
//            for (i in 1..listView.count) {
//
//                val radioName: RadioButton = listView.getChildAt(i - 1)
//                    .findViewById(R.id.data_party_organization_character_name_radiobutton_id)
//
//                if (radioName.isChecked ) {
//                    if (radioName.isChecked == radioButton.isChecked) {
//                        println("ラジオネーム$radioName.isChecked")
//                        println("ラジオボタン$radioButton.isChecked")
//                   //     radioButton.isChecked = !radioButton.isChecked
//
//                        if (radioName.isChecked) {
//                            radioButton.isChecked = false
//
//                        } else {
//                            radioName.isChecked = true
//                            radioNumber += 1
//
//                        }
//                    }
////                    radioNumber -= 1
//                }
//            }
//
//        }
//        if (radioNumber > 3) {
//            Toast.makeText(applicationContext, "パーティは３人です", Toast.LENGTH_LONG).show()
//           // radioButton.isChecked = false
//          //  radioNumber -= 1
//        }
////
////        }else if (radioNumber <= 2){
////            radioButton.isChecked = true
////            radioNumber += 1
//            val textView = findViewById<TextView>(R.id.this_party_start)
//            textView.text = "このパーティで開始（".plus(radioNumber).plus("/3）")
////
//
//
//                // ラジオボタンの選択状態を取得
//
////        var radioa = view
////        var radiob = view
////        var radioc = view
////                // getId()でラジオボタンを識別し、ラジオボタンごとの処理を行う
////        if (radioNumber == 0){
////            radioa = radioButton
////        }else if (radioNumber == 1){
////            radiob = radioButton
////        }else if (radioNumber == 2){
////            radioc = radioButton
////        }
////
////        if (radioButton == radiob){
////
////        }
////

        //     val checked = radioButton.isChecked

        //     if (radioButton.isChecked && rNum > 3){

        // if (rNum > 2 ) {
//                        Toast.makeText(applicationContext, "パーティは３人です", Toast.LENGTH_LONG).show()
        //            radioButton.isChecked = false
        // radioNumber -= 1

//                    }else if (radioNumber <= 3){
//                      //  radioButton.isChecked = true
//                       // radioNumber += 1
////                        val textView = findViewById<TextView>(R.id.this_party_start)
////                        textView.text = "このパーティで開始（".plus(radioNumber).plus("/3）")
//
        //  }
        //   }
        val textView = findViewById<TextView>(R.id.this_party_start)
        textView.text = "このパーティで開始（".plus(rNum).plus("/3）")

    }
//-----------------------------------------------------------------
//    val checked = radioButton.isChecked
//
//    if (checked){
//
//        if (radioNumber > 2 ) {
//            Toast.makeText(applicationContext, "パーティは３人です", Toast.LENGTH_LONG).show()
//            radioButton.isChecked = false
//            radioNumber -= 1
//
//        }else if (radioNumber <= 2 && !radioButton.isChecked){
//            radioButton.isChecked = true
//            radioNumber += 1
////                        val textView = findViewById<TextView>(R.id.this_party_start)
////                        textView.text = "このパーティで開始（".plus(radioNumber).plus("/3）")
//
//        }
//    }
//--------------------------------------------------------------

    //　数字を文字に変える
    private fun occupationConversion(jobValue: Int): String{

        when(jobValue){

            0 -> job = "戦士"
            1 -> job = "魔法使い"
            2 -> job = "僧侶"
            3 -> job = "忍者"
        }
        return job
    }

    override fun onDestroy() {
        mp0.release()
        super.onDestroy()
    }

    override fun onBackPressed() {}
}
