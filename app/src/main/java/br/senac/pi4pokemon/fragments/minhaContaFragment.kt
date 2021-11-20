package br.senac.pi4pokemon.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.senac.pi4pokemon.R
import br.senac.pi4pokemon.databinding.CardPedidosBinding
import br.senac.pi4pokemon.databinding.CardPokemonsBinding
import br.senac.pi4pokemon.databinding.FragmentMinhacontaBinding
import br.senac.pi4pokemon.model.Carrinho
import br.senac.pi4pokemon.model.Pedidos
import br.senac.pi4pokemon.model.Produto
import br.senac.pi4pokemon.model.User
import br.senac.pi4pokemon.services.API
import br.senac.pi4pokemon.services.ARQUIVO_LOGIN
import br.senac.pi4pokemon.views.*
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class minhaContaFragment : Fragment() {
    lateinit var binding: FragmentMinhacontaBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        atualizarPerfil()
        binding = FragmentMinhacontaBinding.inflate(layoutInflater)


        binding.chipMeuPedido.setOnClickListener {
            val intent = Intent(context, MeusPedidosActivity::class.java)
            startActivity(intent)
        }
        binding.chipMeuPerfil.setOnClickListener {
            val intent = Intent(context, MeuPerfilActivity::class.java)
            startActivity(intent)
        }
        binding.chipEditaEndereco.setOnClickListener {
            val intent = Intent(context, EditaEnderecoActivity::class.java)
            startActivity(intent)
        }

        binding.chipLogin.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.imageViewLogoff.setOnClickListener {
            logoff()
            mostrarToast(this.requireContext(), "Você Deslogou com sucesso")
            val intent = Intent(context, BottomNavigation::class.java)
            startActivity(intent)
        }
        return binding.root

    }

    fun logoff(){

        val callback = object : Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>,
            ) {

                if (response.isSuccessful) {

                } else {
                    mostrarToast(this@minhaContaFragment.requireContext(), "Falha ao deslogar")

                    Log.e("ERROR", response.errorBody().toString())

                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {

                mostrarToast(this@minhaContaFragment.requireContext(), "Falha ao deslogar")
                Log.e("ERROR", "Falha ao conectar ao serviço", t)
            }


        }


        API(this.requireContext()).logoff.logoff().enqueue(callback)

    }

    fun atualizarPerfil() {


        val callback = object : Callback<User> {

            override fun onResponse(call: Call<User>, response: Response<User>) {

                if (response.isSuccessful) {

                    val listaProduto = response.body()

                    if (listaProduto != null) {
                        atualizarUI(listaProduto)
                    }


                } else {

                    Snackbar.make(binding.imageView9, "Não foi possivel carregar os pokemons",
                        Snackbar.LENGTH_LONG).show()

                    Log.e("ERROR", response.errorBody().toString())

                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {

                Snackbar.make(binding.imageView9, "Não foi possivel conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao conectar ao serviço", t)
            }

        }

        API(this.requireContext()).usuario.listarPerfil().enqueue(callback)



    }

    fun atualizarUI(user : User) {
        user.let {


            binding.textViewUsuario.text = it.name




        }

    }




}