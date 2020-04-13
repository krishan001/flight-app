package com.uni.flightfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

class FlightInfo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_info)
    }

    // To be called by the onClick of a button in the layout
    fun generateQRCode(view: View) {
        val flightString = "this is a longer test string"
        val encoder = BarcodeEncoder()
        val qrCode = encoder.encodeBitmap(flightString, BarcodeFormat.QR_CODE, 500, 500)

    }
}