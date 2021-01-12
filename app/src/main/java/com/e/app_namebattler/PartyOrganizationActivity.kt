package com.e.app_namebattler

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AbsListView.CHOICE_MODE_MULTIPLE
import android.widget.ListView
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_party_orgnization.*


class PartyOrganizationActivity : AppCompatActivity() {

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



        val listView = findViewById<ListView>(R.id.list_View_party)

        listView.choiceMode = CHOICE_MODE_MULTIPLE

        mListAdapter = PartyListAdapter(this, characterList)

        listView.adapter = mListAdapter

        // 項目をタップしたときの処理
        listView.setOnItemClickListener { parent, view, position, id ->
        }

        // このパーティで開始
        this_party_start.setOnClickListener {

            for (i in 1..listView.count) {

                val radioName: RadioButton = listView.getChildAt(i - 1)
                    .findViewById(R.id.data_party_organization_character_name_radiobutton_id)

                if (radioName.isChecked) {

                    array.add(radioName.text as String)

                }

            }

            if (array.size == 3) {

                nameValue01 = array[0].trim()
                nameValue02 = array[1].trim()
                nameValue03 = array[2].trim()

                val intent = Intent(this, BattleStartActivity::class.java)

                intent.putExtra("name_key01", nameValue01)
                intent.putExtra("name_key02", nameValue02)
                intent.putExtra("name_key03", nameValue03)

                startActivity(intent)

            } else {

                for (i in 1..listView.count) {

                    val radioName: RadioButton = listView.getChildAt(i - 1)
                        .findViewById(R.id.data_party_organization_character_name_radiobutton_id)

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