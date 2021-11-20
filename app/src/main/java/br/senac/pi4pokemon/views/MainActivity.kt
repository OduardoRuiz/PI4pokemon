package br.senac.pi4pokemon.views

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.drawToBitmap
import br.senac.pi4pokemon.databinding.ActivityMainBinding
import br.senac.pi4pokemon.databinding.CardPokemonsBinding
import br.senac.pi4pokemon.model.Categoria
import br.senac.pi4pokemon.model.Produto
import br.senac.pi4pokemon.services.API
import br.senac.pi4pokemon.services.ProdutoService
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {


    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val laucher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val img: Bitmap? = it.data?.getParcelableExtra("data")

                binding.imageViewTirarFoto.setImageBitmap(img)
                binding.textViewImg.text = img.toString()

            }

        }

        binding.buttonTirarFoto.setOnClickListener {
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            if (i.resolveActivity(packageManager) != null) {

                laucher.launch(i)

            }
        }
    }


}