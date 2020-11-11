package com.e.app_namebattler

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment

open class CharacterDialogFragment: DialogFragment() {

    interface Listener{

        fun getUp()

    }

        private var listener: Listener? = null

        override fun onAttach(context: Context){
            super.onAttach(context)
            when (context){
                is Listener -> listener = context
            }
        }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())

        builder.setMessage("登録できるキャラクター数が最大です")
        builder.setPositiveButton("OK") {  dialog,which ->
         listener?.getUp()

        }

        return builder.create()

    }

}