package com.e.app_namebattler.view.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.AbsListView.CHOICE_MODE_MULTIPLE
import android.widget.ListView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.e.app_namebattler.R
import com.e.app_namebattler.model.AllyOpenHelper
import com.e.app_namebattler.view.party.player.job.JobData
import com.e.app_namebattler.view.party.player.CharacterAllData
import com.e.app_namebattler.view.view.adapter.PartyOrganizationListAdapter
import com.e.app_namebattler.view.view.fragment.PartySelectMaxDialogFragment
import com.e.app_namebattler.view.view.message.Comment
import com.e.app_namebattler.view.view.music.MusicData
import kotlinx.android.synthetic.main.activity_party_orgnization.*
import kotlinx.android.synthetic.main.data_party_organization_character_status.view.*


class PartyOrganizationActivity : AppCompatActivity() {

    lateinit var mp0: MediaPlayer

    private var nameValue01: String? = null
    private var nameValue02: String? = null
    private var nameValue03: String? = null

    private lateinit var helper: AllyOpenHelper
    private var characterList = arrayListOf<CharacterAllData>()
    private lateinit var mOrganizationListAdapter: PartyOrganizationListAdapter
    private var allyNameArray = ArrayList<String>() // 味方の名前を格納
    private var radioButtonArray = ArrayList<RadioButton>() //　ラジオボタンを格納
    private var radioNumber = 0 // チェックしているラジオボタンの数
    private val selectCountMax = 3 // 選択できるパーティメンバー数

    var name = ""
    var job = ""
    var hp = 0
    var mp = 0
    var str = 0
    var def = 0
    var agi = 0
    var luck = 0
    private val partyResetNumber = 0
    private var arrayRadioId = ArrayList<Int>()

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_party_orgnization)

        mp0 = MediaPlayer.create(this, MusicData.BGM02.getBgm())
        mp0.isLooping = true
        mp0.start()

        helper = AllyOpenHelper(applicationContext)//DB作成
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
                        c.getInt(6), c.getInt(7), c.getString(8), c.getInt(9), 100
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
        mOrganizationListAdapter = PartyOrganizationListAdapter(this, characterList)
        listView.adapter = mOrganizationListAdapter

        // このパーティで開始
        party_organization_this_party_start_button_id.setOnClickListener {

            for (i in 1..listView.count) {

                val radioName: RadioButton = listView.getChildAt(i - 1)
                    .findViewById(R.id.data_party_organization_character_status_radioButton_id)

                val characterName =
                    listView.getChildAt(i - 1).data_party_organization_character_status_name_id

                if (radioName.isChecked) {
                    allyNameArray.add(characterName.text as String)
                }
            }

            if (selectCountMax <= allyNameArray.size) {

                arrayRadioId.clear()
                val textView =
                    findViewById<TextView>(R.id.party_organization_this_party_start_button_id)
                textView.text = "このパーティで開始（".plus(partyResetNumber).plus("/3）")

                nameValue01 = allyNameArray[0].trim()//trim:文字列の先頭と末尾の半角空白を取り除く
                nameValue02 = allyNameArray[1].trim()
                nameValue03 = allyNameArray[2].trim()

                val intent = Intent(this, BattleStartActivity::class.java)

                intent.putExtra("allyName01_key", nameValue01)
                intent.putExtra("allyName02_key", nameValue02)
                intent.putExtra("allyName03_key", nameValue03)

                allyNameArray.clear()
                mp0.reset()
                startActivity(intent)

            } else {

                allyNameArray.clear()
                attentionMessage(Comment.M_SELECT_MEMBER_COMMENT.getComment())
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

        when (radioNumber) {

            0 -> {
                radioButton.isChecked = true
                radioButtonArray.add(radioButton)
                radioNumber += 1
            }

            1 -> {
                when (radioButton) {
                    radioButtonArray[0] -> {
                        radioButtonArray.remove(radioButtonArray[0])
                        radioButton.isChecked = false
                        radioNumber -= 1
                    }
                    else -> {
                        radioButtonArray.add(radioButton)
                        radioButton.isChecked = true
                        radioNumber += 1
                    }
                }
            }

            2 -> {

                when (radioButton) {
                    radioButtonArray[0] -> {
                        radioButtonArray.remove(radioButtonArray[0])
                        radioButton.isChecked = false
                        radioNumber -= 1
                    }
                    radioButtonArray[1] -> {
                        radioButtonArray.remove(radioButtonArray[1])
                        radioButton.isChecked = false
                        radioNumber -= 1
                    }
                    else -> {
                        radioButtonArray.add(radioButton)
                        radioButton.isChecked = true
                        radioNumber += 1
                    }
                }
            }

            3 -> {
                when (radioButton) {
                    radioButtonArray[0] -> {
                        radioButtonArray.remove(radioButtonArray[0])
                        radioButton.isChecked = false
                        radioNumber -= 1
                    }
                    radioButtonArray[1] -> {
                        radioButtonArray.remove(radioButtonArray[1])
                        radioButton.isChecked = false
                        radioNumber -= 1
                    }
                    radioButtonArray[2] -> {
                        radioButtonArray.remove(radioButtonArray[2])
                        radioButton.isChecked = false
                        radioNumber -= 1
                    }
                    else -> {

                        radioButton.isChecked = false
                        // ダイアフラグで　"パーティメンバーは３人です"　が表示される
                        val dialog = PartySelectMaxDialogFragment()
                        dialog.show(supportFragmentManager, "alert_dialog")
                    }
                }
            }
        }

        val textView = findViewById<TextView>(R.id.party_organization_this_party_start_button_id)
        textView.text = "このパーティで開始（".plus(radioNumber).plus("/3）")
    }

    private fun attentionMessage(message: String) {
        val layoutInflater = layoutInflater
        val customToastView: View = layoutInflater.inflate(R.layout.toast_layout_message, null)
        val toast = Toast.makeText(customToastView.context, "", Toast.LENGTH_LONG)
        toast.setGravity(Gravity.BOTTOM, 0, 180)
        (customToastView.findViewById(R.id.toast_layout_message_id) as TextView).text = message
        toast.setView(customToastView)
        toast.show()
    }

    //　数字を文字に変える
    private fun occupationConversion(jobValue: Int): String {

        when (jobValue) {

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

    override fun onPause() {
        mp0.pause()
        super.onPause()
    }

    override fun onResume() {
        mp0.start()
        super.onResume()
    }

    override fun onBackPressed() {}
}
