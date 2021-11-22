package br.senac.pi4pokemon.views

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import br.senac.pi4pokemon.R
import br.senac.pi4pokemon.databinding.ActivityBottomNavigationBinding
import br.senac.pi4pokemon.fragments.*
import br.senac.pi4pokemon.model.Produto
import br.senac.pi4pokemon.services.API
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BottomNavigation : AppCompatActivity() {

    lateinit var binding: ActivityBottomNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityBottomNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigationView.itemIconTintList = null


        supportFragmentManager.beginTransaction()
            .replace(R.id.container, inicioFragment()).commit()
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menuqrcode, menu)

        val searchItem = menu?.findItem(R.id.pesquisa)
        val searchView = searchItem?.actionView as SearchView

        searchView?.let {
            val querryListiner = object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {

                        searchView.clearFocus()
                        searchItem.collapseActionView()
                    val searchFragment = SearchFragment.newInstance(it)

                        supportFragmentManager.beginTransaction().replace(R.id.container, searchFragment).commit()
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                   return true
                }

            }
            searchView.setOnQueryTextListener(querryListiner)
        }


        return super.onCreateOptionsMenu(menu)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.qrcode) {

            val i = Intent(this, QrCodeActivity::class.java)
            startActivityForResult(i, 1)

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            val idProduto = data.getStringExtra("qrcode") as String

           // mostrarDialogo(this, "${idProduto}")
          obterProduto(idProduto.toInt())

        }
    }

    fun obterProduto(idParametro: Int) {
        val callback = object : Callback<Produto> {
            override fun onResponse(call: Call<Produto>, response: Response<Produto>) {
                val prod = response.body()
                if (response.isSuccessful && prod != null) {

                    val idAqui = prod.id
                    AlertDialog.Builder(this@BottomNavigation)
                        .setTitle("Produto Escaneado, para ver com detalhes clique no botão")
                        .setMessage("Nome pokemon: ${prod.nome}, Valor em pontos: ${prod.preco}")
                        .setCancelable(false)
                        .setPositiveButton("Ver detalhes") { dialogInterface, i ->
                            val i = Intent(this@BottomNavigation, ProductViewActivity::class.java)
                            intent.putExtra("id",idAqui)
                            startActivity(i)
                            setResult(RESULT_CANCELED)
                            finish()
                        }
                        .setNegativeButton("Sair") { dialogInterface, i ->
                            setResult(RESULT_CANCELED)
                            finish()
                        }
                        .create()
                        .show()
                }
            }

            override fun onFailure(call: Call<Produto>, t: Throwable) {
                mostrarToast(this@BottomNavigation, "Falha ao Obter o produto")
                Log.e("BottonNavigation", "ObterProduto", t)
            }

        }
        API(this).pokemonAberto.obterProdutoId(idParametro).enqueue(callback)
    }
}