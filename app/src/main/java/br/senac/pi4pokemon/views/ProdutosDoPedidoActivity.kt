package br.senac.pi4pokemon.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import br.senac.pi4pokemon.R
import br.senac.pi4pokemon.databinding.ActivityProdutosDoPedidoBinding
import br.senac.pi4pokemon.databinding.CardPokemonsBinding
import br.senac.pi4pokemon.model.PedidoItem
import br.senac.pi4pokemon.model.Produto
import br.senac.pi4pokemon.services.API
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProdutosDoPedidoActivity : AppCompatActivity() {
    lateinit var binding: ActivityProdutosDoPedidoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProdutosDoPedidoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MostraItensPedido()
    }


    fun MostraItensPedido() {






        val callback = object : Callback<List<PedidoItem>> {

            override fun onResponse(call: Call<List<PedidoItem>>, response: Response<List<PedidoItem>>) {
                progressBarOff()
                if (response.isSuccessful) {
                    val listaProduto = response.body()

                    atualizarUI(listaProduto)



                } else {

                    Snackbar.make(binding.containerProdutosDoPedido, "Não foi possivel carregar os pokemons",
                        Snackbar.LENGTH_LONG).show()

                    Log.e("ERROR", response.errorBody().toString())

                }

            }

            override fun onFailure(call: Call<List<PedidoItem>>, t: Throwable) {
                progressBarOff()
                Snackbar.make(binding.containerProdutosDoPedido, "Não foi possivel conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao conectar ao serviço", t)

            }


        }
        var idPedido = intent.getIntExtra("id", 0)
        API(this).pedidoItens.pesquisarItensDoPedido(idPedido).enqueue(callback)
        progressBarOn()


    }

    fun atualizarUI(lista: List<PedidoItem>?) {
        binding.containerProdutosDoPedido.removeAllViews()
        lista?.forEach {
            val pokemonBinding = CardPokemonsBinding.inflate(layoutInflater)
            val idAqui = it.id
            pokemonBinding.nomePokemon.text = it.nome
            pokemonBinding.pontosPokemon.text = it.preco
            pokemonBinding.linearLayoutCard.setOnClickListener {


                val intent = Intent(this, ProductViewActivity::class.java)
                intent.putExtra("id",idAqui)
                startActivity(intent)

            }

            Picasso.get()
                .load("http://10.0.2.2:8000/${it.imagem}").into(pokemonBinding.imagemPokemon)

            binding.containerProdutosDoPedido.addView(pokemonBinding.root)

        }

    }
    fun progressBarOff() {
        binding.progressBar.visibility =  View.GONE
        binding.swipeRefreshTodos.isRefreshing = false
    }
    fun progressBarOn() {
        binding.progressBar.visibility =  View.VISIBLE
        binding.swipeRefreshTodos.isRefreshing = true
    }


}