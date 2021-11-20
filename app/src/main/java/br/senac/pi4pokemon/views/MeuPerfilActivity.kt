package br.senac.pi4pokemon.views

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import br.senac.pi4pokemon.R
import br.senac.pi4pokemon.databinding.ActivityMeuPerfilBinding
import br.senac.pi4pokemon.model.User
import br.senac.pi4pokemon.services.API
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MeuPerfilActivity : AppCompatActivity() {
    lateinit var binding: ActivityMeuPerfilBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMeuPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        atualizarPerfil()

    }

    fun atualizarPerfil() {


        val callback = object : Callback<User> {

            override fun onResponse(call: Call<User>, response: Response<User>) {

                if (response.isSuccessful) {

                    val listaProduto = response.body()

                    if (listaProduto != null) {
                        atualizarUI(listaProduto)
                    }


                } else {

                    Snackbar.make(binding.imageViewUsuario, "Não foi possivel carregar os pokemons",
                        Snackbar.LENGTH_LONG).show()

                    Log.e("ERROR", response.errorBody().toString())

                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {

                Snackbar.make(binding.imageViewUsuario, "Não foi possivel conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao conectar ao serviço", t)
            }

        }

        API(this).usuario.listarPerfil().enqueue(callback)


    }

    fun atualizarUI(user : User) {
        user.let {


            binding.textViewNomeUsuario.text = it.name
            binding.textViewEmail.text = it.email




            Picasso.get()
                .load("http://10.0.2.2:8000/${it.avatar}")
                .into(binding.imageViewUsuario)


        }

    }







}