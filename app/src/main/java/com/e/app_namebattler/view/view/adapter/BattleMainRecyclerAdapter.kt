package com.e.app_namebattler.view.view.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.e.app_namebattler.R


class BattleMainRecyclerAdapter(private val memberList: MutableList<MemberStatusData>):
    RecyclerView.Adapter<BattleMainRecyclerAdapter.MyViewHolder>() {

    lateinit var listener: OnItemClickListener

    // Viewの設定
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val member = memberList[position]

        holder.Name.text = member.name
        holder.Hp.text = member.hp
        holder.Mp.text = member.Mp
        holder.Status.text = member.Status

        if (member.hp02 <= 0){
            // HPが0以下の場合名前を赤色に表示
            holder.Name.setTextColor(Color.parseColor("#FF0000"))

        }else {
            // HPが0以上の場合名前を黒色に表示
            holder.Name.setTextColor(Color.parseColor("#000000"))
        }

        // ダメージを受けた時
        if (member.printEffect == 1){
   //        holder.aa.setBackgroundColor(R.drawable.frame_line_05_damage) 色が変
            holder.Name.setBackgroundColor(Color.parseColor("#f4b3c2"))
            holder.Hp.setBackgroundColor(Color.parseColor("#f4b3c2"))
            holder.Mp.setBackgroundColor(Color.parseColor("#f4b3c2"))
            holder.Status.setBackgroundColor(Color.parseColor("#f4b3c2"))
        }

        // 回復した時
        if (member.printEffect == 2){
   //        holder.aa.setBackgroundColor(R.drawable.frame_line_06_healing)　色が変
            holder.Name.setBackgroundColor(Color.parseColor("#00ff7f"))//#00ff7f
            holder.Hp.setBackgroundColor(Color.parseColor("#00ff7f"))
            holder.Mp.setBackgroundColor(Color.parseColor("#00ff7f"))
            holder.Status.setBackgroundColor(Color.parseColor("#00ff7f"))
        }

        if (member.printEffect == 3){
            holder.aa.setBackgroundResource(R.drawable.i_attack_miss)
        }

        // 名前のエリアをタップしたとき
        holder.Name.setOnClickListener{
            listener.onItemClickListener(it, position)
        }

        holder.Hp.setOnClickListener{
            listener.onItemClickListener(it, position)
        }

        holder.Mp.setOnClickListener{
            listener.onItemClickListener(it, position)
        }

        holder.Status.setOnClickListener{
            listener.onItemClickListener(it, position)
        }
    }

    // レイアウトの設定
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.data_battle_main_character_status, viewGroup, false)

        //val params = LinearLayout.LayoutParams(
        //ViewGroup.LayoutParams(100, 100)
     //   val params = LinearLayout.LayoutParams(340, 200)
        // marginの設定　(left, top, right, bottom
        // marginの設定　(left, top, right, bottom
        // params.setMargins(0, 0, 0, 0)
     //   view.layoutParams = params

        return MyViewHolder(view)
    }

    // ViewHolderの定義
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val Name: TextView = view.findViewById(R.id.data_battle_main_character_status_member_name_text_id)
        val Hp: TextView = view.findViewById(R.id.data_battle_main_character_status_member_hp_text_id)
        val Mp: TextView = view.findViewById(R.id.data_battle_main_character_status_member_mp_text_id)
        val Status: TextView = view.findViewById(R.id.data_battle_main_character_status_member_status_text_id)
        val aa:LinearLayout = view.findViewById(R.id.data_battle_main_character_status_linearLayout_id) //試し

    }

    //  リストの行数
    override fun getItemCount(): Int {
        return memberList.size
    }

    interface OnItemClickListener {

        fun onItemClickListener(viw: View, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
}
// 1行分のデータモデル
class MemberStatusData(var name: String, var hp: String, var Mp: String, var Status: String ,var hp02:Int, var printEffect: Int)





