package com.copincomics.copinapp.pages.dialogs

import android.R
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface


class WarnDiag {

    fun build(context: Context, msg: String): AlertDialog.Builder {
        val diag = AlertDialog.Builder(context)
            .setMessage(msg)
            .setPositiveButton("Confirm", DialogInterface.OnClickListener { dialog, which ->
            })
            .setIcon(R.drawable.ic_dialog_alert)
//            .show()
        return diag
    }
}