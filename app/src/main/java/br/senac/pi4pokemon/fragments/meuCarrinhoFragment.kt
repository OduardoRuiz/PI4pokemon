package br.senac.pi4pokemon.fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.senac.pi4pokemon.databinding.CardPokemoncarrinhoBinding
import br.senac.pi4pokemon.databinding.FragmentMeucarrinhoBinding
import br.senac.pi4pokemon.model.Carrinho

import br.senac.pi4pokemon.services.API
import br.senac.pi4pokemon.views.ConfirmacaoActivity
import br.senac.pi4pokemon.views.ProductViewActivity

import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
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

binding.buttonAdd.setOnClickListener {
    addPokemon()
}

        progressBarOn()



    }

    fun atualizarCarrinho(carrinhoAdd: List<Carrinho>?) {
        binding.containerCarrinho.removeAllViews()
        carrinhoAdd?.forEach {
            val pokemonBinding = CardPokemoncarrinhoBinding.inflate(layoutInflater)

          val idAqui = it.produtoId

            val idPokemon =  it.produtoId
            pokemonBinding.pontosPokemonCarrinho.text = it.preco
            pokemonBinding.nomePokemonCarrinho.text = it.nome

            binding.buttonFinalizarCompra.setOnClickListener {
                val intent = Intent(context, ConfirmacaoActivity::class.java)
                intent.putExtra("id",idAqui)
                startActivity(intent)

            }
            pokemonBinding.cardImagemCarrinho.setOnClickListener {



                val intent = Intent(context, ProductViewActivity::class.java)
                intent.putExtra("id", idPokemon)
                startActivity(intent)

            }
            Picasso.get()
                .load("http://10.0.2.2:8000/${it.imagem}").into(pokemonBinding.imagemPokemonCarrinho)




            binding.containerCarrinho.addView(pokemonBinding.root)

        }

    }

    fun progressBarOff() {
        binding.progressBarMeuCarrinho.visibility = View.GONE

    }

    fun progressBarOn() {
        binding.progressBarMeuCarrinho.visibility = View.VISIBLE

    }

    fun addPokemon() {


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
                Snackbar.make(binding.scrollView2, "Não foi possivel conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao conectar ao serviço", t)

            }


        }
        //id default como 2 para a product view activity nao ficar em branco ao ser rodada direto
       // var idPokemon = intent.getIntExtra("id", 2)
        API(this.requireContext()).carrinho.addProdutoCarrinho(10).enqueue(callbackAdd)
        progressBarOn()





    }

}


