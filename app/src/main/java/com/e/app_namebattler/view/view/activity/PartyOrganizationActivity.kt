package com.e.app_namebattler.view.view.activity

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
import com.e.app_namebattler.R
import com.e.app_namebattler.model.MyOpenHelper
import com.e.app_namebattler.view.party.job.JobData
import com.e.app_namebattler.view.party.player.CharacterAllData
import com.e.app_namebattler.view.view.adapter.PartyListAdapter
import com.e.app_namebattler.view.view.fragment.PartySelectMaxDialogFragment
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
    var array02 = ArrayList<RadioButton>()
    var rNum = 0

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

        mp0= MediaPlayer.create(this, R.raw.neighofwar)
        mp0.isLooping=true
        mp0.start()

        helper = MyOpenHelper(applicationContext)//DB作成
        val db = helper.readableDatabase
        characterList.clear()

        try {
            // rawQueryというSELECT専用メソッドを使用してデータを取得する
            val c = db.rawQuery(
                "select NAME, JOB, HP, MP, STR, DEF, AGI, LUCK, CREATE_AT, CHARACTER_IMAGE from CHARACTER",
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
                        c.getInt(6), c.getInt(7), c.getString(8), c.getInt(9)
                    )
                )
                next = c.moveToNext()
            }

        } finally {
            // finallyは、tryの中で例外が発生した時でも必ず実行される
            // dbを開いたら確実にclose
            db.close()
        }

        val listView = findViewById<ListView>(R.id.party_organization_party_list_listView_id)

        listView.choiceMode = CHOICE_MODE_MULTIPLE
        mListAdapter = PartyListAdapter(this, characterList)
        listView.adapter = mListAdapter

        // このパーティで開始
        party_organization_this_party_start_button_id.setOnClickListener {

            radioNumber = 0
            arrayRadioId.clear()
            val textView = findViewById<TextView>(R.id.party_organization_this_party_start_button_id)
            textView.text = "このパーティで開始（".plus(radioNumber).plus("/3）")

            for (i in 1..listView.count) {

                val radioName: RadioButton = listView.getChildAt(i - 1)
                    .findViewById(R.id.data_party_organization_character_status_radioButton_id)

                val characterName = listView.getChildAt(i - 1).data_party_organization_character_status_name_id

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
                        .findViewById(R.id.data_party_organization_character_status_name_id)

                    if (radioName.isChecked) {
                        radioName.isChecked = false
                    }
                }

                array.clear()

                Toast.makeText(this, "3人選択してください", Toast.LENGTH_SHORT).show()
            }
        }


        // 戻るボタン
        party_organization_back_button.setOnClickListener {
            val intent = Intent(this, TopScreenActivity::class.java)
            mp0.reset()
            startActivity(intent)
        }
    }

    @SuppressLint("ResourceType")
    fun radioCheck(view: View) {

        val radioButton = view as RadioButton

        if (rNum == 0) {

            radioButton.isChecked = true
            array02.add(radioButton)
            rNum += 1

        } else if (rNum == 1) {

            if (array02[0] == radioButton) {
                array02.remove(array02[0])
                radioButton.isChecked = false
                rNum -= 1

            } else {
                array02.add(radioButton)
                radioButton.isChecked = true
                rNum += 1
            }

        }else if (rNum == 2) {

            if (array02[0] == radioButton) {
                array02.remove(array02[0])
                radioButton.isChecked = false
                rNum -= 1

            } else if (array02[1] == radioButton) {
                array02.remove(array02[1])
                radioButton.isChecked = false
                rNum -= 1

            } else {
                array02.add(radioButton)
                radioButton.isChecked = true
                rNum += 1
            }

        }else if (rNum == 3){

            if (array02[0] == radioButton) {
                array02.remove(array02[0])
                radioButton.isChecked = false
                rNum -= 1

            } else if (array02[1] == radioButton) {
                array02.remove(array02[1])
                radioButton.isChecked = false
                rNum -= 1

            } else if (array02[2] == radioButton){
                array02.remove(array02[2])
                radioButton.isChecked = false
                rNum -= 1

            }else {

                radioButton.isChecked = false
                // ダイアフラグで　"パーティメンバーは３人です"　が表示される
                val dialog = PartySelectMaxDialogFragment()
                dialog.show(supportFragmentManager, "alert_dialog")
            }
        }

        val textView = findViewById<TextView>(R.id.party_organization_this_party_start_button_id)
        textView.text = "このパーティで開始（".plus(rNum).plus("/3）")
    }

    //　数字を文字に変える
    private fun occupationConversion(jobValue: Int): String{

        when(jobValue){

            0 -> job = JobData.FIGHTER.getJobName()
            1 -> job = JobData.WIZARD.getJobName()
            2 -> job = JobData.PRIEST.getJobName()
            3 -> job = JobData.NINJA.getJobName()
        }
        return job
    }

    override fun onDestroy() {
        mp0.release()
        super.onDestroy()
    }

    override fun onBackPressed() {}
}
