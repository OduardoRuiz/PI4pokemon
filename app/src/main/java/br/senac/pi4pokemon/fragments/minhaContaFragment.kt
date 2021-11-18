package br.senac.pi4pokemon.fragments

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
import br.senac.pi4pokemon.model.Pedidos
import br.senac.pi4pokemon.model.Produto
import br.senac.pi4pokemon.services.API
import br.senac.pi4pokemon.views.MeusPedidosActivity
import br.senac.pi4pokemon.views.ProductViewActivity
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
        binding = FragmentMinhacontaBinding.inflate(layoutInflater)


        binding.chipMeuPedido.setOnClickListener {
            val intent = Intent(context, MeusPedidosActivity::class.java)
            startActivity(intent)
        }
        return binding.root

    }


}