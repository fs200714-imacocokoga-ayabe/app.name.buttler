package com.e.app_namebattler.view.view.activity

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.e.app_namebattler.*
import com.e.app_namebattler.model.MyOpenHelper
import com.e.app_namebattler.view.party.job.JobData
import com.e.app_namebattler.view.party.player.CharacterAllData
import com.e.app_namebattler.view.view.fragment.CharacterCreateMaxDialogFragment
import com.e.app_namebattler.view.view.adapter.ListAdapter
import kotlinx.android.synthetic.main.activity_character_list.*

//キャラクター一覧画面のクラス
class CharacterListActivity : AppCompatActivity(){

    lateinit var mp0: MediaPlayer
    lateinit var helper: MyOpenHelper
    var  characterList = arrayListOf<CharacterAllData>()
    private lateinit var mListAdapter: ListAdapter

    var name = ""
    var job = ""
    var hp = 0
    var mp = 0
    var str = 0
    var def = 0
    var agi = 0
    var luck = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        mp0= MediaPlayer.create(this, R.raw.yokoku)
        mp0.isLooping=true
        mp0.start()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_list)

            helper = MyOpenHelper(applicationContext)//DB作成

            val db = helper.readableDatabase

                characterList.clear()

        // データベースからキャラクターを取得
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
                                    c.getString(0),(occupationConversion(c.getInt(1))),
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

             // listViewにListAdapterを関連付け、データの表示を行う
            val listView = findViewById<ListView>(R.id.character_list_battle_start_ally_battle_party_listView_id)

                mListAdapter = ListAdapter(this, characterList)

                listView.adapter = mListAdapter
                // 項目をタップしたときの処理
                listView.setOnItemClickListener { parent, view, position, id ->

                    val nameValue  = characterList[position].name // タップした場所のキャラクターの名前を取得
                    val intent = Intent(this, CharacterDetailActivity::class.java)
                    // 名前をCharacterDetailActivityに渡す
                    intent.putExtra("name_key", nameValue)
                    mp0.reset()
                    startActivity(intent)
        }

        // テキストの表示
        val textView = findViewById<TextView>(R.id.character_list_chara_list_print_text_id)
        textView.text = "キャラ一覧(".plus(characterList.size).plus("人)")

        // 戻るボタンを押したときの処理
        character_list_back_button_id.setOnClickListener {
            val intent = Intent(this, TopScreenActivity::class.java)
            mp0.reset()
            startActivity(intent)
        }

        // 新しく作成するボタンを押したときの処理
        character_list_new_create_button_id.setOnClickListener {

            //キャラクターリスとが8人以上でボタンが押されたらダイアログが表示される
            if (8 <= characterList.size){
                val dialog = CharacterCreateMaxDialogFragment()
                dialog.show(supportFragmentManager, "alert_dialog")

           }else {
                val intent = Intent(this, CharacterCreationActivity::class.java)
                mp0.reset()
                startActivity(intent)
            }
        }
    }

    // 数字を職業に変換
    private fun occupationConversion(jobValue: Int):String {

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
