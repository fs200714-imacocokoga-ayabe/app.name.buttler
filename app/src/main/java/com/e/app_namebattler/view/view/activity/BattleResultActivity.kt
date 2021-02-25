package com.e.app_namebattler.view.view.activity

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.e.app_namebattler.*
import com.e.app_namebattler.view.party.player.CharacterData
import com.e.app_namebattler.view.party.player.Player
import com.e.app_namebattler.view.view.adapter.BattleMainRecyclerAdapter
import com.e.app_namebattler.view.view.adapter.MemberStatusData
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

        mp0= MediaPlayer.create(this, R.raw.newworld)
        mp0.isLooping=true
        mp0.start()

        // BattleMainActivityからデータを受け取る
        val party00 = intent.getIntExtra("party_key",0)

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
        if (party00 == 0) {
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
            intent.putExtra("name_key01", allyName01)
            intent.putExtra("name_key02", allyName02)
            intent.putExtra("name_key03", allyName03)

            mp0.reset()
            startActivity(intent)
        }

        // 再挑戦ボタンを押したときの処理-BattleMainActivityに遷移
        battle_result_challenge_again_button_id.setOnClickListener {

            val intent = Intent(this, BattleMainActivity::class.java)
            intent.putExtra("name01_key", allyName01)
            intent.putExtra("name02_key", allyName02)
            intent.putExtra("name03_key", allyName03)
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

         val ally001 = MemberStatusData(("  %s".format(ally01.getName())), ("%s %d/%d".format("  HP", ally01.hp, ally01.getMaxHp())), ("%s %d/%d".format("  MP", ally01.mp, ally01.getMaxMp())),("%s %s".format(ally01.getPoison(),ally01.getParalysis())),(ally01.hp))
         val ally002 = MemberStatusData(("  %s".format(ally02.getName())), ("%s %d/%d".format("  HP", ally02.hp, ally02.getMaxHp())), ("%s %d/%d".format("  MP", ally02.mp, ally02.getMaxMp())),("%s %s".format(ally02.getPoison(),ally02.getParalysis())),(ally02.hp))
         val ally003 = MemberStatusData(("  %s".format(ally03.getName())), ("%s %d/%d".format("  HP", ally03.hp, ally03.getMaxHp())), ("%s %d/%d".format("  MP", ally03.mp, ally03.getMaxMp())),("%s %s".format(ally03.getPoison(),ally03.getParalysis())),(ally03.hp))

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
    }

    // 敵キャラクターのステータス表示
     private fun resultEnemyStatus(enemy01: Player, enemy02: Player, enemy03: Player) {

         val enemy001 = MemberStatusData(("  %s".format(enemy01.getName())), ("%s %d/%d".format("  HP", enemy01.hp, enemy01.getMaxHp())), ("%s %d/%d".format("  MP", enemy01.mp, enemy01.getMaxMp())),("%s %s".format(enemy01.getPoison(),enemy01.getParalysis())),(enemy01.hp))
         val enemy002 = MemberStatusData(("  %s".format(enemy02.getName())), ("%s %d/%d".format("  HP", enemy02.hp, enemy02.getMaxHp())), ("%s %d/%d".format("  MP", enemy02.mp, enemy02.getMaxMp())),("%s %s".format(enemy02.getPoison(),enemy02.getParalysis())),(enemy02.hp))
         val enemy003 = MemberStatusData(("  %s".format(enemy03.getName())), ("%s %d/%d".format("  HP", enemy03.hp, enemy03.getMaxHp())), ("%s %d/%d".format("  MP", enemy03.mp, enemy03.getMaxMp())),("%s %s".format(enemy03.getPoison(),enemy03.getParalysis())),(enemy03.hp))

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
    }

    override fun onDestroy() {
        mp0.release()
        super.onDestroy()
    }

    override fun onBackPressed() {}
}

