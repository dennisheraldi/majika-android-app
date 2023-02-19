package com.example.majikatubes1.ui.pembayaran

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.majikatubes1.MainActivity
import com.example.majikatubes1.R
import com.example.majikatubes1.data.pembayaran.PembayaranStatus
import com.example.majikatubes1.databinding.ActivityPembayaranBinding
import com.example.majikatubes1.ui.keranjang.KeranjangFragment
import me.dm7.barcodescanner.zxing.ZXingScannerView
import com.google.zxing.Result
import java.util.*
import kotlin.concurrent.schedule

class PembayaranActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private lateinit var binding: ActivityPembayaranBinding
    private var scannerViewer: ZXingScannerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPembayaranBinding.inflate(layoutInflater)

        hideStatusPembayaran()
        initScannerViewer()
        initView()

        setContentView(binding.root)
    }

    private fun initView() {
        val totalBayarText  = binding.TotalBayarText
        val totalBayar      = intent.getStringExtra("totalBayar").toString()
        totalBayarText.text = "Total: $totalBayar"
    }

    private fun showStatusPembayaran(success: Boolean) {
        val gambarStatusPembayaran  = binding.GambarStatusPembayaran
        val statusPembayaranText    = binding.StatusPembayaranText
        val statusPembayaranText1   = binding.StatusPembayaranText1
        val statusPembayaran        = binding.StatusPembayaran

        if (success) {
            gambarStatusPembayaran.setBackgroundResource(R.drawable.ok)
            statusPembayaranText.text           = "Berhasil"
            statusPembayaranText1.text          = "Sudah dibayar"

        } else {
            gambarStatusPembayaran.setBackgroundResource(R.drawable.cancel)
            statusPembayaranText.text           = "Gagal"
            statusPembayaranText1.text          = "Belum dibayar"
        }
        statusPembayaran.visibility = View.VISIBLE;
    }

    private fun hideStatusPembayaran() {
        val statusPembayaran        = binding.StatusPembayaran
        statusPembayaran.visibility = View.GONE;
    }

    private fun initScannerViewer() {
        scannerViewer = ZXingScannerView(this)
        scannerViewer!!.setAutoFocus(true)
        scannerViewer!!.setResultHandler(this)
        binding.ScannerViewer.addView(scannerViewer)
    }

    override fun onStart() {
        super.onStart()
        doRequestPermission()
        scannerViewer!!.startCamera()
    }

    override fun onResume() {
        super.onResume()
        scannerViewer!!.startCamera()
    }

    override fun onPause() {
        super.onPause()
        scannerViewer!!.stopCamera()
    }

     override fun handleResult (result: Result) {
        var transactionId = result.text
        val pembayaranViewModel = PembayaranViewModel()

        pembayaranViewModel.getStatusPembayaran(transactionId)
        pembayaranViewModel.statusPembayaran.observe(this,  androidx.lifecycle.Observer { result ->
            var statusPembayaran = result.status
            var sudahBayar       = statusPembayaran === PembayaranStatus.SUCCESS

            var timer = Timer()

            if (sudahBayar) {
                scannerViewer!!.stopCamera()
                showStatusPembayaran(sudahBayar)

                timer.schedule(5000) {
                    moveToMainActivity()
                }

            } else {
                showStatusPembayaran(sudahBayar)
                scannerViewer?.resumeCameraPreview(this)
            }
        })
    }

    private fun moveToMainActivity() {
        var mainActivityIntent  = Intent(this, MainActivity::class.java)
        var keranjangFragment   = KeranjangFragment()
        var bundleArgument      = Bundle()

        bundleArgument.putString("statusPembayaran", PembayaranStatus.SUCCESS.toString())
        keranjangFragment.arguments = bundleArgument
        mainActivityIntent.putExtra("statusPembayaran", PembayaranStatus.SUCCESS)
        this.startActivity(mainActivityIntent)
    }

    private fun doRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode){
            100 -> {
                initScannerViewer()
            } else -> {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                this.finish()
            }
        }
    }

    companion object {
        private const val TAG = "majikatubes1"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = mutableListOf(
            Manifest.permission.CAMERA,
        ).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    }
}
