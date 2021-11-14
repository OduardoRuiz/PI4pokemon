package br.senac.pi4pokemon.services

import android.content.Context
import okhttp3.*

const val ARQUIVO_LOGIN = "login"

class AutenticadorToken(private val context: Context) : Interceptor, Authenticator {

    override fun intercept(chain: Interceptor.Chain): Response {
        val prefs = context.getSharedPreferences(ARQUIVO_LOGIN, Context.MODE_PRIVATE)

        val token = prefs.getString("token", "") as String
        val bearer = prefs.getString("bearer", "") as String

        var request = chain.request()

        request = request?.newBuilder()?.addHeader("token", token)?.build()
        request = request?.newBuilder()?.addHeader("bearer", bearer)?.build()

        return chain.proceed(request)

    }

    override fun authenticate(route: Route?, response: Response): Request? {

        val prefs = context.getSharedPreferences(ARQUIVO_LOGIN, Context.MODE_PRIVATE)

        val usuario = prefs.getString("usuario", "") as String
        val senha = prefs.getString("senha", "") as String

        val respostaRetrofit = API(context).login.fazerLogin(usuario, senha).execute()


        var token = respostaRetrofit.body()
        if (respostaRetrofit.isSuccessful && token != null) {

            val editor = prefs.edit()
            editor.putString("token", token.token)
            editor.apply()
            return response?.request()?.newBuilder()?.header("token", token.token)?.build()
        }
        return null

    }




}