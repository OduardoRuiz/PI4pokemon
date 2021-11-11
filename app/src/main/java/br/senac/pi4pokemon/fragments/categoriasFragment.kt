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
import br.senac.pi4pokemon.databinding.CardPokemonsBinding
import br.senac.pi4pokemon.databinding.FragmentCategoriasBinding
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
    var clicadoCategoria = ""
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentCategoriasBinding.inflate(layoutInflater)

        binding.imageViewTerraCategoria.setOnClickListener {

            clicadoCategoria = "terra"
            atualizarCategoria(clicadoCategoria)
        }
        binding.imageAguaCategoria.setOnClickListener {

            clicadoCategoria = "agua"
            atualizarCategoria(clicadoCategoria)

        }
        binding.imageEletricoCategoria.setOnClickListener {

            clicadoCategoria = "Trovão"
            atualizarCategoria(clicadoCategoria)

        }
        binding.imageDarkCategoria.setOnClickListener {

            clicadoCategoria = "Dark"
            atualizarCategoria(clicadoCategoria)

        }
        binding.imageDragaoCategoria.setOnClickListener {

            clicadoCategoria = "Dragao"
            atualizarCategoria(clicadoCategoria)

        }
        binding.imageFadaCategoria.setOnClickListener {

            clicadoCategoria = "fada"
            atualizarCategoria(clicadoCategoria)

        }
        binding.imageFantasmaCategoria.setOnClickListener {

            clicadoCategoria = "Fantasma"
            atualizarCategoria(clicadoCategoria)

        }
        binding.imageFogoCategoria.setOnClickListener {

            clicadoCategoria = "Fogo"
            atualizarCategoria(clicadoCategoria)

        }
        binding.imageGeloCategoria.setOnClickListener {

            clicadoCategoria = "Gelo"
            atualizarCategoria(clicadoCategoria)

        }
        binding.imageGramaCategoria.setOnClickListener {

            clicadoCategoria = "Grama"
            atualizarCategoria(clicadoCategoria)

        }
        binding.imageInsetoCategoria.setOnClickListener {

            clicadoCategoria = "Inseto"
            atualizarCategoria(clicadoCategoria)

        }

        return binding.root
    }


    fun atualizarCategoria(clicandoFuncao: String) {


        val callback = object : Callback<List<Produto>> {

            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {
                if (response.isSuccessful) {
                    progressBarOff()
                    val listaProduto = response.body()
                    atualizarInterface(listaProduto)


                } else {

                    Snackbar.make(binding.constraintLayoutCategorias,
                        "Não foi possivel carregar os pokemons",
                        Snackbar.LENGTH_LONG).show()

                    Log.e("ERROR", response.errorBody().toString())

                }

            }

            override fun onFailure(call: Call<List<Produto>>, t: Throwable) {
                progressBarOff()
                Snackbar.make(binding.constraintLayoutCategorias,
                    "Não foi possivel conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao conectar ao serviço", t)

            }


        }
        fun conectar() {
            API.pokemon.pesquisarCategoria("${clicandoFuncao}").enqueue(callback)

        }
        conectar()
        progressBarOn()
    }

    fun atualizarInterface(lista: List<Produto>?) {
        binding.containerCategorias.removeAllViews()
        lista?.forEach {
            val pokemonBinding = CardPokemonsBinding.inflate(layoutInflater)
            val idAqui = it.id
            pokemonBinding.nomePokemon.text = it.nome
            pokemonBinding.pontosPokemon.text = it.preco

            pokemonBinding.buttonVisualizarPokemon.setOnClickListener {


                val intent = Intent(context, ProductViewActivity::class.java)
                intent.putExtra("id",idAqui)
                startActivity(intent)

            }




            Picasso.get()
                .load("http://10.0.2.2:8000/${it.imagem}").into(pokemonBinding.imagemPokemon)

            binding.containerCategorias.addView(pokemonBinding.root)

        }

    }
    fun progressBarOff() {
        binding.progressBarCat.visibility =  View.GONE

    }
    fun progressBarOn() {
        binding.progressBarCat.visibility =  View.VISIBLE

    }



}