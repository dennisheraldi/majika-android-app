package com.example.majikatubes1.ui.pembayaran

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.majikatubes1.MainActivity
import com.example.majikatubes1.R
import com.example.majikatubes1.data.keranjang.KeranjangRepository
import com.example.majikatubes1.data.pembayaran.PembayaranStatus
import com.example.majikatubes1.databinding.ActivityPembayaranBinding
import me.dm7.barcodescanner.zxing.ZXingScannerView
import com.google.zxing.Result

class PembayaranActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private lateinit var binding: ActivityPembayaranBinding
    private var scannerViewer: ZXingScannerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPembayaranBinding.inflate(layoutInflater)

        initScannerViewer()
        initView()

        setContentView(binding.root)
    }

    private fun initView() {
        val totalBayarText  = binding.TotalBayarText
        val totalBayar      = intent.getStringExtra("totalBayar").toString()
        val actionBar       = supportActionBar

        actionBar?.setDisplayHomeAsUpEnabled(true)

        title               = "Pembayaran"
        totalBayarText.text = "Total: $totalBayar"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            this.finish()
        }
        return super.onOptionsItemSelected(item)
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

    override fun onPause() {
        super.onPause()
        scannerViewer!!.stopCamera()
    }

    private fun doRequestPermission() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            initScannerViewer()
            scannerViewer!!.startCamera()
        } else {
            Toast.makeText(this,
                "Permissions not granted by the user.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    override fun handleResult (result: Result) {
        val transactionId = result.text
        val pembayaranViewModel = PembayaranViewModel()

        pembayaranViewModel.getStatusPembayaran(transactionId)
        pembayaranViewModel.statusPembayaran.observe(this,  androidx.lifecycle.Observer { result ->
            if (result != null) {
                var statusPembayaran = result.status

                showStatusPembayaran(statusPembayaran)

                if (statusPembayaran == PembayaranStatus.SUCCESS) {
                    scannerViewer!!.stopCamera()

                    val keranjangRepository = KeranjangRepository(this)
                    keranjangRepository.deleteAllKeranjang()

                    val countDown           = binding.countDown
                    countDown.visibility    = View.VISIBLE

                    object : CountDownTimer(6000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            countDown.text = "${millisUntilFinished/1000} second(s) to main"
                        }

                        override fun onFinish() {
                            moveToMainActivity()
                        }
                    }.start()
                }
            }
        })
        scannerViewer?.resumeCameraPreview(this)
    }

    private fun showStatusPembayaran(status: PembayaranStatus) {
        val gambarStatusPembayaran  = binding.GambarStatusPembayaran
        val statusPembayaranText    = binding.StatusPembayaranText
        val statusPembayaranText1   = binding.StatusPembayaranText1
        val statusPembayaran        = binding.StatusPembayaran

        if (status == PembayaranStatus.SUCCESS) {
            gambarStatusPembayaran.setBackgroundResource(R.drawable.ok)
            statusPembayaranText.text           = "Berhasil"
            statusPembayaranText1.text          = "Sudah dibayar"

        } else {
            gambarStatusPembayaran.setBackgroundResource(R.drawable.cancel)
            statusPembayaranText.text           = "Gagal"
            statusPembayaranText1.text          = if (status == PembayaranStatus.FAILED) "Belum dibayar" else "QR kedaluwarsa"
        }
        statusPembayaran.visibility = View.VISIBLE;
    }

    private fun moveToMainActivity() {
        var mainActivityIntent  = Intent(this, MainActivity::class.java)
        this.startActivity(mainActivityIntent)
    }

    companion object {
        private const val TAG = "majikatubes1"
        private const val REQUEST_CODE_PERMISSIONS = 100
        private val REQUIRED_PERMISSIONS = mutableListOf(
            Manifest.permission.CAMERA,
        ).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    }
}
