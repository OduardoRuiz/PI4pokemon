package br.senac.pi4pokemon.views

import android.content.Context
import android.widget.Toast

fun mostrarToast(context: Context, msg: String){

    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()

}