package br.senac.pi4pokemon.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.senac.pi4pokemon.databinding.CardPokemonsBinding
import br.senac.pi4pokemon.databinding.FragmentLendariosBinding
import br.senac.pi4pokemon.model.Produto
import br.senac.pi4pokemon.services.API
import br.senac.pi4pokemon.views.ProductViewActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.coroutineContext

class lendariosFragment : Fragment() {

    lateinit var binding: FragmentLendariosBinding

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentLendariosBinding.inflate(layoutInflater)

        binding.swipeRefreshTodos.setOnRefreshListener {
            atualizarPokemons()

        }
        atualizarPokemons()
        return binding.root

    }


    fun atualizarPokemons() {






        val callback = object : Callback<List<Produto>> {

            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {
                progressBarOff()
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
                progressBarOff()
                Snackbar.make(binding.containerProdutos, "Não foi possivel conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao conectar ao serviço", t)

            }


        }
            API(this.requireContext()).pokemonAberto.listarProdutos().enqueue(callback)
        progressBarOn()


    }

    fun atualizarUI(lista: List<Produto>?) {
        binding.containerProdutos.removeAllViews()
        lista?.forEach {
            val pokemonBinding = CardPokemonsBinding.inflate(layoutInflater)
            val idAqui = it.id
            pokemonBinding.nomePokemon.text = it.nome
            pokemonBinding.pontosPokemon.text = it.preco
            pokemonBinding.linearLayoutCard.setOnClickListener {


                val intent = Intent(context, ProductViewActivity::class.java)
                intent.putExtra("id",idAqui)
                startActivity(intent)

            }

            Picasso.get()
                .load("http://10.0.2.2:8000/${it.imagem}").into(pokemonBinding.imagemPokemon)

                binding.containerProdutos.addView(pokemonBinding.root)

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