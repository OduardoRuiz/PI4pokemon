package br.senac.pi4pokemon.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.senac.pi4pokemon.R
import br.senac.pi4pokemon.databinding.ActivityNewAdressBinding
import br.senac.pi4pokemon.model.Endereco
import br.senac.pi4pokemon.services.API
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewAdressActivity : AppCompatActivity() {
    lateinit var binding: ActivityNewAdressBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewAdressBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.buttonSalvar.setOnClickListener {


            botaocadastrarEndereco()


        }
    }


    fun botaocadastrarEndereco() {


        val cidade = binding.editTextCidade.text.toString()
        val cep = binding.editTextCep.text.toString()
        val complemento = binding.editTextComplemento.text.toString()
        val estado = binding.editTextTextEstado.text.toString()
        val bairro = binding.editTextBairro.text.toString()
        val rua = binding.editTextLogradouro.text.toString()
        val numero = binding.editTextNumero.text.toString()
        val contato = binding.editTextNumero.text.toString()

        if (cidade.isNotEmpty() && cep.isNotEmpty() && complemento.isNotEmpty()
            && estado.isNotEmpty() && bairro.isNotEmpty() && rua.isNotEmpty() &&
            numero.isNotEmpty() && contato.isNotEmpty() ) {

            val callback = object : Callback<Endereco> {


                override fun onResponse(
                    call: Call<Endereco>,
                    response: Response<Endereco>,
                ) {

                    if (response.isSuccessful) {

                        mostrarToast(this@NewAdressActivity, "Foi")


                    } else {

                        mostrarToast(this@NewAdressActivity, "Endereço atualizado")

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
                numero = numero.toInt(),
                cep = cep,
                complemento = complemento,
                contato = contato.toInt(),
                estado = estado,
                bairro = bairro,
                rua = rua,

                )
            API(this).endereco.cadastraEndereco(endereco = enderecoNovo).enqueue(callback)
            val intent = Intent(this, BottomNavigation::class.java)
            startActivity(intent)


        } else{

            mostrarSnackBar(binding.buttonSalvar, "Preencha todos o campos")
        }








    }
}