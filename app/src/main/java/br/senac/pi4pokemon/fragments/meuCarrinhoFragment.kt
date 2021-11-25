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
import br.senac.pi4pokemon.views.mostrarSnackBar

import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.MemoryPolicy
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
            override fun onResponse(
                call: Call<List<Carrinho>>,
                response: Response<List<Carrinho>>,
            ) {
                progressBarOff()
                if (response.isSuccessful) {
                    val listaCarrinho = response.body()

                    atualizarCarrinho(listaCarrinho)
                } else {
                    mostrarSnackBar(binding.constraintLayoutMeuCarrinho, "Não foi possivel carregar " +
                            ", faça o login na Aba Minha conta")

                    Log.e("ERROR", response.errorBody().toString())

                }
            }

            override fun onFailure(call: Call<List<Carrinho>>, t: Throwable) {
                progressBarOff()
                mostrarSnackBar(binding.constraintLayoutMeuCarrinho, "Não foi possivel carregar" +
                        ", faça o login na Aba Minha conta" )
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


            val idPokemon = it.produto_id
            pokemonBinding.pontosPokemonCarrinho.text = it.preco
            pokemonBinding.nomePokemonCarrinho.text = it.nome
            pokemonBinding.textViewQuantidade.text = it.quantidade.toString()

            binding.buttonFinalizarCompra.setOnClickListener {
                val intent = Intent(context, ConfirmacaoActivity::class.java)
                intent.putExtra("id", idPokemon)
                startActivity(intent)

            }
            pokemonBinding.imageViewAdd.setOnClickListener {
                addPokemon(idPokemon)
                atualizarPokemons()
            }
            pokemonBinding.imageViewMenos.setOnClickListener {
                removePokemon(idPokemon)
                atualizarPokemons()
            }


            pokemonBinding.cardImagemCarrinho.setOnClickListener {


                val intent = Intent(context, ProductViewActivity::class.java)
                intent.putExtra("id", idPokemon)
                startActivity(intent)

            }
            Picasso.get()
                .load("http://10.0.2.2:8000/${it.imagem}")
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(pokemonBinding.imagemPokemonCarrinho)




            binding.containerCarrinho.addView(pokemonBinding.root)

        }

    }

    fun progressBarOff() {
        binding.progressBarMeuCarrinho.visibility = View.GONE

    }

    fun progressBarOn() {
        binding.progressBarMeuCarrinho.visibility = View.VISIBLE

    }

    fun addPokemon(add: Int) {


        val callbackAdd = object : Callback<Void> {

            override fun onResponse(call: Call<Void>, response: Response<Void>) {

                if (response.isSuccessful) {
                    progressBarOff()
                  //  atualizarCarrinhoBotao(response.body())


                } else {

                    mostrarSnackBar(binding.scrollView2, "Não foi possivel carregar pokemons")
                    Log.e("ERROR", response.errorBody().toString())

                }

            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                progressBarOff()

                mostrarSnackBar(binding.scrollView2, "Não foi possivel carregar pokemons")
                Log.e("ERROR", "Falha ao conectar ao serviço", t)

            }


        }

        API(this.requireContext()).carrinho.addProdutoCarrinho(add).enqueue(callbackAdd)
        progressBarOn()


    }

    fun removePokemon(remove: Int) {


        val callbackAdd = object : Callback<Void> {

            override fun onResponse(call: Call<Void>, response: Response<Void>) {

                if (response.isSuccessful) {
                    progressBarOff()

             //       atualizarCarrinhoBotao(response.body())


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

        API(this.requireContext()).carrinho.removeProdutoCarrinho(remove).enqueue(callbackAdd)
        progressBarOn()


    }

    fun uploadfoto() {

    }



   // fun atualizarCarrinhoBotao(body: Void?) {
     //   binding.containerCarrinho.removeAllViews()




     //  binding.containerCarrinho.addView(meuCarrinhoFragment)

    }







