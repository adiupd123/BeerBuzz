package com.adiupd123.beerbuzz.ui.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.adiupd123.beerbuzz.R
import com.adiupd123.beerbuzz.databinding.FragmentQRScannerBinding
import com.adiupd123.beerbuzz.utils.NetworkResult
import com.adiupd123.beerbuzz.viewmodels.QRScannerViewModel
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QRScannerFragment : Fragment() {
    private var _binding: FragmentQRScannerBinding? = null
    private val binding
        get() = _binding!!
    private val qrScannerViewModel by viewModels<QRScannerViewModel>()
    private lateinit var codeScanner: CodeScanner
    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            // permission granted, continue the normal workflow of the app
            if (!hasCameraPermissions()) {
                // If the permission was previously not granted, show a Toast message
                Toast.makeText(context, "${Manifest.permission.CAMERA} permission granted", Toast.LENGTH_SHORT).show()
            }
        } else {
            // If the permission was denied, check whether never ask again is selected or not
            if (!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.CAMERA)) {
                // If never ask again was selected, show a dialog explaining how to grant the permission manually
                AlertDialog.Builder(requireContext())
                    .setMessage("Please grant the camera permission in the app settings to use this feature.")
                    .setPositiveButton("Open Settings") { _, _ ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", requireActivity().packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }
                    .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                    .show()
            } else {
                // If the permission was denied, but not permanently, show a Toast message
                Toast.makeText(context, "${Manifest.permission.CAMERA} permission denied", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQRScannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermission.launch(Manifest.permission.CAMERA)
        bindObservers()
        val activity = requireActivity()
        codeScanner = CodeScanner(activity, binding.scannerView)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = listOf(BarcodeFormat.QR_CODE)  // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.CONTINUOUS // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false
        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                Toast.makeText(activity, it.text, Toast.LENGTH_LONG).show()
                qrScannerViewModel.getScannedBeer(it.text.toInt())
                return@runOnUiThread
            }
        }
        binding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }
    private fun hasCameraPermissions() =
        ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    private fun bindObservers() {
        qrScannerViewModel.scannedBeerLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Success -> {
                    val bundle = Bundle()
                    val jsonBeerItem = Gson().toJson(it.data?.get(0))
                    bundle.putString("beerItem", jsonBeerItem)
                    findNavController().navigate(
                        R.id.action_QRScannerFragment_to_beerItemFragment,
                        bundle
                    )
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
                is NetworkResult.Loading -> {}
            }
        })
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

}
