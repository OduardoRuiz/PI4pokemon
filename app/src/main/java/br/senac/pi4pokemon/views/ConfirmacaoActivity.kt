package br.senac.pi4pokemon.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import br.senac.pi4pokemon.R
import br.senac.pi4pokemon.databinding.ActivityConfirmacaoBinding
import br.senac.pi4pokemon.databinding.ActivityProductViewBinding
import br.senac.pi4pokemon.databinding.CardConfirmacaoBinding
import br.senac.pi4pokemon.databinding.CardPokemonsBinding
import br.senac.pi4pokemon.model.Carrinho
import br.senac.pi4pokemon.model.Endereco
import br.senac.pi4pokemon.model.Produto
import br.senac.pi4pokemon.services.API
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConfirmacaoActivity : AppCompatActivity() {
    lateinit var binding: ActivityConfirmacaoBinding
    var total: Double = 0.0
    var quants = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityConfirmacaoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.swipeRefreshTodos.setOnRefreshListener {
            atualizarPokemons()
            atualizarEndereco()

        }
        atualizarPokemons()
        atualizarEndereco()
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
            atualizarPokemons()
            val intent = Intent(this, PedidoConfirmadoActivity::class.java)
            startActivity(intent)
        }

        progressBarOn()





    }

    fun atualizarUI(lista: List<Carrinho>?) {
        binding.containerProdutosConfirmacao.removeAllViews()
        lista?.forEach {
            val idAquiFrag = it.id
            val pokemonBinding = CardConfirmacaoBinding.inflate(layoutInflater)
            pokemonBinding.nomePokemon.text = it.nome
           pokemonBinding.pontosPokemon.text = it.preco
            pokemonBinding.textViewQuantidadeConfirmacao.text = it.quantidade.toString()


                total   += it.preco.toDouble() * it.quantidade
                quants += it.quantidade

            Picasso.get()
                .load("http://10.0.2.2:8000/${it.imagem}")
                .into(pokemonBinding.imagemPokemon)
            binding.containerProdutosConfirmacao.addView(pokemonBinding.root)

        }
        // adiciona valor total
        binding.textViewValorTotal.text = total.toString()
        binding.textViewQuantidadeTotalConfirmacao.text = quants.toString()

        quants = 0
        total = 0.0
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
    fun atualizarEndereco() {


        val callback = object : Callback<List<Endereco>> {

            override fun onResponse(
                call: Call<List<Endereco>>,
                response: Response<List<Endereco>>
            ) {

                if (response.isSuccessful) {

                    val listaProduto2 = response.body()

                    atualizarUiEndereco(listaProduto2)


                } else {

                    Snackbar.make(binding.constraintLayoutConfirmacao,
                        "Não foi possivel carregar os pokemons",
                        Snackbar.LENGTH_LONG).show()

                    Log.e("ERROR", response.errorBody().toString())

                }

            }

            override fun onFailure(call: Call<List<Endereco>>, t: Throwable) {

                Snackbar.make(binding.constraintLayoutConfirmacao,
                    "Não foi possivel conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao conectar ao serviço", t)

            }

        }
        API(this).endereco.listaEndereco().enqueue(callback)

    }

    fun atualizarUiEndereco(lista: List<Endereco>?) {

        lista?.get(0)?.let {



            binding.textViewRuaConfirmar.text = it.rua
            binding.textViewNumeroConfirmacao2.text = it.numero.toString()
            binding.textViewComplementoConfirmacao2.text  = it.complemento
            binding.textViewCidadeConfirmar.text = it.cidade
            binding.textViewEstadoConfirmar.text = it.estado
            binding.textViewCepConfirmar.text = it.cep
            binding.textViewContato.text = it.contato.toString()
            if (binding.textViewComplementoConfirmacao2.text.isEmpty()){
                binding.textViewComplementoConfirmacao2.visibility = View.INVISIBLE
            }










        }
    }

}