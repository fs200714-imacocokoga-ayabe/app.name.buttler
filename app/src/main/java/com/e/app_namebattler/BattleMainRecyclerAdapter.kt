package com.e.app_namebattler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class BattleMainRecyclerAdapter(private val memberList: MutableList<MemberStatusData>):
    RecyclerView.Adapter<BattleMainRecyclerAdapter.MyViewHolder>() {

    // Viewの設定
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val member = memberList[position]
        holder.Name.text = member.name
        holder.Hp.text = member.hp.toString()
        holder.Mp.text = member.Mp.toString()
        holder.Status.text = member.Status
    }


    // レイアウトの設定
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.data_battle_main_character_status, viewGroup, false)
        return MyViewHolder(view)
    }

    // ViewHolderの定義
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val Name: TextView = view.findViewById(com.e.app_namebattler.R.id.member_name_id)
        val Hp: TextView = view.findViewById(com.e.app_namebattler.R.id.member_hp_id)
        val Mp: TextView = view.findViewById(com.e.app_namebattler.R.id.member_mp_id)
        val Status: TextView = view.findViewById(com.e.app_namebattler.R.id.member_status_id)

    }

    //  リストの行数
    // override fun getItemCount(): Int = memberList.size
    override fun getItemCount(): Int {
        // number of items in the data set held by the adapter
        return memberList.size
    }

}
// 1行分のデータモデル
class MemberStatusData(var name: String, var hp: String, var Mp: String, var Status: String)




