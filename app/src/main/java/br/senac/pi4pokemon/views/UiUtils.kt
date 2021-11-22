package br.senac.pi4pokemon.views

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun mostrarToast(context: Context, msg: String){

    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()

}

fun mostrarSnackBar(view: View ,msg: String ){

    Snackbar.make(view,  msg, Snackbar.LENGTH_LONG).show()
}

fun mostrarDialogo(context: Context, msg: String){
    AlertDialog.Builder(context)
        .setMessage(msg)
        .setPositiveButton("OK", null)
        .create()
        .show()
}

