package com.uni.flightfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

class ShowQR : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_qr)

        val barcodeView = findViewById<ImageView>(R.id.genBarcode)

        //Generate a QR code from string using encoder
        val encoder = BarcodeEncoder()
        val bitmap = encoder.encodeBitmap("test", BarcodeFormat.QR_CODE, 500, 500)

        //Set the QR code as the image
        barcodeView.setImageBitmap(bitmap)
    }
}
