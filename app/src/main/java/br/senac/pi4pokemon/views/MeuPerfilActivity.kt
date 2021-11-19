package br.senac.pi4pokemon.views

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.senac.pi4pokemon.R
import br.senac.pi4pokemon.databinding.ActivityMeuPerfilBinding
import br.senac.pi4pokemon.databinding.ActivityMeusPedidosBinding
import br.senac.pi4pokemon.model.Produto
import br.senac.pi4pokemon.model.User
import br.senac.pi4pokemon.services.API
import br.senac.pi4pokemon.services.ARQUIVO_LOGIN
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
        atualizarPokemons()

    }
    fun atualizarPokemons() {


        val callback = object : Callback<List<User>> {

            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {

                if (response.isSuccessful) {

                    val listaProduto = response.body()

                    atualizarUI(listaProduto)


                } else {

                    Snackbar.make(binding.imageViewUsuario, "Não foi possivel carregar os pokemons",
                        Snackbar.LENGTH_LONG).show()

                    Log.e("ERROR", response.errorBody().toString())

                }
            }
            override fun onFailure(call: Call<List<User>>, t: Throwable) {

                Snackbar.make(binding.imageViewUsuario, "Não foi possivel conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao conectar ao serviço", t)
            }

        }

        API(this).usuario.listarPerfil().enqueue(callback)



    }

    fun atualizarUI(lista: List<User>?) {
        lista?.get(0)?.let {


            binding.textViewNomeUsuario.text = it.name
           binding.textViewEmail.text = it.email




            Picasso.get()
                .load("http://10.0.2.2:8000/${it.avatar}")
                .into(binding.imageViewUsuario)


        }

    }


}