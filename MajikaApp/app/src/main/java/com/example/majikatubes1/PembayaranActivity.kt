package com.example.majikatubes1

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.majikatubes1.data.pembayaran.PembayaranRepository
import com.example.majikatubes1.databinding.ActivityPembayaranBinding
import kotlinx.android.synthetic.main.activity_pembayaran.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import com.google.zxing.Result
import java.util.*
import kotlin.concurrent.schedule
import kotlin.concurrent.timerTask

class PembayaranActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private lateinit var binding: ActivityPembayaranBinding
    private var scannerViewer: ZXingScannerView? = null
    private var pembayaranRepository: PembayaranRepository? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPembayaranBinding.inflate(layoutInflater)

        initView()
        initScannerViewer()

        setContentView(binding.root)
    }

    private fun initView() {
        val totalBayarText  = binding.TotalBayarText
        val totalBayar      = intent.getStringExtra("totalBayar").toString()
        totalBayarText.text = "Total: $totalBayar"

        this.hideStatusPembayaran()
    }

    private fun showStatusPembayaran(success: Boolean) {
        if (success) {
            binding.GambarStatusPembayaran.setBackgroundResource(R.drawable.ok)
            binding.StatusPembayaranText.text           = "Berhasil"
            binding.StatusPembayaranText1.text          = "Sudah dibayar"

        } else {
            binding.GambarStatusPembayaran.setBackgroundResource(R.drawable.cancel)
            binding.StatusPembayaranText.text           = "Gagal"
            binding.StatusPembayaranText1.text          = "Belum dibayar"
        }
        binding.StatusPembayaran.visibility = View.VISIBLE;
    }

    private fun hideStatusPembayaran() {
        binding.StatusPembayaran.visibility = View.GONE;
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
        Log.v(TAG, result.text)
        var transactionId = result.text

        pembayaranRepository = PembayaranRepository()

        var statusPembayaran = pembayaranRepository!!.getStatusPembayaran(transactionId)
        var sudahBayar       = statusPembayaran.equals("SUCCESS")

        var timer = Timer()
        Log.v(TAG, statusPembayaran.value.toString())

        if (sudahBayar) {
            scannerViewer!!.stopCamera()
            showStatusPembayaran(sudahBayar)

            timer.schedule(5000) {
                moveToMainActivity()
            }

        } else {
            showStatusPembayaran(sudahBayar)
        }
    }

    private fun moveToMainActivity() {
        var mainActivityIntent = Intent(this, MainActivity::class.java)
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
