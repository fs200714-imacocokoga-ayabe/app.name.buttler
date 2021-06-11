package com.e.app_namebattler.view.view.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.e.app_namebattler.view.view.message.Comment

class PartySelectMaxDialogFragment : DialogFragment() {

    interface Listener {
        fun maxParty()
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

        builder.setMessage(Comment.M_PARTY_MEMBER_NUMBER_COMMENT.getComment())

        builder.setPositiveButton("OK") { dialog, which ->
            listener?.maxParty()
        }

        return builder.create()
    }
}