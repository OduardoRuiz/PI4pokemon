package br.senac.pi4pokemon.views

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import br.senac.pi4pokemon.databinding.ActivityLoginBinding
import br.senac.pi4pokemon.model.Token
import br.senac.pi4pokemon.model.User
import br.senac.pi4pokemon.services.API
import br.senac.pi4pokemon.services.ARQUIVO_LOGIN
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonRegistrar.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.buttonLogin.setOnClickListener {
            val usuario = binding.editLogin.text.toString()
            val senha = binding.editSenha.text.toString()

            val callback = object: Callback<Token> {
                override fun onResponse(call: Call<Token>, response: Response<Token>) {

                    val token = response.body()
                    if (response.isSuccessful && token != null) {

                        val  editor = getSharedPreferences(ARQUIVO_LOGIN, Context.MODE_PRIVATE).edit()

                        editor.putString("usuario", usuario)
                        editor.putString("senha", senha)
                        editor.putString("token", token.token)
                        editor.apply()
                        val intent = Intent(this@LoginActivity, BottomNavigation::class.java)

                        startActivity(intent)

                        Toast.makeText(this@LoginActivity, "Login Efetuado", Toast.LENGTH_LONG).show()

                    } else {
                        var msg = "Não foi possivel logar"

                        Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_LONG).show()
                        response.errorBody()?.let {
                            Log.e("LoginActivity", it.string())
                        }

                    }

                }

                override fun onFailure(call: Call<Token>, t: Throwable) {
                  Toast.makeText(this@LoginActivity, "Verique usuario ou senha ou faça o cadastro", Toast.LENGTH_LONG).show()
                    Log.e("LoginActivity", "onCreate", t)
                }

            }

            val user =  User(email = binding.editLogin.text.toString(),
                password = binding.editSenha.text.toString())
            API(this).login.fazerLogin(user).enqueue(callback)
        }
    }
}