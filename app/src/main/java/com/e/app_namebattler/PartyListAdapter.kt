package com.e.app_namebattler

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class PartyListAdapter (val context: Context, val CharacterList: ArrayList<CharacterAllData>) : BaseAdapter() {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.data_party_organization_character_status, null)
        val Name = view.findViewById<TextView>(R.id.data_party_organization_character_name_radiobutton_id)
        val Job = view.findViewById<TextView>(R.id.data_party_organization_character_job_id)
        val Hp = view.findViewById<TextView>(R.id.data_party_organization_character_hp_id)
        val Mp = view.findViewById<TextView>(R.id.data_party_organization_character_mp_id)
        val Str = view.findViewById<TextView>(R.id.data_party_organization_character_str_id)
        val Def = view.findViewById<TextView>(R.id.data_party_organization_character_def_id)
        val Agi = view.findViewById<TextView>(R.id.data_party_organization_character_agi_id)

        val character = CharacterList[position]

        Name.text = "  ".plus(character.name)
        Job.text = "  ".plus(character.job)
        Hp.text = "   HP:".plus(character.hp.toString())
        Mp.text = "   MP:".plus(character.mp.toString())
        Str.text = "   STR".plus(character.str.toString())
        Def.text = "   DEF:".plus(character.def.toString())
        Agi.text = "   AGI:".plus(character.agi.toString())

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