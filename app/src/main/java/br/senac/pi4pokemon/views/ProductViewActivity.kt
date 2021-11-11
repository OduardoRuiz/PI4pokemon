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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductViewActivity : AppCompatActivity() {

    lateinit var binding: ActivityProductViewBinding






    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityProductViewBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
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
          API.pokemon.pesquisarProdutos(idPokemon).enqueue(callback)
        progressBarOn()



    }

    fun atualizarUI(lista: List<Produto>?) {
        binding.constraintLayoutProductView.removeAllViews()
        lista?.forEach {
            val pokemonBinding = ActivityProductViewBinding.inflate(layoutInflater)
            pokemonBinding.nomePokemonProductView.text = it.nome
            pokemonBinding.textPontosPokemonProductView.text = it.preco
            pokemonBinding.textDescricaoProdutoView.text = it.descricao








            if (it.categoria_id ==  3 ) {
                pokemonBinding.buttonTipoProductView.text = "Funciona"
                pokemonBinding.buttonTipoProductView.setBackgroundColor(getColor(R.color.pokeYellow))

            }

            Picasso.get()
                .load("http://10.0.2.2:8000/${it.imagem}").into(pokemonBinding.imagePokemonProductView)
            binding.constraintLayoutProductView.addView(pokemonBinding.root)

        }

    }
    fun progressBarOff() {
        binding.progressBarProd.visibility =  View.GONE

    }
    fun progressBarOn() {
        binding.progressBarProd.visibility =  View.VISIBLE

    }

}