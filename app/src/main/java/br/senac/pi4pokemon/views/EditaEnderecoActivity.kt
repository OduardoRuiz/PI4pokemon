package br.senac.pi4pokemon.views

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import br.senac.pi4pokemon.R
import br.senac.pi4pokemon.databinding.ActivityEditaEnderecoBinding
import br.senac.pi4pokemon.model.Endereco
import br.senac.pi4pokemon.model.Token
import br.senac.pi4pokemon.model.User
import br.senac.pi4pokemon.services.API
import br.senac.pi4pokemon.services.ARQUIVO_LOGIN
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditaEnderecoActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditaEnderecoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditaEnderecoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        atualizarEndereco()

        binding.buttonSalvar.setOnClickListener {

            botaoAtualizarEndereco()
            atualizarEndereco()

        }
    }

    fun atualizarEndereco() {


        val callback = object : Callback<List<Endereco>> {

            override fun onResponse(
                call: Call<List<Endereco>>,
                response: Response<List<Endereco>>,
            ) {

                if (response.isSuccessful) {

                    val listaProduto2 = response.body()

                    atualizarUiEndereco(listaProduto2)


                } else {

                    Snackbar.make(binding.imageView3,
                        "Não foi possivel carregar os pokemons",
                        Snackbar.LENGTH_LONG).show()

                    Log.e("ERROR", response.errorBody().toString())

                }

            }

            override fun onFailure(call: Call<List<Endereco>>, t: Throwable) {

                Snackbar.make(binding.imageView3,
                    "Não foi possivel conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao conectar ao serviço", t)

            }

        }
        API(this).endereco.listaEndereco().enqueue(callback)


    }

    fun atualizarUiEndereco(lista: List<Endereco>?) {

        lista?.get(0)?.let {


            binding.editTextLogradouro.setText(it.rua)
            binding.editTextNumero.setText(it.numero.toString())
            binding.editTextComplemento.setText(it.complemento)
            binding.editTextBairro.setText(it.bairro)
            binding.editTextCidade.setText(it.cidade)
            binding.editTextTextEstado.setText(it.estado)
            binding.editTextCep.setText(it.cep)
            binding.editTextPhone.setText(it.contato.toString())


        }
    }

    fun botaoAtualizarEndereco() {


       val cidade = binding.editTextCidade.text.toString()
        val numero = binding.editTextNumero.text.toString().toInt()
        val cep = binding.editTextCep.text.toString()
        val complemento = binding.editTextComplemento.text.toString()
        val contato = binding.editTextPhone.text.toString().toInt()
        val estado = binding.editTextTextEstado.text.toString()
        val bairro = binding.editTextBairro.text.toString()
        val rua = binding.editTextLogradouro.text.toString()


        val callback = object : Callback<Endereco> {


            override fun onResponse(
                call: Call<Endereco>,
                response: Response<Endereco>,
            ) {

                if (response.isSuccessful) {

                    mostrarToast(this@EditaEnderecoActivity, "Foi")


                } else {

                    mostrarToast(this@EditaEnderecoActivity, "Endereço atualizado")

                    Log.e("ERROR", response.errorBody().toString())

                }

            }

            override fun onFailure(call: Call<Endereco>, t: Throwable) {

                mostrarSnackBar(binding.imageView7, "Endereço atualizado***")

                Log.e("ERROR", "Falha ao conectar ao serviço", t)

            }

        }

        val enderecoNovo = Endereco(
            cidade = cidade,
            numero = numero,
            cep = cep,
            complemento = complemento,
            contato = contato,
            estado = estado,
            bairro = bairro,
            rua = rua,

            )
        API(this).endereco.editaEndereco(endereco = enderecoNovo).enqueue(callback)

    }


}