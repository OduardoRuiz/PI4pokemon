package br.senac.pi4pokemon.views

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import br.senac.pi4pokemon.R
import br.senac.pi4pokemon.databinding.ActivityQrCodeBinding
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.ScanMode
import com.google.zxing.BarcodeFormat

class QrCodeActivity : AppCompatActivity() {
    lateinit var binding: ActivityQrCodeBinding
    lateinit var leitorQr: CodeScanner
    var permissaoConcedida = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        verificarPermissaoCamera()
    }

    fun inicializarLeitorQrCode() {

        leitorQr = CodeScanner(this, binding.scannerView)

        leitorQr.camera = CodeScanner.CAMERA_BACK
        leitorQr.formats = listOf(BarcodeFormat.QR_CODE)
        leitorQr.isAutoFocusEnabled = true
        leitorQr.autoFocusMode = AutoFocusMode.SAFE
        leitorQr.isFlashEnabled = false
        leitorQr.scanMode = ScanMode.SINGLE

        leitorQr.setDecodeCallback {

            val respIntent = Intent()
            respIntent.putExtra("qrcode", it.text)
            setResult(RESULT_OK, respIntent)
            finish()
        }

        leitorQr.setErrorCallback {
            runOnUiThread {
                mostrarToast(this, "não foi possivel abrir câmera")
                Log.e("QrCodeActivity", "inicializarLeitorQrCode", it)
                setResult(RESULT_CANCELED)
                finish()
            }
        }


    }

    fun verificarPermissaoCamera() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
        } else {
            permissaoConcedida = true
            inicializarLeitorQrCode()

        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissaoConcedida = true
                inicializarLeitorQrCode()

            } else if (!shouldShowRequestPermissionRationale(permissions[0])) {
                mostrarDialogoPermissaoCamera()

            } else {

                permissaoConcedida = false
                mostrarToast(this, "Sem permissão de uso da câmera não é possivel ler QR Code")
                setResult(RESULT_CANCELED)
                finish()
            }


        }
    }

    override fun onResume() {
        super.onResume()
        if (permissaoConcedida) {
            leitorQr.startPreview()
        }
    }

    override fun onPause() {
        super.onPause()
        if (permissaoConcedida) {
            leitorQr.releaseResources()
        }
    }

    private fun mostrarDialogoPermissaoCamera() {
        AlertDialog.Builder(this)
            .setTitle("Permissão da Câmera")
            .setMessage("Habilite a permissão do uso da camera em configurações para ler qr code")
            .setCancelable(false)
            .setPositiveButton("Ir para Configurações") { dialogInterface, i ->
                val i = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                i.data = Uri.fromParts("package", packageName, null)
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