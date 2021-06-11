package com.e.app_namebattler.view.view.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.e.app_namebattler.view.view.message.Comment

open class CharacterCreateMaxDialogFragment : DialogFragment() {

    interface Listener {
        fun maxDisplay()
    }

    private var listener: Listener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        when (context) {
            is Listener -> listener = context
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())

        builder.setMessage(Comment.M_MAX_REGISTER_CHARACTER_COMMENT.getComment())
        
        builder.setPositiveButton("OK") { dialog, which ->
            listener?.maxDisplay()
        }
        return builder.create()
    }
}