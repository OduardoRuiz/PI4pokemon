package br.senac.pi4pokemon.views


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import br.senac.pi4pokemon.R
import br.senac.pi4pokemon.databinding.ActivityProductViewBinding

import br.senac.pi4pokemon.model.Produto
import br.senac.pi4pokemon.services.API
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
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


        val callback = object : Callback<List<Produto>> {

            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {

                if (response.isSuccessful) {
                    progressBarOff()
                    val listaProduto = response.body()

                    atualizarUI(listaProduto)


                } else {

                    Snackbar.make(binding.textView8, "Não foi possivel carregar os pokemons",
                        Snackbar.LENGTH_LONG).show()

                    Log.e("ERROR", response.errorBody().toString())

                }
            }
            override fun onFailure(call: Call<List<Produto>>, t: Throwable) {
                progressBarOff()
                Snackbar.make(binding.textView9, "Não foi possivel conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao conectar ao serviço", t)
            }

        }
        //id default como 2 para a product view activity nao ficar em branco ao ser rodada direto
        var idPokemon = intent.getIntExtra("id", 2)
        API(this).pokemonAberto.pesquisarProdutos(idPokemon).enqueue(callback)

        binding.buttonAddCarrinho.setOnClickListener {
            addPokemon(idPokemon)
            mostrarToast(this, "Produto adicionado ao carrinho")
        }
        binding.buttonComprarAgora.setOnClickListener {
            addPokemon(idPokemon)
            mostrarToast(this, "Produto adicionado ao carrinho")
        }

        progressBarOn()

    }

    fun atualizarUI(lista: List<Produto>?) {
        lista?.get(0)?.let {
            val idAquiFrag = it.id

            binding.nomePokemonProductView.text = it.nome
            binding.textPontosPokemonProductView.text = it.preco
            binding.textDescricaoProdutoView.text = it.descricao


            if (it.categoria_id == 3) {
                binding.buttonTipoProductView.text = "Funciona"
                binding.buttonTipoProductView.setBackgroundColor(getColor(R.color.pokeYellow))

            }

            Picasso.get()
                .load("http://10.0.2.2:8000/${it.imagem}")
                .into(binding.imagePokemonProductView)


        }

    }
    fun progressBarOff() {
        binding.progressBarProd.visibility = View.GONE
        binding.imagePokemonProductView.visibility = View.VISIBLE

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

                    Snackbar.make(binding.imagePokemonProductView,
                        "Não foi possivel carregar os pokemons",
                        Snackbar.LENGTH_LONG).show()

                    Log.e("ERROR", response.errorBody().toString())

                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                progressBarOff()
                Snackbar.make(binding.imagePokemonProductView,
                    "Não foi possivel conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao conectar ao serviço", t)
            }
        }

        API(this).carrinho.addProdutoCarrinho(idPokemon).enqueue(callbackAdd)
        progressBarOn()

    }
}