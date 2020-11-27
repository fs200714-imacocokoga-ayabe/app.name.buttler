package com.e.app_namebattler

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class BattleMainListAdapter (val context: Context, val memberList: ArrayList<CharacterAllData>) : BaseAdapter(){

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.data_battle_main_character_status, null)
        val Name = view.findViewById<TextView>(R.id.data_battle_main_character_status_id)

        val Hp = view.findViewById<TextView>(R.id.member_hp_id)
        val Mp = view.findViewById<TextView>(R.id.member_mp_id)
        val Status = view.findViewById<TextView>(R.id.member_status_id)

        val character = memberList[position]

        Name.text = "  ".plus(character.name)
        Hp.text = "   HP:".plus(character.hp.toString())
        Mp.text = "   MP:".plus(character.mp.toString())
        Status.text = "".plus(character.mp.toString())
        return view
    }

    override fun getItem(position: Int): Any {
        return memberList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return memberList.count()
    }
}