package br.senac.pi4pokemon.services

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class API(private val context: Context) {


    private val baseUrl = "http://10.0.2.2:8000"
    private val timeout = 30L


    private val retrofitAberto: Retrofit
        get() {

            val okHttp = OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)

                .build()

            return Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttp)
                .build()
        }

    private val retrofitSeguro: Retrofit
        get() {
            val autenticador = AutenticadorToken(context)


            val okHttp = OkHttpClient.Builder()
                .readTimeout(timeout, TimeUnit.SECONDS)
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .addInterceptor(autenticador)
                .authenticator(autenticador)
                .build()
            return Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttp)
                .build()
        }


    val pokemonAberto: ProdutoService
        get() {
            return retrofitAberto.create(ProdutoService::class.java)
        }

    val pokemonSeguro: ProdutoService
        get() {
            return retrofitSeguro.create(ProdutoService::class.java)
        }

    val login: LoginService
    get() {
        return retrofitAberto.create(LoginService::class.java)
    }
    val cliente: UserService
    get() {
        return retrofitSeguro.create(UserService::class.java)
    }
    val carrinho: CarrinhoService
    get() {
        return retrofitSeguro.create(CarrinhoService::class.java)
    }

    val categoria: CategoriaService
        get() {
            return retrofitAberto.create(CategoriaService::class.java)
        }
    val pedido: PedidoService
        get() {
            return retrofitSeguro.create(PedidoService::class.java)
        }
    val endereco: EnderecoService
        get() {
            return retrofitSeguro.create(EnderecoService::class.java)
        }
    val pedidoItens: PedidoItemService
        get() {
            return retrofitSeguro.create(PedidoItemService::class.java)
        }
    val usuario: UserService
        get() {
            return retrofitSeguro.create(UserService::class.java)
        }
    val logoff: UserService
        get() {
            return retrofitSeguro.create(UserService::class.java)
        }

}