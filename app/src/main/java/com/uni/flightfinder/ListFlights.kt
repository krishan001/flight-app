package com.uni.flightfinder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.uni.flightfinder.adaptors.*
import kotlinx.android.synthetic.main.activity_list_flights.*


class ListFlights : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_flights)
        val quotes = intent.getSerializableExtra("com.uni.MainActivity") as? RawFlightItem
        println(quotes.toString())
        val exampleList = generateDummyList(100)
        val flightList = getFlightList(quotes)
        recycler_view.adapter = FlightListAdaptor(flightList)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)
    }

    private fun generateDummyList(size:Int): List<FlightItem>{
        val list = ArrayList<FlightItem>()
        for (i in 0 until size){
            val drawable = R.drawable.ic_airplane
            val item = FlightItem(drawable, "07:45 - 09:45", "MAD-BCN", "Direct", "2h 28m",
                "21:30 - 22:50", "BCN - MAD", "Direct", "2h 25m", "£98")
            list += item
        }
        return list
    }
    private fun getFlightList(item:RawFlightItem?):List<FlightItem>{
        val list = ArrayList<FlightItem>()
        var direct:String
        val drawable = R.drawable.ic_airplane
        if (item != null) {
            for (i in item.Quotes.indices){
                if(item.Quotes[i].Direct){
                    direct = "Direct"
                } else{
                    direct = "Changes"
                }
                val cost = item.Quotes[i].MinPrice.toString()
                val outbound = getOutboundAirports(item.Quotes[i], item.Places)
                val inbound = getInboundAirports(item.Quotes[i], item.Places)
                val departureAirportCodes = "$outbound - $inbound"
                val returnAirportCodes = "$inbound - $outbound"
                val flight = FlightItem(drawable,"07:45 - 09:45", departureAirportCodes, direct, "2h 28m",
                    "21:30 - 22:50", returnAirportCodes, direct, "2h 25m", cost )
                list += flight

            }
        }

        return list
    }

    private fun getOutboundAirports(quotes:RawQuote, places:List<RawPlaces>): String{
        var code = ""
        val id = quotes.OutboundLeg.OriginId
        for (i in places.indices){
            if(places[i].PlaceId == id){
                code = places[i].IataCode
            }
        }
        return code
    }

    private fun getInboundAirports(quotes:RawQuote, places:List<RawPlaces>): String{
        var code = ""
        val id = quotes.OutboundLeg.DestinationId
        for (i in places.indices){
            if(places[i].PlaceId == id){
                code = places[i].IataCode
            }
        }
        return code
    }
}