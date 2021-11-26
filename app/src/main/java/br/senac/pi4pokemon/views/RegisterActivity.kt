package br.senac.pi4pokemon.views

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import br.senac.pi4pokemon.R
import br.senac.pi4pokemon.databinding.ActivityRegisterBinding
import br.senac.pi4pokemon.model.Endereco
import br.senac.pi4pokemon.model.Token
import br.senac.pi4pokemon.model.User
import br.senac.pi4pokemon.services.API
import br.senac.pi4pokemon.services.ARQUIVO_LOGIN
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
        if (email == emailConfirmar && senha == senhaConfirmar &&
            senha.length >= 8 && senhaConfirmar.length >= 8  ){

            val novoUser = User(name = nome, password = senhaConfirmar, email = emailConfirmar)
            API(this).usuario.registrarUser(user = novoUser).enqueue(callback)

            login(usuario = emailConfirmar, senha = senhaConfirmar)





        } else {
            mostrarToast(this, "Revise informações, e-mail ou senha nao conferem," +
                    " a senha necessita de 8 caracteres")
        }



    }

    fun login(usuario: String, senha: String) {


        val callback = object: Callback<Token> {
            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                val token = response.body()

                if (response.isSuccessful && token != null) {


                    val  editor = getSharedPreferences(ARQUIVO_LOGIN, Context.MODE_PRIVATE).edit()

                    editor.putString("usuario", usuario)
                    editor.putString("senha", senha)
                    editor.putString("token", token.token)
                    editor.apply()


                    val intent = Intent(this@RegisterActivity, NewAdressActivity::class.java)
                    startActivity(intent)

                    Toast.makeText(this@RegisterActivity, "Login Efetuado", Toast.LENGTH_LONG).show()

                } else {
                    var msg = response.message().toString()
                    if (msg == "") {

                        msg = "Não foi possivel efetuar login"
                    }
                    Toast.makeText(this@RegisterActivity, msg, Toast.LENGTH_LONG).show()
                    response.errorBody()?.let {
                        Log.e("LoginActivity", it.string())
                    }

                }

            }

            override fun onFailure(call: Call<Token>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Verique usuario ou senha ou faça o cadastro", Toast.LENGTH_LONG).show()
                Log.e("LoginActivity", "onCreate", t)
            }

        }

        val user =  User(email = usuario,
            password = senha)
        API(this).login.fazerLogin(user).enqueue(callback)
    }
}