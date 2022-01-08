package br.senac.pi4pokemon.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.senac.pi4pokemon.databinding.ActivityPedidoConfirmadoBinding
import br.senac.pi4pokemon.model.Endereco
import br.senac.pi4pokemon.model.Pedidos
import br.senac.pi4pokemon.services.API
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PedidoConfirmadoActivity : AppCompatActivity() {
    lateinit var binding: ActivityPedidoConfirmadoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPedidoConfirmadoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        atualizarPokemons()
        atualizarEndereco()



    }

    fun atualizarPokemons() {


        val callback = object : Callback<List<Pedidos>> {

            override fun onResponse(call: Call<List<Pedidos>>, response: Response<List<Pedidos>>) {

                if (response.isSuccessful) {

                    val listaProduto = response.body()

                    atualizarUI(listaProduto)


                } else {

                    Snackbar.make(binding.constraintLayoutPedidoConfirmado,
                        "Não foi possivel carregar os pokemons",
                        Snackbar.LENGTH_LONG).show()

                    Log.e("ERROR", response.errorBody().toString())

                }

            }

            override fun onFailure(call: Call<List<Pedidos>>, t: Throwable) {

                Snackbar.make(binding.constraintLayoutPedidoConfirmado,
                    "Não foi possivel conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao conectar ao serviço", t)

            }

        }
        API(this).pedido.listarUltimoPedido().enqueue(callback)



    }

    fun atualizarUI(lista: List<Pedidos>?) {

        lista?.get(0)?.let {


            binding.textViewPedidoId.text = it.id.toString()




        }

    }

    fun atualizarEndereco() {


        val callback = object : Callback<List<Endereco>> {

            override fun onResponse(
                call: Call<List<Endereco>>,
                response: Response<List<Endereco>>
            ) {

                if (response.isSuccessful) {

                    val listaProduto2 = response.body()

                    atualizarUiEndereco(listaProduto2)


                } else {

                    Snackbar.make(binding.constraintLayoutPedidoConfirmado,
                        "Não foi possivel carregar os pokemons",
                        Snackbar.LENGTH_LONG).show()

                    Log.e("ERROR", response.errorBody().toString())

                }

            }

            override fun onFailure(call: Call<List<Endereco>>, t: Throwable) {

                Snackbar.make(binding.constraintLayoutPedidoConfirmado,
                    "Não foi possivel conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao conectar ao serviço", t)

            }

        }
        API(this).endereco.listaEndereco().enqueue(callback)

    }

    fun atualizarUiEndereco(lista: List<Endereco>?) {

        lista?.get(0)?.let {


            binding.textViewLogradouroConfirmacao.text = it.rua
            binding.textViewNumeroConfirmacao.text = it.numero.toString()
            binding.textViewComplementoConfirmacao.text = it.complemento
            binding.textViewBairroConfirmacao.text = it.bairro
            binding.textViewCidade.text = it.cidade
            binding.textViewEstadoConfirmacao.text = it.estado
            binding.textViewCEP.text = it.cep








        }
    }
}