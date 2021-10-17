package com.rjesture.kotbox

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.widget.Toast

/**
 * Created by Rjesture on 10/15/2021.
 */

     fun setLog(title: String, message: String) {
        Log.v(title, "message :-  " + message)
    }

//*****************************Back To Exit****************************************
fun showAlertDialog(
    context: Context?, title: String?, message: String?, positiveLabel: String?,
    positiveClick: DialogInterface.OnClickListener?, negativeLabel: String?,
    negativeClick: DialogInterface.OnClickListener?, isCancelable: Boolean
): AlertDialog? {
    try {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle(title)
        dialogBuilder.setCancelable(isCancelable)
        dialogBuilder.setMessage(message)
        dialogBuilder.setPositiveButton(positiveLabel, positiveClick)
        dialogBuilder.setNegativeButton(negativeLabel, negativeClick)
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
        return alertDialog
    } catch (e: Exception) {
        handleCatch(e)
    }
    return null
}

//*****************************Handle Catch****************************************
fun handleCatch(e: java.lang.Exception) {
    e.printStackTrace()
}

fun handleCatch(e: java.lang.Exception, context: Context?) {
    e.printStackTrace()
    showToast(
        context,
        "Something went wrong"
    )
}

fun handleCatch(e: java.lang.Exception, context: Context?, errorMessage: String?) {
    e.printStackTrace()
    showToast(context, errorMessage)
}
fun showToast(context: Context?, text: String?) {
    try {
        Toast.makeText(context, text, Toast.LENGTH_LONG)
    } catch (e: java.lang.Exception) {
        handleCatch(e)
    }
}


