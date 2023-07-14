package com.example.notesapp.utils

import android.app.Activity
import android.app.AlertDialog
import com.example.notesapp.R

class LoadingDialog(val mActivity: Activity) {
    private lateinit var isdialog: AlertDialog
    fun startLoading(){
        val inflater = mActivity.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_layout,null)
        val builder = AlertDialog.Builder(mActivity)
        builder.setView(dialogView)
        builder.setCancelable(false)
        isdialog = builder.create()
        isdialog.show()
    }
    fun isDismiss(){
        isdialog.dismiss()
    }
}