package com.e.app_namebattler.view.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.e.app_namebattler.*
import com.e.app_namebattler.model.CharacterData
import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.view.adapter.BattleMainRecyclerAdapter
import com.e.app_namebattler.view.view.adapter.MemberStatusData
import com.e.app_namebattler.view.view.music.MusicData
import kotlinx.android.synthetic.main.activity_battle_main.*
import kotlinx.android.synthetic.main.activity_battle_result.*

class BattleResultActivity : AppCompatActivity() {

    lateinit var mp0: MediaPlayer
    private lateinit var memberList: MutableList<MemberStatusData>
    private var toast: Toast? = null

    // CharacterDataクラスのデータを呼び出す
    private val charaData = CharacterData.getInstance()
    private val ally01 = charaData.ally01
    private val ally02 = charaData.ally02
    private val ally03 = charaData.ally03
    private val enemy01 = charaData.enemy01
    private val enemy02 = charaData.enemy02
    private val enemy03 = charaData.enemy03

    // 名前を格納
    private val allyName01 = ally01?.getName()
    private val allyName02 = ally02?.getName()
    private val allyName03 = ally03?.getName()
    private val enemyName01 = enemy01?.getName()
    private val enemyName02 = enemy02?.getName()
    private val enemyName03 = enemy03?.getName()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle_result)

        mp0 = MediaPlayer.create(this, MusicData.BGM03.getBgm())
        mp0.isLooping = true
        mp0.start()

        // BattleMainActivityからデータを受け取る
        val allyPartySurvivalNumber = intent.getIntExtra("party_key", 0)

        if (ally01 != null) {
            if (ally02 != null) {
                if (ally03 != null) {
                    resultCharacterStatus(ally01, ally02, ally03)
                }
            }
        }

        if (enemy01 != null) {
            if (enemy02 != null) {
                if (enemy03 != null) {
                    resultCharacterStatus(enemy01, enemy02, enemy03)
                }
            }
        }

        // 味方パーティメンバーが0の場合"you lose" を表示
        if (allyPartySurvivalNumber == 0) {
            val imageView = findViewById<ImageView>(R.id.battle_result_win_or_Loss_imageView_id)
            imageView.setImageResource(R.drawable.i_defeat)

            // 味方パーティメンバーが0でない場合　"you win" を表示
        } else {
            val imageView = findViewById<ImageView>(R.id.battle_result_win_or_Loss_imageView_id)
            imageView.setImageResource(R.drawable.i_victory)
        }

        // 次の対戦ボタンを押したときの処理-BattleStartActivityに遷移
        battle_result_next_battle_button_id.setOnClickListener {

            val intent = Intent(this, BattleStartActivity::class.java)
            intent.putExtra("allyName01_key", allyName01)
            intent.putExtra("allyName02_key", allyName02)
            intent.putExtra("allyName03_key", allyName03)

            mp0.reset()
            startActivity(intent)
        }

        // 再挑戦ボタンを押したときの処理-BattleMainActivityに遷移
        battle_result_challenge_again_button_id.setOnClickListener {

            val intent = Intent(this, BattleMainActivity::class.java)
            intent.putExtra("allyName01_key", allyName01)
            intent.putExtra("allyName02_key", allyName02)
            intent.putExtra("allyName03_key", allyName03)
            intent.putExtra("enemyName01_key", enemyName01)
            intent.putExtra("enemyName02_key", enemyName02)
            intent.putExtra("enemyName03_key", enemyName03)
            mp0.reset()
            startActivity(intent)
        }

        // 対戦を終了するボタンを押したときの処理-TopScreenActivityに遷移
        battle_result_end_battle_button_id.setOnClickListener {

            val intent = Intent(this, TopScreenActivity::class.java)
            mp0.reset()
            startActivity(intent)
        }
    }

    private fun resultCharacterStatus(
        character01: Player,
        character02: Player,
        character03: Player
    ) {
        val  character001 = memberStatusData(character01)
        val  character002 = memberStatusData(character02)
        val  character003 = memberStatusData(character03)

        memberList = arrayListOf( character001,  character002,  character003)

        if (character01.isMark) {

            val layoutManager = LinearLayoutManager(
                this,
                RecyclerView.HORIZONTAL,
                false
            ).apply {
                battle_result_ally_status_recycleView_id.layoutManager = this
            }

            BattleMainRecyclerAdapter(memberList).apply {
                battle_result_ally_status_recycleView_id.adapter = this
            }

            battle_result_ally_status_recycleView_id.adapter = BattleMainRecyclerAdapter(memberList)
            (battle_result_ally_status_recycleView_id.adapter as BattleMainRecyclerAdapter).setOnItemClickListener(
                object : BattleMainRecyclerAdapter.OnItemClickListener {
                    override fun onItemClickListener(
                        viw: View,
                        position: Int
                    ) {
                        when (position) {
                            0 -> setImageType(character01)
                            1 -> setImageType(character02)
                            2 -> setImageType(character03)
                        }
                    }
                })

        }else{

            val layoutManager = LinearLayoutManager(
                this,
                RecyclerView.HORIZONTAL,
                false
            ).apply {
                battle_result_enemy_status_recycleView_id.layoutManager = this
            }

            BattleMainRecyclerAdapter(memberList).apply {
                battle_result_enemy_status_recycleView_id.adapter = this
            }

            battle_result_enemy_status_recycleView_id.adapter = BattleMainRecyclerAdapter(memberList)
            (battle_result_enemy_status_recycleView_id.adapter as BattleMainRecyclerAdapter).setOnItemClickListener(
                object : BattleMainRecyclerAdapter.OnItemClickListener {
                    override fun onItemClickListener(
                        viw: View,
                        position: Int
                    ) {
                        when (position) {
                            0 -> setImageType(character01)
                            1 -> setImageType(character02)
                            2 -> setImageType(character03)
                        }
                    }
                })
        }
    }

    private fun memberStatusData(character: Player): MemberStatusData {

        return MemberStatusData(
            ("  %s".format(character.getName())),
            ("%s %d/%d".format("  HP", character.hp, character.getMaxHp())),
            ("%s %d/%d".format("  MP", character.mp, character.getMaxMp())),
            ("%s %s".format(character.getPoison(), character.getParalysis())),
            (character.hp), character.getPrintStatusEffect())
    }

    // バトルメイン画面でステータスをタップでキャラクターのステータスを表示する
    @SuppressLint("ShowToast", "InflateParams")
    private fun setImageType(character: Player) {

        if (toast != null) {
            toast!!.cancel()
        }

        val layoutInflater = layoutInflater
        val customToastView: View = layoutInflater.inflate(R.layout.toast_layout_character_status, null)
        (customToastView.findViewById(R.id.toast_layout_imageView_id) as ImageView).setImageResource(character.getCharacterImageType())
        toast = Toast.makeText(customToastView.context, "", Toast.LENGTH_SHORT)
        toast!!.setGravity(Gravity.CENTER, 0, 0)
        (customToastView.findViewById(R.id.toast_layout_job_id) as TextView).text = "${character.job}"
        (customToastView.findViewById(R.id.toast_layout_str_id) as TextView).text = "${character.str}"
        (customToastView.findViewById(R.id.toast_layout_def_id) as TextView).text = "${character.def}"
        (customToastView.findViewById(R.id.toast_layout_agi_id) as TextView).text = "${character.agi}"
        (customToastView.findViewById(R.id.toast_layout_luck_id) as TextView).text = "${character.luck}"
        toast!!.setView(customToastView)
        toast!!.show()
    }

    override fun onDestroy() {
        mp0.release()
        super.onDestroy()
    }

//此処の処理は一旦保留：スリープ解除後効果音が出なくなるため
//    override fun onPause() {
//        mp0.pause()
//        super.onPause()
//    }
//
//    override fun onResume() {
//        mp0.start()
//        super.onResume()
//    }

    override fun onBackPressed() {}
}

