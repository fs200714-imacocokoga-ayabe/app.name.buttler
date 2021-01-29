package com.e.app_namebattler

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class BattleMainRecyclerAdapter(private val memberList: MutableList<MemberStatusData>):
    RecyclerView.Adapter<BattleMainRecyclerAdapter.MyViewHolder>() {

    lateinit var listener: OnItemClickListener

    // Viewの設定
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val member = memberList[position]

        holder.Name.text = member.name
        holder.Hp.text = member.hp
        holder.Mp.text = member.Mp
        holder.Status.text = member.Status

        if (member.hp02 <= 0){

            holder.Name.setTextColor(Color.parseColor("#FF0000"))

        }else {

            holder.Name.setTextColor(Color.parseColor("#000000"))
        }

        holder.Status.setOnClickListener{
            listener.onItemClickListener(it, position, memberList[position])
        }
    }

    // レイアウトの設定
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.data_battle_main_character_status, viewGroup, false)
        return MyViewHolder(view)
    }

    // ViewHolderの定義
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val Name: TextView = view.findViewById(R.id.member_name_id)
        val Hp: TextView = view.findViewById(R.id.member_hp_id)
        val Mp: TextView = view.findViewById(R.id.member_mp_id)
        val Status: TextView = view.findViewById(R.id.member_status_id)
    }

    //  リストの行数
    override fun getItemCount(): Int {
        return memberList.size
    }

    interface OnItemClickListener {
        fun onItemClickListener(viw: View, position: Int, clickedText: MemberStatusData)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
}

// 1行分のデータモデル
class MemberStatusData(var name: String, var hp: String, var Mp: String, var Status: String ,var hp02:Int)




