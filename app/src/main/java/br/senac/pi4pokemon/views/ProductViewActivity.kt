package br.senac.pi4pokemon.views


import android.content.Context
import android.content.Intent
import android.media.session.MediaSession
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import br.senac.pi4pokemon.R
import br.senac.pi4pokemon.databinding.ActivityProductViewBinding
import br.senac.pi4pokemon.databinding.CardPokemonsBinding
import br.senac.pi4pokemon.model.Categoria

import br.senac.pi4pokemon.model.Produto
import br.senac.pi4pokemon.model.Token
import br.senac.pi4pokemon.services.API
import br.senac.pi4pokemon.services.ARQUIVO_LOGIN
import br.senac.pi4pokemon.services.AutenticadorToken
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import okhttp3.Request
import okhttp3.internal.http2.Header
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductViewActivity : AppCompatActivity() {

    lateinit var binding: ActivityProductViewBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        atualizarPokemons()





    }


    fun atualizarPokemons() {


        val callback = object : Callback<Produto> {

            override fun onResponse(call: Call<Produto>, response: Response<Produto>) {

                if (response.isSuccessful) {
                    progressBarOff()
                    val listaProduto = response.body()

                    if (listaProduto != null) {
                        atualizarUI(listaProduto)
                    }

                } else {

                    Snackbar.make(binding.textView5, "Não foi possivel carregar os pokemons",
                        Snackbar.LENGTH_LONG).show()

                    Log.e("ERROR", response.errorBody().toString())

                }
            }
            override fun onFailure(call: Call<Produto>, t: Throwable) {
                progressBarOff()
                Snackbar.make(binding.textView5, "Não foi possivel conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao conectar ao serviço", t)
            }

        }
        //id default como 2 para a product view activity nao ficar em branco ao ser rodada direto
        var idPokemon = intent.getIntExtra("id", 2)
        API(this).pokemonAberto.pesquisarProdutos(idPokemon).enqueue(callback)
        val prefs = this.getSharedPreferences(ARQUIVO_LOGIN, Context.MODE_PRIVATE)
        val token = prefs.getString("token", "") as String

        binding.buttonAddCarrinho.setOnClickListener {
            addPokemon(idPokemon)
            if (token == "") {
                mostrarToast(this, "Faça o login antes de adicionar ao carrinho")
            }else {
                mostrarToast(this, "Produto adicionado ao carrinho")
            }
        }
        binding.buttonComprarAgora.setOnClickListener {
            addPokemon(idPokemon)
            if (token == "") {
                mostrarToast(this, "Faça o login antes de adicionar ao carrinho")
            }else {
                mostrarToast(this, "Produto adicionado ao carrinho")
            }
        }

        progressBarOn()

    }

    fun atualizarUI(produto: Produto) {
        produto.let {
            val idAquiFrag = it.id

            binding.nomePokemonProductView.text = it.nome
            binding.textPontosPokemonProductView.text = it.preco
            binding.textDescricaoProdutoView.text = it.descricao

         val  idCategoria = it.categoria_id
            atualizarVejaMais(idCategoria)




            Picasso.get()
                .load("http://10.0.2.2:8000/${it.imagem}")
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(binding.imagePokemonProductView)


        }

    }
    fun progressBarOff() {
        binding.progressBarProd.visibility = View.GONE
        binding.imagePokemonProductView.visibility = View.VISIBLE

    }

    fun atualizarVejaMais(idCategoria: Int) {






        val callback = object : Callback<List<Produto>> {

            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {
                progressBarOff()
                if (response.isSuccessful) {
                    val listaProduto = response.body()

                    atualizarVejaMais(listaProduto)



                } else {

                    Snackbar.make(binding.imageView11, "Não foi possivel carregar os pokemons",
                        Snackbar.LENGTH_LONG).show()

                    Log.e("ERROR", response.errorBody().toString())

                }

            }

            override fun onFailure(call: Call<List<Produto>>, t: Throwable) {
                progressBarOff()
                Snackbar.make(binding.imageView11, "Não foi possivel conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao conectar ao serviço", t)

            }


        }
        API(this).categoria.obterCategoriaId(idCategoria).enqueue(callback)
        progressBarOn()


    }

    fun atualizarVejaMais(lista: List<Produto>?) {
        binding.gridLayoutVejaMais.removeAllViews()
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
                .load("http://10.0.2.2:8000/${it.imagem}").memoryPolicy(MemoryPolicy.NO_CACHE).into(pokemonBinding.imagemPokemon)

            binding.gridLayoutVejaMais.addView(pokemonBinding.root)

        }

    }


    fun progressBarOn() {
        binding.progressBarProd.visibility = View.VISIBLE
        binding.imagePokemonProductView.visibility = View.INVISIBLE
    }

    fun addPokemon(idPokemon: Int) {


        val callbackAdd = object : Callback<Void> {

            override fun onResponse(call: Call<Void>, response: Response<Void>) {

                if (response.isSuccessful) {
                    progressBarOff()

                } else {

                    mostrarSnackBar(binding.imagePokemonProductView, "Não foi possivel carregar pokemons")
                    Log.e("ERROR", response.errorBody().toString())

                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                progressBarOff()
                mostrarSnackBar(binding.imagePokemonProductView, "Não foi possivel carregar pokemons")

                Log.e("ERROR", "Falha ao conectar ao serviço", t)
            }
        }

        API(this).carrinho.addProdutoCarrinho(idPokemon).enqueue(callbackAdd)
        progressBarOn()

    }
}