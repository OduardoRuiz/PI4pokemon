package br.senac.pi4pokemon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.senac.pi4pokemon.databinding.ActivityBottomNavigationBinding

class BottomNavigation : AppCompatActivity() {

    lateinit var binding: ActivityBottomNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityBottomNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigationView.itemIconTintList = null
    }
}