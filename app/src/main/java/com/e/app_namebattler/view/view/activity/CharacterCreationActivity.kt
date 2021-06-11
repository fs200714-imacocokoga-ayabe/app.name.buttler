package com.e.app_namebattler.view.view.activity


import android.annotation.SuppressLint
import android.content.Intent
import android.database.DatabaseUtils
import android.media.MediaPlayer
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.e.app_namebattler.R
import com.e.app_namebattler.model.AllyOpenHelper
import com.e.app_namebattler.view.party.job.JobData
import com.e.app_namebattler.view.view.fragment.CharacterCreateMaxDialogFragment
import com.e.app_namebattler.view.view.message.Comment
import com.e.app_namebattler.view.view.music.MusicData
import kotlinx.android.synthetic.main.activity_character_creation.*


class CharacterCreationActivity : AppCompatActivity(), TextWatcher {

    lateinit var mp0: MediaPlayer
    lateinit var helper: AllyOpenHelper
    private var isSameName: Boolean = false // 同じ名前かどうか true:同じ名前 false:違う名前
    private var inputStr: String = "" // editTextに入力された文字を格納
    private var limitLength = StringBuilder()
    private var characterCount: Int = 0 // データベースのキャラクター数のチェック用
    private val maxCharacterNumber = 8 // 登録できるキャラクター数
    private val nameLength = 10 //キャラクターの名前の文字数
    private var toast: Toast? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_creation)

        mp0 = MediaPlayer.create(this, MusicData.BGM04.getBgm())
        mp0.isLooping = true
        mp0.start()

        val job01TextView: TextView =
            findViewById(R.id.character_creation_occupation_selection_radioButton01_id)
        job01TextView.text = JobData.FIGHTER.getJobName()

        val job02TextView: TextView =
            findViewById(R.id.character_creation_occupation_selection_radioButton02_id)
        job02TextView.text = JobData.WIZARD.getJobName()

        val job03TextView: TextView =
            findViewById(R.id.character_creation_occupation_selection_radioButton03_id)
        job03TextView.text = JobData.PRIEST.getJobName()

        val job04TextView: TextView =
            findViewById(R.id.character_creation_occupation_selection_radioButton04_id)
        job04TextView.text = JobData.NINJA.getJobName()

        // textWatcherのリスナーを登録　
        val createNameValues =
            findViewById<EditText>(R.id.character_creation_name_input_field_editText_id)
        createNameValues.addTextChangedListener(this)

        // 登録してあるキャラクターの数と登録できる数を表示する
        printPartyNumber()

        // 戻るボタンを押した時の処理
        character_creation_create_character_back_button_id.setOnClickListener {
            val intent = Intent(this, CharacterListActivity::class.java)
            mp0.reset()
            toast!!.cancel()
            startActivity(intent)
        }

        // 作成するボタンを押したときの処理
        character_creation_create_character_button_id.setOnClickListener {

            if (maxCharacterNumber <= characterNumberCheck()) {
                val dialog = CharacterCreateMaxDialogFragment()
                dialog.show(supportFragmentManager, "alert_dialog")
            } else {
                //職業を取得
                val radioGroupJob: RadioGroup =
                    findViewById(R.id.character_creation_character_select_radioGroup_id)
                val radioId = radioGroupJob.checkedRadioButtonId
                val createJobValues: RadioButton = radioGroupJob.findViewById(radioId)
                //表示させる形式に変数を変換
                val nameValue: String = createNameValues.text.toString()
                // データベースの中に同じ名前が存在するかのチェック
                if (sameNameCheck(nameValue)) {

                    attentionMessage(Comment.M_SAME_NAME_COMMENT.getComment())

                } else {

                    val jobValue: String = createJobValues.text.toString()
                    // 入力した名前と選択した職業を渡す
                    val intent = Intent(this, CharacterCreatedActivity::class.java)
                    // 名前と職業をCharacterCreateActivityに渡す
                    intent.putExtra("name_key", nameValue)
                    intent.putExtra("job_key", jobValue)
                    mp0.reset()
                    toast!!.cancel()
                    startActivity(intent)
                }
            }
        }
    }

    private fun printPartyNumber() {

        val currentCharacterNumber = characterNumberCheck()
        val attentionMessage01 =
            "現在${currentCharacterNumber}人です、あと${maxCharacterNumber - currentCharacterNumber}人作成出来ます。"
        attentionMessage(attentionMessage01)
    }

    // ---textWatcher---使用していませんが参考に残しています-------------------------------------------------------------------------------
    // 文字列が修正される直前に呼び出される
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    // 文字１つを入力した時に呼び出される
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    // 最後にこのメソッドが呼び出される
    @SuppressLint("ResourceAsColor")
    override fun afterTextChanged(s: Editable?) {

        inputStr = s.toString()

        if (nameLength < inputStr.length) {

            limitLength.append(inputStr)
            limitLength.deleteCharAt(10)// 11文字目を消去

            val ic = Toast.makeText(this,
                Comment.M_NAME_NUMBER_COMMENT.getComment(),
                Toast.LENGTH_SHORT)
            ic.setGravity(Gravity.TOP, 0, 0)
            ic.show()

            val editText =
                findViewById<View>(R.id.character_creation_name_input_field_editText_id) as EditText
            editText.setText(limitLength)

            limitLength.clear()
        }
    }
//--------------------------------------------------------------------------------------------------------------------------------

    // 同じ名前がデータベースに存在しているかのチェック
    private fun sameNameCheck(nameValue: String?): Boolean {

        helper = AllyOpenHelper(applicationContext)//DB作成

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

    private fun characterNumberCheck(): Int {

        helper = AllyOpenHelper(applicationContext)//DB作成

        val db = helper.readableDatabase

        try {
            // データベースのキャラクター数を取得する
            val db = helper.readableDatabase
            characterCount = DatabaseUtils.queryNumEntries(db, "CHARACTER").toInt()

        } finally {
            // dbを開いたら確実にclose
            db.close()
        }
        return characterCount
    }

    fun onClickCharacter01(view: View) {
        jobMemo(JobData.FIGHTER.getJobMemo())
    }

    fun onClickCharacter02(view: View) {
        jobMemo(JobData.WIZARD.getJobMemo())
    }

    fun onClickCharacter03(view: View) {
        jobMemo(JobData.PRIEST.getJobMemo())
    }

    fun onClickCharacter04(view: View) {
        jobMemo(JobData.NINJA.getJobMemo())
    }

    private fun attentionMessage(message: String) {
        if (toast != null) {
            toast!!.cancel()
        }
        val layoutInflater = layoutInflater
        val customToastView: View = layoutInflater.inflate(R.layout.toast_layout_message, null)
        toast = Toast.makeText(customToastView.context, "", Toast.LENGTH_LONG)
        toast!!.setGravity(Gravity.TOP, 0, 200)
        (customToastView.findViewById(R.id.toast_layout_message_id) as TextView).text = message
        toast!!.setView(customToastView)
        toast!!.show()
    }

    private fun jobMemo(message: String) {
        if (toast != null) {
            toast!!.cancel()
        }
        val layoutInflater = layoutInflater
        val customToastView: View =
            layoutInflater.inflate(R.layout.toast_layout_strategy_memo, null)
        toast = Toast.makeText(customToastView.context, "", Toast.LENGTH_LONG)
        toast!!.setGravity(Gravity.BOTTOM, 0, 300)
        (customToastView.findViewById(R.id.toast_layout_strategy_comment_message_id) as TextView).text =
            message
        toast!!.setView(customToastView)
        toast!!.show()
    }

    override fun onDestroy() {
        mp0.release()
        super.onDestroy()
    }

    override fun onBackPressed() {}

}
