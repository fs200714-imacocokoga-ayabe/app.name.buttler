package com.e.app_namebattler.view.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.e.app_namebattler.R
import com.e.app_namebattler.view.party.player.CharacterAllData


class CharacterListAdapter(val context: Context, private val CharacterList: ArrayList<CharacterAllData>) :
    BaseAdapter() {

    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.data_character_status, null)
        val Name = view.findViewById<TextView>(R.id.data_character_status_data_name_text_id)
        val Job = view.findViewById<TextView>(R.id.data_character_status_data_job_text_id)
        val Hp = view.findViewById<TextView>(R.id.data_character_status_data_hp_text_id)
        val Mp = view.findViewById<TextView>(R.id.data_character_status_data_mp_text_id)
        val Str = view.findViewById<TextView>(R.id.data_character_status_data_str_text_id)
        val Def = view.findViewById<TextView>(R.id.data_character_status_data_def_text_id)
        val Agi = view.findViewById<TextView>(R.id.data_character_status_data_agi_text_id)

        val character = CharacterList[position]

        Name.text = "  ".plus(character.name)
        Job.text = "  ".plus(character.job)
        Hp.text = "   HP:".plus(character.hp.toString())
        Mp.text = "   MP:".plus(character.mp.toString())
        Str.text = "   STR".plus(character.str.toString())
        Def.text = "   DEF:".plus(character.def.toString())
        Agi.text = "   AGI:".plus(character.agi.toString())

        //val params = LinearLayout.LayoutParams(
        //ViewGroup.LayoutParams(100, 100)
        val params = LinearLayout.LayoutParams(character.size2X - 12, 200)
        // marginの設定　(left, top, right, bottom
        // marginの設定　(left, top, right, bottom
        // params.setMargins(50, 0, 0, 0)
        view.layoutParams = params

        return view
    }

    override fun getItem(position: Int): Any {
        return CharacterList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return CharacterList.count()
    }
}
