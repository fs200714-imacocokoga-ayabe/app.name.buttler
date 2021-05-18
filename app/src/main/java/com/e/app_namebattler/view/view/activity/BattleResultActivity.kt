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
import com.e.app_namebattler.view.party.player.CharacterData
import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.view.adapter.BattleMainRecyclerAdapter
import com.e.app_namebattler.view.view.adapter.MemberStatusData
import com.e.app_namebattler.view.view.music.MusicData
import kotlinx.android.synthetic.main.activity_battle_result.*

class BattleResultActivity : AppCompatActivity() {

    lateinit var mp0: MediaPlayer
    lateinit var memberList: MutableList<MemberStatusData>

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

        mp0= MediaPlayer.create(this, MusicData.BGM03.getBgm())
        mp0.isLooping=true
        mp0.start()

        // BattleMainActivityからデータを受け取る
        val allyPartyCount = intent.getIntExtra("party_key",0)

        if (ally01 != null) {
            if (ally02 != null) {
                if (ally03 != null) {
                    resultAllyStatus(ally01, ally02, ally03)
                }
            }
        }

        if (enemy01 != null) {
            if (enemy02 != null) {
                if (enemy03 != null) {
                    resultEnemyStatus(enemy01, enemy02, enemy03)
                }
            }
        }

        // 味方パーティメンバーが0の場合"you lose" を表示
        if (allyPartyCount == 0) {
            val imageView = findViewById<ImageView>(R.id.battle_result_win_or_Loss_imageView_id)
            imageView.setImageResource(R.drawable.i_defeat)

            // 味方パーティメンバーが0でない場合　"you win" を表示
        }else{
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

    // 味方キャラクターのステータス表示
     private fun resultAllyStatus(ally01: Player, ally02: Player, ally03: Player) {

        val ally001 = memberStatusData(ally01)
        val ally002 = memberStatusData(ally02)
        val ally003 = memberStatusData(ally03)

         memberList = arrayListOf(ally001, ally002, ally003)

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
                        0 -> setImageType(ally01)
                        1 -> setImageType(ally02)
                        2 -> setImageType(ally03)
                    }
                }
            })
    }

    // 敵キャラクターのステータス表示
     private fun resultEnemyStatus(enemy01: Player, enemy02: Player, enemy03: Player) {

        val enemy001 = memberStatusData(enemy01)
        val enemy002 = memberStatusData(enemy02)
        val enemy003 = memberStatusData(enemy03)

         memberList = arrayListOf(enemy001, enemy002, enemy003)

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
                @SuppressLint("ResourceAsColor")

                override fun onItemClickListener(
                    viw: View,
                    position: Int
                ) {
                    when (position) {
                        0 -> setImageType(enemy01)
                        1 -> setImageType(enemy02)
                        2 -> setImageType(enemy03)
                    }
                }
            })
    }

    private fun memberStatusData(character: Player): MemberStatusData {

        return  MemberStatusData(
            ("  %s".format(character.getName())),
            ("%s %d/%d".format("  HP", character.hp, character.getMaxHp())),
            ("%s %d/%d".format("  MP", character.mp, character.getMaxMp())),
            ("%s %s".format(character.getPoison(), character.getParalysis())),
            (character.hp), character.getPrintStatusEffect())
    }

    // バトルメイン画面でステータスをタップでキャラクターのステータスを表示する
    @SuppressLint("ShowToast", "InflateParams")
    private fun setImageType(character: Player) {

        val layoutInflater = layoutInflater
        val customToastView: View = layoutInflater.inflate(R.layout.toast_layout, null)

        (customToastView.findViewById(R.id.toast_layout_imageView_id) as ImageView).setImageResource(character.getCharacterImageType())
        val ts = Toast.makeText(customToastView.context, "", Toast.LENGTH_SHORT)
        ts.setGravity(Gravity.CENTER, 0, 0)
        (customToastView.findViewById(R.id.toast_layout_job_id) as TextView).text = "${character.job}"
        (customToastView.findViewById(R.id.toast_layout_str_id) as TextView).text = "${character.str}"
        (customToastView.findViewById(R.id.toast_layout_def_id) as TextView).text = "${character.def}"
        (customToastView.findViewById(R.id.toast_layout_agi_id) as TextView).text = "${character.agi}"
        (customToastView.findViewById(R.id.toast_layout_luck_id) as TextView).text = "${character.luck}"
        ts.setView(customToastView)
        ts.show()
    }

    override fun onDestroy() {
        mp0.release()
        super.onDestroy()
    }

    override fun onBackPressed() {}
}

