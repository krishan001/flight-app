package com.uni.flightfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.uni.flightfinder.adaptors.FlightItem
import java.lang.Exception

class ShowQR : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_qr)

        val flight = intent.getSerializableExtra("FlightInfo") as? FlightItem ?: throw Exception("No flight passed")

        val barcodeView = findViewById<ImageView>(R.id.genBarcode)

        val gson = Gson()
        val flightJSON = gson.toJson(flight)
        val encoder = BarcodeEncoder()
        val qrCode = encoder.encodeBitmap(flightJSON, BarcodeFormat.QR_CODE, 500, 500)

        //Set the QR code as the image
        barcodeView.setImageBitmap(qrCode)
    }
}
