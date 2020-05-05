package com.uni.flightfinder

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.uni.flightfinder.adaptors.*
import kotlinx.android.synthetic.main.activity_list_flights.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.uni.flightfinder.adaptors.RawFlightItem



class ListFlights : AppCompatActivity() {
    var toSend:RawFlightItem?=null
    private val restServe by lazy{
        restAPI.create()
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_flights)
        val toProcess = intent.getSerializableExtra("com.uni.MainActivity") as ArrayList<String>
        println("TO PROCESS $toProcess")
        getQuotes(toProcess[0], toProcess[1], toProcess[2], toProcess[3])

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





    private fun getQuotes(sendDepart: String, sendDestination: String, outboundDate: String, inboundDate: String ){


        //swap outbound and inbound date to be from the intent.
        var maker = restServe.getQuotes(sendDepart,sendDestination,outboundDate,inboundDate) //needs to take data from the text box(es)

        maker.enqueue(object: Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val api: JsonObject? = response.body()

                    var jsonString: String = api.toString()

                    val gson = Gson()
                    val token = object : TypeToken<RawFlightItem>() {}.type

                    //this is to become the parseable variable
                    toSend = gson.fromJson(jsonString, token)
                    println("TO SEND $toSend")

                    val flightList = getFlightList(toSend)
                    val exampleList = generateDummyList(100)
                    recycler_view.adapter = FlightListAdaptor(flightList)
                    recycler_view.layoutManager = LinearLayoutManager(this@ListFlights)
                    recycler_view.setHasFixedSize(true)
                }
                else{

                    Toast.makeText(this@ListFlights,response.body().toString(), Toast.LENGTH_SHORT).show()
                    Toast.makeText(this@ListFlights,response.message(), Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@ListFlights,"Api not responding", Toast.LENGTH_SHORT).show()
            }
        })

        println("RETURN $toSend")

    }
}