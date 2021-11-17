package br.senac.pi4pokemon.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.senac.pi4pokemon.R
import br.senac.pi4pokemon.databinding.ActivityPedidoConfirmadoBinding
import br.senac.pi4pokemon.databinding.CardPokemonsBinding
import br.senac.pi4pokemon.model.Carrinho
import br.senac.pi4pokemon.model.Pedidos
import br.senac.pi4pokemon.services.API
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PedidoConfirmadoActivity : AppCompatActivity() {
    lateinit var binding: ActivityPedidoConfirmadoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPedidoConfirmadoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        atualizarPokemons()
    }

    fun atualizarPokemons() {


        val callback = object : Callback<List<Pedidos>> {

            override fun onResponse(call: Call<List<Pedidos>>, response: Response<List<Pedidos>>) {

                if (response.isSuccessful) {

                    val listaProduto = response.body()

                    atualizarUI(listaProduto)


                } else {

                    Snackbar.make(binding.constraintLayoutPedidoConfirmado, "Não foi possivel carregar os pokemons",
                        Snackbar.LENGTH_LONG).show()

                    Log.e("ERROR", response.errorBody().toString())

                }

            }

            override fun onFailure(call: Call<List<Pedidos>>, t: Throwable) {

                Snackbar.make(binding.constraintLayoutPedidoConfirmado, "Não foi possivel conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao conectar ao serviço", t)

            }

        }
        API(this).pedido.listarPedidosUser().enqueue(callback)

    }

    fun atualizarUI(lista: List<Pedidos>?) {
        binding.constraintLayoutPedidoConfirmado.removeAllViews()
        lista?.forEach {

            val pokemonBinding = ActivityPedidoConfirmadoBinding.inflate(layoutInflater)
            pokemonBinding.textViewPedidoId.text = it.id.toString()


            binding.constraintLayoutPedidoConfirmado.addView(pokemonBinding.root)

        }

    }
}