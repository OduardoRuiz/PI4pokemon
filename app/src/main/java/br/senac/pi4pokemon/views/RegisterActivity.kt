package br.senac.pi4pokemon.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.senac.pi4pokemon.R
import br.senac.pi4pokemon.databinding.ActivityRegisterBinding
import br.senac.pi4pokemon.model.Endereco
import br.senac.pi4pokemon.model.User
import br.senac.pi4pokemon.services.API
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonCadastrar.setOnClickListener{
            registrarUsuario()
        }

    }




    fun registrarUsuario() {



        val callback = object : Callback<User> {


            override fun onResponse(
                call: Call<User>,
                response: Response<User>,
            ) {

                if (response.isSuccessful) {

                    mostrarToast(this@RegisterActivity, "Cadastro efetuado")


                } else {

                    mostrarToast(this@RegisterActivity, "Erro")

                    Log.e("ERROR", response.errorBody().toString())

                }

            }

            override fun onFailure(call: Call<User>, t: Throwable) {

                mostrarSnackBar(binding.editTextEmailRegistrar, "Erro")

                Log.e("ERROR", "Falha ao conectar ao serviço", t)

            }

        }
        val nome = binding.editTextNomeRegistrar.text.toString()
        val email = binding.editTextEmailRegistrar.text.toString()
        val emailConfirmar  = binding.editTextEmailConfirmarRegistrar.text.toString()
        val senha = binding.editTextSenhaRegistrar.text.toString()
        val senhaConfirmar = binding.editTextSenhaConfirmar.text.toString()
        if (email == emailConfirmar && senha == senhaConfirmar){

            val novoUser = User(name = nome, password = senhaConfirmar, email = emailConfirmar)
            API(this).usuario.registrarUser(user = novoUser).enqueue(callback)
            val intent = Intent(this, NewAdressActivity::class.java)
            startActivity(intent)



        } else {
            mostrarToast(this, "Revise informações, e-mail ou senha nao conferem")
        }



    }
}