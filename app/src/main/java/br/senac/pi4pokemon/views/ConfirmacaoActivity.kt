package br.senac.pi4pokemon.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import br.senac.pi4pokemon.R
import br.senac.pi4pokemon.databinding.ActivityConfirmacaoBinding
import br.senac.pi4pokemon.databinding.ActivityProductViewBinding
import br.senac.pi4pokemon.databinding.CardPokemonsBinding
import br.senac.pi4pokemon.model.Carrinho
import br.senac.pi4pokemon.model.Produto
import br.senac.pi4pokemon.services.API
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConfirmacaoActivity : AppCompatActivity() {
    lateinit var binding: ActivityConfirmacaoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityConfirmacaoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.swipeRefreshTodos.setOnRefreshListener {
            atualizarPokemons()

        }
        atualizarPokemons()
    }

    fun atualizarPokemons() {


        val callback = object : Callback<List<Carrinho>> {

            override fun onResponse(call: Call<List<Carrinho>>, response: Response<List<Carrinho>>) {

                if (response.isSuccessful) {

                    val listaProduto = response.body()
                    progressBarOff()
                    atualizarUI(listaProduto)


                } else {

                    Snackbar.make(binding.scrollView2, "Não foi possivel carregar os pokemons",
                        Snackbar.LENGTH_LONG).show()

                    Log.e("ERROR", response.errorBody().toString())

                }

            }

            override fun onFailure(call: Call<List<Carrinho>>, t: Throwable) {
                progressBarOff()
                Snackbar.make(binding.scrollView2, "Não foi possivel conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao conectar ao serviço", t)

            }


        }


        API(this).carrinho.mostraCarrinho().enqueue(callback)

        binding.buttonFinalizarPedido.setOnClickListener {
            finalizarPedidido()
        }

        progressBarOn()



    }

    fun atualizarUI(lista: List<Carrinho>?) {
        binding.containerProdutosConfirmacao.removeAllViews()
        lista?.forEach {
            val idAquiFrag = it.id
            val pokemonBinding = CardPokemonsBinding.inflate(layoutInflater)
            pokemonBinding.nomePokemon.text = it.nome
           pokemonBinding.pontosPokemon.text = it.preco







            Picasso.get()
                .load("http://10.0.2.2:8000/${it.imagem}")
                .into(pokemonBinding.imagemPokemon)
            binding.containerProdutosConfirmacao.addView(pokemonBinding.root)

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

    fun finalizarPedidido() {

        val callbackAdd = object : Callback<Void> {

            override fun onResponse(call: Call<Void>, response: Response<Void>) {

                if (response.isSuccessful) {
                    progressBarOff()
                    // val listaProduto = response.body()

                    //   atualizarUI(listaProduto)


                } else {

                    Snackbar.make(binding.scrollView2, "Não foi possivel carregar os pokemons",
                        Snackbar.LENGTH_LONG).show()

                    Log.e("ERROR", response.errorBody().toString())

                }

            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                progressBarOff()
                Snackbar.make(binding.progressBar, "Não foi possivel conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao conectar ao serviço", t)

            }


        }


        API(this).carrinho.FinalizaPedido().enqueue(callbackAdd)
        progressBarOn()


    }

}