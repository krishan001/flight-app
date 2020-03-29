package com.uni.flightfinder

import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.samples.vision.barcodereader.BarcodeCapture
import com.google.android.gms.samples.vision.barcodereader.BarcodeGraphic
import com.google.android.gms.vision.barcode.Barcode
import xyz.belvi.mobilevisionbarcodescanner.BarcodeRetriever


class ScanQR : AppCompatActivity(), BarcodeRetriever {
    //private val barcodeCapture =
    //    supportFragmentManager.findFragmentById(R.id.barcode) as BarcodeCapture?

    private var barcodeCapture : BarcodeCapture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_barcode)


        barcodeCapture = supportFragmentManager.findFragmentById(R.id.barcode) as BarcodeCapture?

        

        barcodeCapture!!.setRetrieval(this)

    }

    override fun onRetrieved(barcode: Barcode?) {

        runOnUiThread(Runnable { Toast.makeText(applicationContext, barcode?.displayValue, Toast.LENGTH_SHORT).show() })
    }

    override fun onRetrievedMultiple(closetToClick: Barcode?, barcode: MutableList<BarcodeGraphic>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBitmapScanned(sparseArray: SparseArray<Barcode>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRetrievedFailed(reason: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPermissionRequestDenied() {
        runOnUiThread(
            object : Runnable {
                override fun run() {
                    println("Hi")
                }
            }
        )
    }
}
