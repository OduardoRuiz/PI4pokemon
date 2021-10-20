package br.senac.pi4pokemon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import br.senac.pi4pokemon.databinding.ActivityBottomNavigationBinding
import br.senac.pi4pokemon.fragments.*

class BottomNavigation : AppCompatActivity() {

    lateinit var binding: ActivityBottomNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {






        super.onCreate(savedInstanceState)
        binding = ActivityBottomNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigationView.itemIconTintList = null


        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.categorias -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, categoriasFragment()).commit()
                }
                R.id.lendarios -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, lendariosFragment()).commit()
                }
                R.id.meucarrinho -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, meuCarrinhoFragment()).commit()
                }
                R.id.minhaconta -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, minhaContaFragment()).commit()
                }

                R.id.inicio -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, inicioFragment()).commit()
                }

            }

            true
        }
    }
}