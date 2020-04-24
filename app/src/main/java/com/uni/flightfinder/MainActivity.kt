package com.uni.flightfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.uni.flightfinder.adaptors.Airport
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    val restServe by lazy{
        restAPI.create()
    }

    var FromList = mutableListOf("")
    var ToList = mutableListOf("")



    //var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FromList = mutableListOf("Pending...")
        ToList = mutableListOf("Pending...")


        val departureDate = findViewById<CalendarView>(R.id.departureCalendarView)
        val returnDate = findViewById<CalendarView>(R.id.returnCalendarView)
        var outboundDate:String
        var inboundDate:String

        updateFrom()
        updateTo()

        val btnAirport = findViewById<Button>(R.id.findAirportsBtn)
        var fromText=findViewById<EditText>(R.id.fromText)
        var toText=findViewById<EditText>(R.id.toText)



        btnAirport?.setOnClickListener { getAirports(fromText.text.toString(),toText.text.toString()) }
        departureDate?.setOnDateChangeListener { view,year,month,day ->
            outboundDate = ""+year+"-"+(month+1)+"-"+day
            println(outboundDate)
        }
        returnDate?.setOnDateChangeListener { view,year,month,day ->
            inboundDate = ""+year+"-"+(month+1)+"-"+day
            println(inboundDate)
        }




        //loop over x and put it in the spinner...?
        //set the values in the departure spinner to be the data from the request
        //for loop over each element? bufferedReader.forEachLine()?



    }
    private fun updateTo(){
        val toArrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ToList)
        toArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        destinationSpinner!!.setAdapter(toArrayAdapter)
    }

    private fun updateFrom(){
        val fromArrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, FromList)
        fromArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        departingSpinner!!.setAdapter(fromArrayAdapter)

    }


    private fun  getAirports(from:String,to:String){

        getAirports(from)

        var makerTo = restServe.getAirports(to)

        makerTo.enqueue(object: Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.isSuccessful){
                    val api: JsonObject? = response.body()

                    var jsonString:String=api.toString()

                    jsonString=jsonString.drop(10)
                    jsonString=jsonString.dropLast(1)

                    val gson=Gson()
                    val arrayAirport = object : TypeToken<Array<Airport>>() {}.type

                    val split:Array<Airport> = gson.fromJson(jsonString,arrayAirport)

                    var x:MutableList<String> = mutableListOf()

                    split.forEachIndexed{_,air->
                        x.add(air.PlaceName+","+air.CountryName+"("+air.CityId+")")
                    }

                    ToList=x
                    println("X:$x")
                    updateTo()
                }
                else{


                    Toast.makeText(this@MainActivity,response.body().toString(),Toast.LENGTH_SHORT).show()
                    Toast.makeText(this@MainActivity,response.message(),Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@MainActivity,"Api not responding",Toast.LENGTH_SHORT).show()
            }
        })
    }




    private fun getAirports(from:String){
        var makerFrom = restServe.getAirports(from) //needs to take data from the text box(es)
        makerFrom.enqueue(object: Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.isSuccessful){
                    val api: JsonObject? = response.body()

                    var jsonString:String=api.toString()

                    jsonString=jsonString.drop(10)
                    jsonString=jsonString.dropLast(1)
                    println("\nJSONSTRING:$jsonString")

                    val gson=Gson()
                    val arrayAirport = object : TypeToken<Array<Airport>>() {}.type

                    val split:Array<Airport> = gson.fromJson(jsonString,arrayAirport)

                    var x:MutableList<String> = mutableListOf()

                    split.forEachIndexed{idx,air->
                        x.add(air.PlaceName+","+air.CountryName+"("+air.CityId+")")
                    }

                    FromList=x
                    println("X:$x\nfromList:$FromList")
                    updateFrom()

                }
                else{


                    Toast.makeText(this@MainActivity,response.body().toString(),Toast.LENGTH_SHORT).show()
                    Toast.makeText(this@MainActivity,response.message(),Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@MainActivity,"Api not responding",Toast.LENGTH_SHORT).show()
            }
        })
    }
}


//data class Destinations(
//   val
//)