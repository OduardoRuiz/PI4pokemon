package br.senac.pi4pokemon.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import br.senac.pi4pokemon.R
import br.senac.pi4pokemon.databinding.ActivityMeusPedidosBinding
import br.senac.pi4pokemon.databinding.CardPedidosBinding
import br.senac.pi4pokemon.databinding.CardPokemonsBinding
import br.senac.pi4pokemon.model.Pedidos
import br.senac.pi4pokemon.services.API
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MeusPedidosActivity : AppCompatActivity() {
    lateinit var binding: ActivityMeusPedidosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMeusPedidosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        atualizarPedidos()
    }


    fun atualizarPedidos() {


        val callback = object : Callback<List<Pedidos>> {

            override fun onResponse(call: Call<List<Pedidos>>, response: Response<List<Pedidos>>) {
                if (response.isSuccessful) {
                    val listaProduto = response.body()
                    progressBarOff()

                    atualizarUI(listaProduto)


                } else {

                    Snackbar.make(binding.containerMeusPedidos,
                        "Não foi possivel carregar os pokemons",
                        Snackbar.LENGTH_LONG).show()

                    Log.e("ERROR", response.errorBody().toString())

                }

            }

            override fun onFailure(call: Call<List<Pedidos>>, t: Throwable) {
                progressBarOff()
                Snackbar.make(binding.containerMeusPedidos, "Não foi possivel conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao conectar ao serviço", t)

            }


        }
        API(this).pedido.listarPedidosUser().enqueue(callback)
        progressBarOn()


    }

    fun atualizarUI(lista: List<Pedidos>?) {
        binding.containerMeusPedidos.removeAllViews()
        lista?.forEach {
            val pokemonBinding = CardPedidosBinding.inflate(layoutInflater)
            val idAqui = it.id
            pokemonBinding.textViewIdPedido.text = it.id.toString()
            pokemonBinding.textViewDataPedido.text = it.created_at
            pokemonBinding.textViewStatusPedido.text = it.status
            pokemonBinding.textViewLocalEntrega.text = it.endereco_id.toString()
            pokemonBinding.buttonVisualizarProdutos.setOnClickListener {
                val intent = Intent(this, ProdutosDoPedidoActivity::class.java)
                intent.putExtra("id",idAqui)
                startActivity(intent)


            }






            binding.containerMeusPedidos.addView(pokemonBinding.root)

        }

    }

    fun progressBarOff() {
        binding.progressBar.visibility = View.GONE
        binding.swipeRefreshTodos.isRefreshing = false
    }

    fun progressBarOn() {
        binding.progressBar.visibility = View.VISIBLE
        binding.swipeRefreshTodos.isRefreshing = true
    }
}