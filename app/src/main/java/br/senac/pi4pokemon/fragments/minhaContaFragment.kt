package br.senac.pi4pokemon.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toFile
import br.senac.pi4pokemon.R
import br.senac.pi4pokemon.databinding.CardPedidosBinding
import br.senac.pi4pokemon.databinding.CardPokemonsBinding
import br.senac.pi4pokemon.databinding.FragmentMinhacontaBinding
import br.senac.pi4pokemon.model.*
import br.senac.pi4pokemon.services.API
import br.senac.pi4pokemon.services.ARQUIVO_LOGIN
import br.senac.pi4pokemon.views.*
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class minhaContaFragment : Fragment() {
    lateinit var binding: FragmentMinhacontaBinding
    lateinit var usuario: User
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
        binding.buttonMudaImagem.setOnClickListener {
            AlertDialog.Builder(this.requireContext())
                .setTitle("muda foto")
                .setCancelable(true)
                .setNeutralButton("Escolher imagem") { _: DialogInterface, _: Int ->
                    val grapPhoto = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )

                    startActivityForResult(grapPhoto, 1)
                }
                .create()
                .show()



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
                    mostrarToast(this@minhaContaFragment.requireContext(), "Deslogado com sucesso")
                    val prefs = this@minhaContaFragment.requireContext().getSharedPreferences(ARQUIVO_LOGIN, Context.MODE_PRIVATE)
                    val editor = prefs.edit()
                    editor.putString("token", "")
                    editor.putString("usuario", "")
                    editor.putString("senha", "")
                    editor.apply()
                    binding.imageViewLogoff.visibility = View.INVISIBLE
                    binding.chipMeuPedido.visibility = View.INVISIBLE
                    binding.chipMeuPerfil.visibility = View.INVISIBLE
                    binding.chipEditaEndereco.visibility = View.INVISIBLE
                    binding.buttonMudaImagem.visibility = View.INVISIBLE
                    binding.chipLogin.visibility = View.VISIBLE


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

                        usuario = listaProduto
                        atualizarUI(listaProduto)
                    }


                } else {

                    Snackbar.make(binding.imageViewUsuarioMinhaConta, "Não foi possivel carregar os pokemons",
                        Snackbar.LENGTH_LONG).show()

                    Log.e("ERROR", response.errorBody().toString())

                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {

                Snackbar.make(binding.imageViewUsuarioMinhaConta, "Não foi possivel conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao conectar ao serviço", t)
            }

        }

        API(this.requireContext()).usuario.listarPerfil().enqueue(callback)



    }

    fun atualizarUI(user : User) {
        user.let {


            binding.textViewUsuario.text = it.name


            Picasso.get()
                .load("http://10.0.2.2:8000/${it.avatar}").memoryPolicy(MemoryPolicy.NO_CACHE).into(binding.imageViewUsuarioMinhaConta)






        }

    }

    override fun onResume() {
        super.onResume()
        val prefs = this.requireContext().getSharedPreferences(ARQUIVO_LOGIN, Context.MODE_PRIVATE)
        val token = prefs.getString("token", "") as String
        if (token.isEmpty()){

            binding.imageViewLogoff.visibility = View.INVISIBLE
            binding.chipMeuPedido.visibility = View.INVISIBLE
            binding.chipMeuPerfil.visibility = View.INVISIBLE
            binding.chipEditaEndereco.visibility = View.INVISIBLE
            binding.buttonMudaImagem.visibility = View.INVISIBLE
            binding.chipLogin.visibility = View.VISIBLE
        }else{

            binding.imageViewLogoff.visibility = View.VISIBLE
            binding.chipMeuPedido.visibility = View.VISIBLE
            binding.chipMeuPerfil.visibility = View.VISIBLE
            binding.chipEditaEndereco.visibility = View.VISIBLE
            binding.chipLogin.visibility = View.INVISIBLE

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1){
            val contactUri: Uri? = data?.data
            val file = contactUri?.toFile()
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val body = MultipartBody.Part.createFormData("imagem", file?.name, requestFile);

            val callback = object: Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                   if (response.isSuccessful){

                       Picasso.get()
                           .load("http://10.0.2.2:8000/${usuario.avatar}").memoryPolicy(MemoryPolicy.NO_CACHE).into(binding.imageViewUsuarioMinhaConta)

                   }else {

                       Snackbar.make(binding.imageViewUsuarioMinhaConta, "Não foi possivel carregar os pokemons",
                           Snackbar.LENGTH_LONG).show()

                       Log.e("ERROR", response.errorBody().toString())

                   }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Snackbar.make(binding.imageViewUsuarioMinhaConta, "Não foi possivel conectar ao servidor",
                        Snackbar.LENGTH_LONG).show()

                    Log.e("ERROR", "Falha ao conectar ao serviço", t)
                }



            }
            API(this.requireContext()).usuario.uploadfoto(body).enqueue(callback)

        }
    }





}