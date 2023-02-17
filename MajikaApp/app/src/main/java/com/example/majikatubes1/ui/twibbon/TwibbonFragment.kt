package com.example.majikatubes1.ui.twibbon

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.majikatubes1.databinding.FragmentTwibbonBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class TwibbonFragment : Fragment() {

    private var _binding: FragmentTwibbonBinding? = null
    private val binding get() = _binding!!

    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null

    private var preview: Preview? = null
    private var isPreviewFrozen = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTwibbonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()

        startCamera()

        binding.imageCaptureButton.setOnClickListener {
            freezePreview()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            val imageCaptureBuilder = ImageCapture.Builder()

            imageCapture = imageCaptureBuilder
                .build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner, cameraSelector, preview, imageCapture
                )
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun freezePreview() {
        if (!isPreviewFrozen) {
            // Unbind the preview use case to freeze the camera preview
            val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                cameraProvider.unbind(preview)
                isPreviewFrozen = true
            }, ContextCompat.getMainExecutor(requireContext()))
        } else {
            // rebind the preview use case to resume the camera preview
            val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview
                )
                isPreviewFrozen = false
            }, ContextCompat.getMainExecutor(requireContext()))
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
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