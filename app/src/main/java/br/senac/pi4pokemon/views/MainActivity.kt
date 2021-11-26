package br.senac.pi4pokemon.views

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import br.senac.pi4pokemon.databinding.ActivityMainBinding
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonTirarFoto.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(i, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode === 1 && resultCode === RESULT_OK && data != null && data.data != null) {
            try {
                var selectedImageUri: Uri = data.data as Uri

                val input = this.contentResolver.openInputStream(selectedImageUri);
                val img = BitmapFactory.decodeStream(input)
                //Substituir this por context no fragmento
                val file = File(this.cacheDir, "avatar.jpg")
                val os = BufferedOutputStream(FileOutputStream(file))
                img.compress(Bitmap.CompressFormat.JPEG, 100, os)
                os.close()

                //AQUI VAI O CÓDIGO QUE VAI CONFIGURAR O "file" NO MULTIPART E MANDAR O MULTIPART PRO RETROFIT

            } catch (e: Exception) {
                Log.e("FileSelectorActivity", "File select error", e)
            }
        }
    }
}