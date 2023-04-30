package com.andi.epatrol.ui.scan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.andi.epatrol.MainActivity
import com.andi.epatrol.MainActivity.Companion.uidUser
import com.andi.epatrol.data.Absen
import com.andi.epatrol.databinding.ActivityScanBinding
import com.andi.epatrol.util.DateHelper
import com.budiyev.android.codescanner.*
import com.google.firebase.database.FirebaseDatabase

class ScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanBinding
    private lateinit var codeScanner:CodeScanner
    private lateinit var scannerView:CodeScannerView
    private var lokasi:String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        //scanner
        scannerView =binding.scanView
        codeScanner = CodeScanner(this, scannerView)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),123)
        }else{
            startScanning()
        }

        binding.btnKonfirmScan.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }

        if (lokasi==null){
            binding.btnKonfirmScan.isEnabled = false
        }



    }

    private fun startScanning() {
        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                Toast.makeText(this, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
                lokasi = it.text
                binding.tvLokasiScan.text = lokasi
                binding.tvJamScan.text = DateHelper.getCurrentDateHour()
                binding.btnKonfirmScan.isEnabled = true

                val ref = FirebaseDatabase.getInstance().getReference("absen")
                val absenId:String? = ref.push().key
                val absen = Absen(absenId,uidUser,lokasi,DateHelper.getCurrentDateFull())
                if (absenId!=null){
                    ref.child(absenId).setValue(absen).addOnCompleteListener {
                        Toast.makeText(this, "Data Saved ",Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==123){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Camera Permission Granted", Toast.LENGTH_LONG).show()
                startScanning()
            }else{
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_LONG).show()
            }
        }
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