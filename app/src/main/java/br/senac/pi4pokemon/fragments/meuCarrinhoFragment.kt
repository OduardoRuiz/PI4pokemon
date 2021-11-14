package br.senac.pi4pokemon.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.senac.pi4pokemon.databinding.CardPokemoncarrinhoBinding
import br.senac.pi4pokemon.databinding.FragmentMeucarrinhoBinding
import br.senac.pi4pokemon.model.Carrinho
import br.senac.pi4pokemon.model.CarrinhoTeste
import br.senac.pi4pokemon.model.CarrinhoTesteItem
import br.senac.pi4pokemon.services.API

import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class meuCarrinhoFragment : Fragment() {

    lateinit var binding: FragmentMeucarrinhoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMeucarrinhoBinding.inflate(layoutInflater)
        atualizarPokemons()
        // Inflate the layout for this fragment
        return binding.root
    }

    fun atualizarPokemons() {

      val callback = object : Callback<List<Carrinho>> {
          override fun onResponse(call: Call<List<Carrinho>>, response: Response<List<Carrinho>>) {
              progressBarOff()
              if (response.isSuccessful) {
                val listaCarrinho = response.body()

                  atualizarCarrinho(listaCarrinho)
              } else {
                  Snackbar.make(binding.constraintLayoutMeuCarrinho,
                      "Não foi possivel carregar os pokemons",
                      Snackbar.LENGTH_LONG).show()

                  Log.e("ERROR", response.errorBody().toString())

              }
          }

          override fun onFailure(call: Call<List<Carrinho>>, t: Throwable) {
              progressBarOff()
              Snackbar.make(binding.constraintLayoutMeuCarrinho,
                  "Não foi possivel conectar ao servidor",
                  Snackbar.LENGTH_LONG).show()

              Log.e("ERROR", "Falha ao conectar ao serviço", t)
          }


      }
        API(this.requireContext()).carrinho.mostraCarrinho().enqueue(callback)

        progressBarOn()

    }

    fun atualizarCarrinho(carrinhoAdd: List<Carrinho>?) {
        binding.containerCarrinho.removeAllViews()
        carrinhoAdd?.forEach {
            val pokemonBinding = CardPokemoncarrinhoBinding.inflate(layoutInflater)



            pokemonBinding.nomePokemonCarrinho.text = it.nome




            binding.containerCarrinho.addView(pokemonBinding.root)

        }

    }

    fun progressBarOff() {
        binding.progressBarMeuCarrinho.visibility = View.GONE

    }

    fun progressBarOn() {
        binding.progressBarMeuCarrinho.visibility = View.VISIBLE

    }

}


