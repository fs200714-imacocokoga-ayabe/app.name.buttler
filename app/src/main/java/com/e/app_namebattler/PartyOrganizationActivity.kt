package com.e.app_namebattler

import android.R
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_party_orgnization.*


class PartyOrganizationActivity : AppCompatActivity() {

    private var nameValue01: String? = null
    private var nameValue02: String? = null
    private var nameValue03: String? = null

    lateinit var helper: MyOpenHelper
    var characterList = arrayListOf<CharacterAllData>()

    private lateinit var mListAdapter: PartyListAdapter

    var array = ArrayList<Int>()

    var name = ""
    var job = ""
    var hp = 0
    var mp = 0
    var str = 0
    var def = 0
    var agi = 0
    var luck = 0
    var create_at = ""

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.e.app_namebattler.R.layout.activity_party_orgnization)

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
                        c.getString(0), (OccupationConversion01(c.getInt(1))),
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

        val listView = findViewById<ListView>(com.e.app_namebattler.R.id.list_View_party)

        mListAdapter = PartyListAdapter(this, characterList)

        listView.adapter = mListAdapter


//        val radioGroup = findViewById<View>(com.e.app_namebattler.R.id.radiogroup_id) as RadioGroup
//        radioGroup.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
//            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
//                if (checkedId != -1) {
//                    // 選択されているラジオボタンの取得
//                    val radioButton = findViewById<View>(checkedId) as RadioButton
//
//                    // ラジオボタンのテキストを取得
//                    val text = radioButton.text.toString()
//                    Log.v("checked", text)
//                    println("ログラジオ$text")
//                } else {
//                    // 何も選択されていない場合の処理
//                }
//            }
//        })



      //  val radioGroup = findViewById<RadioGroup>(R.id.radiogroup_id)

      //  val id: Int = radioGroup.checkedRadioButtonId

       // val id = radioGroup.checkedRadioButtonId


//
//        val radioButton = findViewById<RadioButton>(id)
//
//        val index = radioGroup.indexOfChild(radioButton)

   //

//        radioGroup.setOnCheckedChangeListener  { group, checkedId ->
//
//            val radioButton = findViewById<RadioButton>(checkedId)
//        }
        // 項目をタップしたときの処理
        listView.setOnItemClickListener { parent, view, position, id ->

                // チェックされてないアイテムは含まれない模様
            //    val checked = listView.checkedItemPosition

              //  for (i in 1.. checked.size()) {

//                    val key = checked.keyAt (i)
            //val key = checked
                //    array(checked)
              //  }
//
//            // テキストの表示
//            val textView = findViewById<TextView>(R.id.this_party_start)
//            textView.text = "このパーティーで開始(".plus(characterList.size).plus("人)")

//            for (i in 0..listView.adapter.count){
//
//                if (listView.adapter.getItemId(i) == id){
//
//                    listView.adapter.getItem(i).setChecked(ture)
//                }else{
//                    listView.adapter.getItem(i).setChecked(false)
//                }
//            }


//            val nameValue = characterList[position].name
//            val intent = Intent(this, CharacterDetailActivity::class.java)
//            intent.putExtra("name_key", nameValue)
//            startActivity(intent)
        }

        // このパーティで開始
                  this_party_start.setOnClickListener {

//                      for (i in characterList) {
//
//                          val radioGroup: RadioGroup = findViewById(com.e.app_namebattler.R.id.radiogroup_id)
//
//                          val radioId = radioGroup.checkedRadioButtonId
//
//                          val aaa: RadioButton = radioGroup.findViewById(radioId)
//
//                          println("ログラジオ$aaa")
//                      }
////
//                // チェックされてないアイテムは含まれない模様
//                val checked = listView.checkedItemPositions
//
//                for (i in 1.. checked.size()) {
//
//                    val key = checked.keyAt (i)
//
//                    array.plusAssign(key)
//                }



            nameValue01 = characterList[listView.firstVisiblePosition].name
            nameValue02 = characterList[listView.firstVisiblePosition + 1].name
            nameValue03 = characterList[listView.firstVisiblePosition + 2].name

//            nameValue01 = characterList[array[0]].name
//            nameValue02 = characterList[array[1]].name
//            nameValue03 = characterList[array[2]].name

            val intent = Intent(this, BattleStartActivity::class.java)

            intent.putExtra("name_key01", nameValue01)
            intent.putExtra("name_key02", nameValue02)
            intent.putExtra("name_key03", nameValue03)

            startActivity(intent)
        }

            // 戻るボタン
            party_organizetion_back_button.setOnClickListener {
                val intent = Intent(this, TopScreenActivity::class.java)
                startActivity(intent)

            }
    }

    fun OccupationConversion01(jobValue: Int): String{

        when(jobValue){

            0 -> {
                job = "戦士"
            }
            1 -> {
                job = "魔法使い"
            }
            2 -> {
                job = "僧侶"
            }
            3 -> {
                job = "忍者"
            }
        }
        return job

    }
}