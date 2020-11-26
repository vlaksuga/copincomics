package com.copincomics.copinapp.pages.dialogs

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast


class ExitDialog(val context: Context, val title: String, val body: String) {

    fun build(): AlertDialog.Builder {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(body)

        builder.setPositiveButton(
            "Yes"
        ) { dialog, which ->
            Toast.makeText(context.applicationContext, "Yes.", Toast.LENGTH_LONG).show()
        }

        builder.setNegativeButton(
            "No"
        ) { dialog, which ->
            Toast.makeText(context.applicationContext, "No.", Toast.LENGTH_LONG).show()
        }

        return builder
    }
}
