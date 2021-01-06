package com.e.app_namebattler

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_battle_main.*
import kotlinx.android.synthetic.main.activity_battle_result_screen.*

class BattleResultScreenActivity : AppCompatActivity() {

    lateinit var memberList: MutableList<MemberStatusData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle_result_screen)

        val allyName01 = intent.getStringExtra("name_key01")
        val allyName02 = intent.getStringExtra("name_key02")
        val allyName03 = intent.getStringExtra("name_key03")
        val enemyName01 = intent.getStringExtra("name_key04")
        val enemyName02 = intent.getStringExtra("name_key05")
        val enemyName03 = intent.getStringExtra("name_key06")
        val party00 = intent.getIntExtra("party_key",0)

        val charaData = CharacterData.getInstance()

        val ally01 = charaData.ally01
        val ally02 = charaData.ally02
        val ally03 = charaData.ally03
        val enemy01 = charaData.enemy01
        val enemy02 = charaData.enemy02
        val enemy03 = charaData.enemy03

        if (ally01 != null) {
            if (ally02 != null) {
                if (ally03 != null) {
                    ResultAllyStatus(ally01, ally02, ally03)
                }
            }
        }

        if (enemy01 != null) {
            if (enemy02 != null) {
                if (enemy03 != null) {
                    ResultEnemyStatus(enemy01, enemy02, enemy03)
                }
            }
        }


        // 味方パーティメンバーが0の場合"you lose" を表示
        if (party00 == 0) {

            val imageView = findViewById<ImageView>(R.id.win_or_Loss_imageView_id)
            imageView.setImageResource(R.drawable.i_defeat)

            // 味方パーティメンバーが0でない場合　"you win" を表示
        }else{

            val imageView = findViewById<ImageView>(R.id.win_or_Loss_imageView_id)
            imageView.setImageResource(R.drawable.i_victory)

        }

        // 次の対戦ボタンを押したときの処理
        next_battle_button_id.setOnClickListener {

            val intent = Intent(this, BattleStartActivity::class.java)

            intent.putExtra("name_key01", allyName01)
            intent.putExtra("name_key02", allyName02)
            intent.putExtra("name_key03", allyName03)

            startActivity(intent)
        }

        // 再挑戦ボタンを押したときの処理
        challenge_again_button_id.setOnClickListener {

            val intent = Intent(this, BattleMainActivity::class.java)

            intent.putExtra("name_key01", allyName01)
            intent.putExtra("name_key02", allyName02)
            intent.putExtra("name_key03", allyName03)
            intent.putExtra("name_key01", enemyName01)
            intent.putExtra("name_key02", enemyName02)
            intent.putExtra("name_key03", enemyName03)

            startActivity(intent)
        }

        // 対戦を終了するボタンを押したときの処理
        end_battle_button_id.setOnClickListener {

            val intent = Intent(this, TopScreenActivity::class.java)
            startActivity(intent)
        }
    }

     fun ResultAllyStatus(ally01: Player, ally02: Player, ally03: Player) {

        val ally001 = MemberStatusData(ally01.getName(), ("%s %d/%d".format("HP", ally01.hp, ally01.getMaxHp())), ("%s %d/%d".format("MP", ally01.mp, ally01.getMaxMp())),("%s %s".format(ally01.getPoison(),ally01.getParalysis())))
        val ally002 = MemberStatusData(ally02.getName(), ("%s %d/%d".format("HP", ally02.hp, ally02.getMaxHp())), ("%s %d/%d".format("MP", ally02.mp, ally02.getMaxMp())),("%s %s".format(ally02.getPoison(),ally02.getParalysis())))
        val ally003 = MemberStatusData(ally03.getName(), ("%s %d/%d".format("HP", ally03.hp, ally03.getMaxHp())), ("%s %d/%d".format("MP", ally03.mp, ally03.getMaxMp())),("%s %s".format(ally03.getPoison(),ally03.getParalysis())))

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

     fun ResultEnemyStatus(enemy01: Player, enemy02: Player, enemy03: Player) {

        val enemy001 = MemberStatusData(enemy01.getName(), ("%s %d/%d".format("HP", enemy01.hp, enemy01.getMaxHp())), ("%s %d/%d".format("MP", enemy01.mp, enemy01.getMaxMp())),("%s %s".format(enemy01.getPoison(),enemy01.getParalysis())))
        val enemy002 = MemberStatusData(enemy02.getName(), ("%s %d/%d".format("HP", enemy02.hp, enemy02.getMaxHp())), ("%s %d/%d".format("MP", enemy02.mp, enemy02.getMaxMp())),("%s %s".format(enemy02.getPoison(),enemy02.getParalysis())))
        val enemy003 = MemberStatusData(enemy03.getName(), ("%s %d/%d".format("HP", enemy03.hp, enemy03.getMaxHp())), ("%s %d/%d".format("MP", enemy03.mp, enemy03.getMaxMp())),("%s %s".format(enemy03.getPoison(),enemy03.getParalysis())))

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
}

