package com.example.majikatubes1.ui.pembayaran

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.majikatubes1.MainActivity
import com.example.majikatubes1.R
import com.example.majikatubes1.data.keranjang.KeranjangRepository
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
        if (requestCode == 100)
            initScannerViewer()
        else {
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
            var statusPembayaran = result.status
            Log.v(TAG, statusPembayaran.toString())
            var sudahBayar       = statusPembayaran == PembayaranStatus.SUCCESS

            if (sudahBayar) {
                scannerViewer!!.stopCamera()
                showStatusPembayaran(sudahBayar)

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
            } else {
                showStatusPembayaran(sudahBayar)
                scannerViewer?.resumeCameraPreview(this)
            }
        })
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

    private fun moveToMainActivity() {
        var mainActivityIntent  = Intent(this, MainActivity::class.java)
        this.startActivity(mainActivityIntent)
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
