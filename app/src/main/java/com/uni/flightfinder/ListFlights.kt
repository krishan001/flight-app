package com.uni.flightfinder

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
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

    /*
    * On create this activity will get the data from the previous activity
    * and pass it to the relevant functions to process it
    * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_flights)
        val toProcess = intent.getSerializableExtra("com.uni.MainActivity") as ArrayList<String>
        println("TOPROCESS $toProcess")
        getQuotes(toProcess[0], toProcess[1], toProcess[2], toProcess[3])

    }
    /*
    * This function takes the raw data from the API and formats it
    * in a way that the rest of the program can interact with
    * */
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
                    "21:30 - 22:50", returnAirportCodes, direct, "2h 25m", "£$cost" )
                list += flight

            }
        }

        return list
    }

    /*
    * Parses the data in order to get the outbound airport codes
    * */
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
    /*
    * Parses the data in order to get the inbound airport codes
    * */
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



    /*
    * Makes the API call to get the quotes for the flight information
    * and calls the subsequent functions
    * */

    private fun getQuotes(sendDepart: String, sendDestination: String, outboundDate: String, inboundDate: String ){

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

                    val flightList = getFlightList(toSend)
                    println("FLIGHT LIST $flightList")
                    if (flightList.isEmpty()){
                        val intent = Intent(this@ListFlights, MainActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(this@ListFlights,"There are no flights available", Toast.LENGTH_SHORT).show()
                    }
                    recycler_view.adapter = FlightListAdaptor(flightList)
                    recycler_view.layoutManager = LinearLayoutManager(this@ListFlights)
                    recycler_view.setHasFixedSize(true)
                    val title: TextView = findViewById<TextView>(R.id.title)
                    title.text = flightList[0].departureAirports
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

    }
}