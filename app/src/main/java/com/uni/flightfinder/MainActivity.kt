package com.uni.flightfinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.os.bundleOf
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.uni.flightfinder.adaptors.Airport
import com.uni.flightfinder.adaptors.RawFlightItem
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class MainActivity : AppCompatActivity() {

    val EXTRA_MESSAGE = "com.uni.MainActivity"
    lateinit var nextPageIntent: Intent
    val restServe by lazy {
        restAPI.create()
    }
    var FromList = mutableListOf("")
    var ToList = mutableListOf("")
    var outboundDate: String = ""
    var inboundDate: String = ""
    //var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FromList = mutableListOf("Pending...")
        ToList = mutableListOf("Pending...")


        val departureDate = findViewById<CalendarView>(R.id.departureCalendarView)
        val returnDate = findViewById<CalendarView>(R.id.returnCalendarView)


        updateFrom()
        updateTo()

        val btnAirport = findViewById<Button>(R.id.findAirportsBtn)
        var fromText = findViewById<EditText>(R.id.fromText)
        var toText = findViewById<EditText>(R.id.toText)


        //sets button listener.
        btnAirport?.setOnClickListener {
            getAirports(
                fromText.text.toString(),
                toText.text.toString()
            )
        }

        //sets button listener.
        destinationBtn?.setOnClickListener {
            nextPageIntent = Intent(this, ListFlights::class.java)
            var sendDepart =
                departingSpinner.selectedItem.toString().split("(")[1].split(")")[0] + "-sky"
            var sendDestination =
                destinationSpinner.selectedItem.toString().split("(")[1].split(")")[0] + "-sky"
            var toSend = arrayOf(sendDepart, sendDestination, outboundDate, inboundDate)
            println("GOOD TO GO!")
            nextPageIntent.putExtra(EXTRA_MESSAGE, toSend)
            startActivity(nextPageIntent)
        }

        //updates callendar value
        departureDate?.setOnDateChangeListener { _, year, month, day ->
            var monthString = ""
            var dayString = ""
            if (month + 1 < 10) {
                monthString = "0" + (month + 1).toString()
            } else {
                monthString = (month + 1).toString()
            }
            if (day < 10) {
                dayString = "0" + (day).toString()
            }
            outboundDate = "$year-$monthString-$dayString"
            //println(outboundDate)

        }

        //updates callendar value
        returnDate?.setOnDateChangeListener { _, year, month, day ->
            var monthString = ""
            var dayString = ""
            if (month + 1 < 10) {
                monthString = "0" + (month + 1).toString()
            } else {
                monthString = (month + 1).toString()
            }
            if (day < 10) {
                dayString = "0" + (day).toString()
            }
            inboundDate = "$year-$monthString-$dayString"
            //println(inboundDate)
        }


    }

    //updates the arrays and spinners for the available airports.
    private fun updateTo() {
        val toArrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ToList)
        toArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        destinationSpinner!!.adapter = toArrayAdapter
    }

    //updates the arrays and spinners for the available airports.
    private fun updateFrom() {
        val fromArrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, FromList)
        fromArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        departingSpinner!!.adapter = fromArrayAdapter

    }


    //gets airports using from and to strings.
    private fun getAirports(from: String, to: String) {

        getAirports(from)

        var makerTo = restServe.getAirports(to)

        makerTo.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.isSuccessful) {
                    val api: JsonObject? = response.body()

                    var jsonString: String = api.toString()

                    jsonString = jsonString.drop(10)
                    jsonString = jsonString.dropLast(1)

                    val gson = Gson()
                    val arrayAirport = object : TypeToken<Array<Airport>>() {}.type

                    val split: Array<Airport> = gson.fromJson(jsonString, arrayAirport)

                    var x: MutableList<String> = mutableListOf()

                    split.forEachIndexed { _, air ->
                        x.add(air.PlaceName + ", " + air.CountryName + "(" + air.PlaceId.split("-")[0] + ")")
                    }

                    ToList = x
                    updateTo()
                } else {


                    Toast.makeText(
                        this@MainActivity,
                        response.body().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    Toast.makeText(this@MainActivity, response.message(), Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Api not responding", Toast.LENGTH_SHORT).show()
            }
        })
    }

    //gets airports using the from string.
    private fun getAirports(from: String) {
        var makerFrom = restServe.getAirports(from) //needs to take data from the text box(es)
        makerFrom.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.isSuccessful) {
                    val api: JsonObject? = response.body()

                    var jsonString: String = api.toString()

                    jsonString = jsonString.drop(10)
                    jsonString = jsonString.dropLast(1)

                    val gson = Gson()
                    val arrayAirport = object : TypeToken<Array<Airport>>() {}.type

                    val split: Array<Airport> = gson.fromJson(jsonString, arrayAirport)

                    var x: MutableList<String> = mutableListOf()

                    split.forEachIndexed { _, air ->
                        x.add(air.PlaceName + ", " + air.CountryName + "(" + air.PlaceId.split("-")[0] + ")")
                    }

                    FromList = x
                    updateFrom()

                } else {


                    Toast.makeText(
                        this@MainActivity,
                        response.body().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    Toast.makeText(this@MainActivity, response.message(), Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Api not responding", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

//data class Destinations(
//   val
//)