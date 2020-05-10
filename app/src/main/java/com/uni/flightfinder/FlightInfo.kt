package com.uni.flightfinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.uni.flightfinder.adaptors.FlightItem
import kotlinx.android.synthetic.main.activity_flight_info.*
import java.lang.Exception

class FlightInfo : AppCompatActivity() {
    lateinit var flight: FlightItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_info)

        flight = intent.getSerializableExtra("FlightListAdaptor") as? FlightItem ?: throw Exception("No flight passed")
        showFlightInfo()
    }

    // To be called by the onClick of a button in the layout
    fun generateQRCode(view: View) {
        val img : ImageView = findViewById(R.id.afi_imageView1)

        val context=img.context
        val intent = Intent(context, ShowQR::class.java)
        intent.putExtra("FlightInfo", flight)
        context.startActivity(intent)
    }

    fun showFlightInfo() {
        val nameData: TextView = findViewById(R.id.afi_flightNameData)
        nameData.text = flight.departureAirports
        // Fill outbound data
        val outTime1: TextView = findViewById(R.id.afi_outboundTime1Data)
        val outTime2: TextView = findViewById(R.id.afi_outboundTime2Data)
        val outTimeDiff: TextView = findViewById(R.id.afi_outboundTimeData)
        val outTimes = flight.departureTimes.replace(" ","").split("-")
        outTime1.text = outTimes[0]
        outTime2.text = outTimes[1]
        outTimeDiff.text = flight.departureTravelTime
        val outLoc1: TextView = findViewById(R.id.afi_outboundLoc1Data)
        val outLoc2: TextView = findViewById(R.id.afi_outboundLoc2Data)
        val outLocs = flight.departureAirports.replace(" ","").split("-")
        outLoc1.text = outLocs[0]
        outLoc2.text = outLocs[1]
        val outDirect: TextView = findViewById(R.id.afi_outboundDirectData)
        outDirect.text = flight.direct1

        // Fill return data
        val inTime1: TextView = findViewById(R.id.afi_returnTime1Data)
        val inTime2: TextView = findViewById(R.id.afi_returnTime2Data)
        val inTimeDiff: TextView = findViewById(R.id.afi_returnTimeData)
        val inTimes = flight.returnTimes.replace(" ","").split("-")
        inTime1.text = inTimes[0]
        inTime2.text = inTimes[1]
        inTimeDiff.text = flight.returnTravelTime
        val inLoc1: TextView = findViewById(R.id.afi_returnLoc1Data)
        val inLoc2: TextView = findViewById(R.id.afi_returnLoc2Data)
        val inLocs = flight.returnAirports.replace(" ","").split("-")
        inLoc1.text = inLocs[0]
        inLoc2.text = inLocs[1]
        val inDirect: TextView = findViewById(R.id.afi_returnDirectData)
        inDirect.text = flight.direct2

        // Fill in price data
        val price: TextView = findViewById(R.id.afi_returnPriceData)
        price.text = flight.cost
    }
}