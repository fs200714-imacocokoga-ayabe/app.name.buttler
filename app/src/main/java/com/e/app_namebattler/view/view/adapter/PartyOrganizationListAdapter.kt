package com.e.app_namebattler.view.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Checkable
import android.widget.RadioButton
import android.widget.TextView
import com.e.app_namebattler.R
import com.e.app_namebattler.view.party.player.CharacterAllData

class PartyOrganizationListAdapter (val context: Context, val CharacterList: ArrayList<CharacterAllData>) : BaseAdapter(),
    Checkable {

    private var mRadioButton: RadioButton? = null

    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.data_party_organization_character_status, null)
        val Button = view.findViewById<RadioButton>(R.id.data_party_organization_character_status_radioButton_id)
        val Name = view.findViewById<TextView>(R.id.data_party_organization_character_status_name_id)
        val Job = view.findViewById<TextView>(R.id.data_party_organization_character_status_job_id)
        val Hp = view.findViewById<TextView>(R.id.data_party_organization_character_status_hp_id)
        val Mp = view.findViewById<TextView>(R.id.data_party_organization_character_status_mp_id)
        val Str = view.findViewById<TextView>(R.id.data_party_organization_character_status_str_id)
        val Def = view.findViewById<TextView>(R.id.data_party_organization_character_status_def_id)
        val Agi = view.findViewById<TextView>(R.id.data_party_organization_character_status_agi_id)

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

    override fun isChecked(): Boolean {
        return mRadioButton!!.isChecked
    }

    override fun setChecked(checked: Boolean) {
        // RadioButton の表示を切り替える
        if (mRadioButton != null) {
            mRadioButton!!.isChecked = checked
        }
    }

    override fun toggle() {}
}

