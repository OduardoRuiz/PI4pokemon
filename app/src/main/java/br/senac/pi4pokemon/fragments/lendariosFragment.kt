package br.senac.pi4pokemon.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.senac.pi4pokemon.R
import br.senac.pi4pokemon.databinding.CardPokemonsBinding
import br.senac.pi4pokemon.databinding.FragmentLendariosBinding
import br.senac.pi4pokemon.model.Produto
import br.senac.pi4pokemon.services.ProdutoService
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class lendariosFragment : Fragment() {

    lateinit var binding: FragmentLendariosBinding

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentLendariosBinding.inflate(layoutInflater)
        atualizarPokemons()
        return binding.root
    }


    fun atualizarPokemons() {

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ProdutoService::class.java)

        val call = service.listarProdutos()

        val callback = object : Callback<List<Produto>> {

            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {
                if (response.isSuccessful) {
                    val listaProduto = response.body()

                    atualizarUI(listaProduto)



                } else {

                    Snackbar.make(binding.containerProdutos, "Não foi possivel carregar os pokemons",
                        Snackbar.LENGTH_LONG).show()

                    Log.e("ERROR", response.errorBody().toString())

                }

            }

            override fun onFailure(call: Call<List<Produto>>, t: Throwable) {
                Snackbar.make(binding.containerProdutos, "Não foi possivel conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao conectar ao serviço", t)

            }


        }
        call.enqueue(callback)


    }

    fun atualizarUI(lista: List<Produto>?) {
        binding.containerProdutos.removeAllViews()
        lista?.forEach {
            val pokemonBinding = CardPokemonsBinding.inflate(layoutInflater)
            pokemonBinding.nomePokemon.text = it.nome
            pokemonBinding.pontosPokemon.text = it.preco

            binding.containerProdutos.addView(pokemonBinding.root)

        }

    }
}