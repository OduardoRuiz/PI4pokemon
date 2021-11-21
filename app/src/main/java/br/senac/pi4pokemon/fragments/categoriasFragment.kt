package br.senac.pi4pokemon.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import br.senac.pi4pokemon.R
import br.senac.pi4pokemon.databinding.ActivityMainBinding
import br.senac.pi4pokemon.databinding.CardPokemonsBinding
import br.senac.pi4pokemon.databinding.FragmentCategoriasBinding
import br.senac.pi4pokemon.model.Categoria
import br.senac.pi4pokemon.model.Produto
import br.senac.pi4pokemon.services.API
import br.senac.pi4pokemon.views.ProductViewActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class categoriasFragment : Fragment() {
    lateinit var binding: FragmentCategoriasBinding

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentCategoriasBinding.inflate(layoutInflater)

        atualizarCategoria()
        return binding.root

    }


    fun atualizarCategoria() {


        val callback = object : Callback<List<Categoria>> {

            override fun onResponse(
                call: Call<List<Categoria>>,
                response: Response<List<Categoria>>,
            ) {
                progressBarOff()
                if (response.isSuccessful) {


                    val listaProduto = response.body()
                    atualizarInterface(listaProduto)


                } else {

                    Snackbar.make(binding.containerCateg,
                        "Não foi possivel carregar os pokemons",
                        Snackbar.LENGTH_LONG).show()

                    Log.e("ERROR", response.errorBody().toString())

                }

            }

            override fun onFailure(call: Call<List<Categoria>>, t: Throwable) {
                progressBarOff()
                Snackbar.make(binding.gridLayoutCategoria,
                    "Não foi possivel conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao conectar ao serviço", t)

            }


        }

        fun conectar() {
            API(this.requireContext()).categoria.listarCategoria().enqueue(callback)

        }
        conectar()
        progressBarOn()

    }

    fun atualizarInterface(lista: List<Categoria>?) {
        binding.gridLayoutCategoria.removeAllViews()
        lista?.forEach {
            val pokemonBinding = FragmentCategoriasBinding.inflate(layoutInflater)
            var idAqui = it.nome
            pokemonBinding.textViewCategoria.text = it.nome


            pokemonBinding.imageViewCategoria.setOnClickListener {


                atualizarCategoria2(idAqui)

            }


            Picasso.get()
                .load("http://10.0.2.2:8000/${it.icone}").into(pokemonBinding.imageViewCategoria)

            binding.gridLayoutCategoria.addView(pokemonBinding.root)

        }

    }


    fun atualizarCategoria2(nomeCategoria: String) {


        val callback = object : Callback<List<Produto>> {

            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {
                if (response.isSuccessful) {
                    progressBarOff()
                    val listaProduto = response.body()
                    atualizarInterface2(listaProduto)


                } else {

                    Snackbar.make(binding.scrollView5,
                        "Não foi possivel carregar os pokemons",
                        Snackbar.LENGTH_LONG).show()

                    Log.e("ERROR", response.errorBody().toString())

                }

            }

            override fun onFailure(call: Call<List<Produto>>, t: Throwable) {
                progressBarOff()
                Snackbar.make(binding.scrollView5,
                    "Não foi possivel conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao conectar ao serviço", t)

            }


        }

        API(this.requireContext()).pokemonAberto.pesquisarCategoria("${nomeCategoria}").enqueue(callback)

        progressBarOn()
    }

    fun atualizarInterface2(lista: List<Produto>?) {
        binding.gridLayoutInflapokemon.removeAllViews()
        binding.gridLayoutCategoria.removeAllViews()

        lista?.forEach {
            val pokemonBinding = CardPokemonsBinding.inflate(layoutInflater)
            var idPokemon = it.id
            pokemonBinding.nomePokemon.text = it.nome
            pokemonBinding.pontosPokemon.text = it.preco



            pokemonBinding.linearLayoutCard.setOnClickListener {



                val intent = Intent(context, ProductViewActivity::class.java)
                intent.putExtra("id",idPokemon)
                startActivity(intent)

            }






            Picasso.get()
                .load("http://10.0.2.2:8000/${it.imagem}").into(pokemonBinding.imagemPokemon)

            binding.gridLayoutInflapokemon.addView(pokemonBinding.root)

        }

    }
    fun progressBarOff() {
        binding.progressBarCat.visibility =  View.GONE
        binding.gridLayoutCategoria.visibility = View.VISIBLE


    }
    fun progressBarOn() {
        binding.progressBarCat.visibility =  View.VISIBLE
        binding.gridLayoutCategoria.visibility = View.INVISIBLE

    }



}