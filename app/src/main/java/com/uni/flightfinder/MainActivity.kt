package com.uni.flightfinder

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.uni.flightfinder.adaptors.Airport
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.util.*


class MainActivity : AppCompatActivity()  {

    //tells intent where request has come from
    val EXTRA_MESSAGE = "com.uni.MainActivity"

    //intent will be given data when it's required
    lateinit var nextPageIntent: Intent

    //instance of the restApi class
    val restServe by lazy {
        restAPI.create()
    }

    //used for the location spinners
    var FromList = mutableListOf("")
    var ToList = mutableListOf("")

    //datestrings
    var outboundDate: String = ""
    var inboundDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FromList = mutableListOf("Pending...")
        ToList = mutableListOf("Pending...")

        updateFrom()
        updateTo()

        val btnAirport = findViewById<Button>(R.id.findAirportsBtn)
        var fromText = findViewById<EditText>(R.id.fromText)
        var toText = findViewById<EditText>(R.id.toText)

        /*
        Scan QR code section

         */

        val scanQr = findViewById<Button>(R.id.barcodeButton)

        scanQr.setOnClickListener {
            val scanQRIntent = Intent(this, ScanQR::class.java)
            startActivity(scanQRIntent)
        }

        /*
        DEPARTURE/RETURN DATE

        - Set onClick for Containers
        - Load a date picker dialog if the container is clicked
        - Set the date variables when the date picker is updated

        //Added
        - Date converter

         */

        val depDate = findViewById<TextView>(R.id.DepDate)
        val returnDate = findViewById<TextView>(R.id.ReturnDate)
        val depDay = findViewById<TextView>(R.id.DepDay)
        val returnDay = findViewById<TextView>(R.id.ReturnDay)

        val departureContainer = findViewById<LinearLayout>(R.id.DepDateClick)
        var depDateSetListener: OnDateSetListener

        //Set ready to be changed by the date picker
        var departureCal = Calendar.getInstance()
        var returnCal = Calendar.getInstance()

        val returnContainer = findViewById<LinearLayout>(R.id.ReturnDateClick)
        var returnDateSetListener: OnDateSetListener

        depDateSetListener =
            OnDateSetListener { datePicker, year, month, day ->
                //Set selected date to calendar
                val cal = Calendar.getInstance()
                val locale = Locale.getDefault()
                cal.set(year,month,day)

                //set departure date
                departureCal.set(year,month,day)

                if (!inboundDate.equals("")){
                    if (returnCal.timeInMillis < departureCal.timeInMillis) {
                        inboundDate = ""
                        returnDate.setText("Unselected")
                        returnDay.setText("")

                    }
                }

                //Update UI objects and variables
                val dayOfWeek =
                    cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, locale)
                val monthOfYear =
                    cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, locale)

                var monthString = convertDate(month+1)
                var dayString = convertDate(day)

                depDate.setText("$dayString $monthOfYear")
                depDay.setText("$dayOfWeek $year")

                outboundDate = "$year-$monthString-$dayString"
            }

        departureContainer.setOnClickListener {
            val cal: Calendar = Calendar.getInstance()
            val year: Int = cal.get(Calendar.YEAR)
            val month: Int = cal.get(Calendar.MONTH)
            val day: Int = cal.get(Calendar.DAY_OF_MONTH)

            val dialog = DatePickerDialog(
                this,
                R.style.CalendarDatePickerDialog,
                depDateSetListener,
                year, month, day
            )
            dialog.datePicker.minDate = System.currentTimeMillis() - 1000
            dialog.show()
        }


        returnDateSetListener =
            OnDateSetListener { datePicker, year, month, day ->
                //Set selected date to calendar
                val cal = Calendar.getInstance()
                val locale = Locale.getDefault()
                cal.set(year,month,day)
                returnCal.set(year,month,day)

                //set departure date
                val dayOfWeek =
                    cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, locale)
                val monthOfYear =
                    cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, locale)

                //Update UI objects and variables
                var monthString = convertDate(month)
                var dayString = convertDate(day)

                returnDate.setText("$dayString $monthOfYear")
                returnDay.setText("$dayOfWeek $year")

                inboundDate = "$year-$monthString-$dayString"

            }


        returnContainer.setOnClickListener {
            if (outboundDate.equals("")) {
                Toast.makeText(this@MainActivity, "Please set departure date.", Toast.LENGTH_SHORT).show()
            }
            else {
                val cal: Calendar = Calendar.getInstance()
                val year: Int = cal.get(Calendar.YEAR)
                val month: Int = cal.get(Calendar.MONTH)
                val day: Int = cal.get(Calendar.DAY_OF_MONTH)

                val dialog = DatePickerDialog(
                    this,
                    R.style.CalendarDatePickerDialog,
                    returnDateSetListener,
                    year, month, day
                )
                dialog.datePicker.minDate = departureCal.timeInMillis
                dialog.show()
            }
        }



        //sets button listener.
        btnAirport?.setOnClickListener {
            getAirports(
                fromText.text.toString(),
                toText.text.toString()
            )
        }


        //sets button listener and sends user to next page if validation permits.
        destinationBtn?.setOnClickListener {
            nextPageIntent = Intent(this, ListFlights::class.java)
            var doIt = false

            var sendDepart = departingSpinner.selectedItem.toString()

            //ensures the selected airport is not default
            if ((("(")in sendDepart) && ((")") in sendDepart)){
                sendDepart = sendDepart.split("(")[1].split(")")[0] + "-sky"
                doIt = true
            } else {
                doIt = false
            }


            var sendDestination = destinationSpinner.selectedItem.toString()
            //ensures that default airport not selected in this spinner or previous one.
            if ((("(")in sendDestination) && ((")") in sendDestination)){
                sendDestination = sendDestination.split("(")[1].split(")")[0] + "-sky"
                if (doIt){
                    doIt = true
                }
            } else {
                doIt = false
            }

            if (outboundDate == "" || inboundDate == ""){
                doIt = false
            }
            if (doIt) {
                var toSend = arrayListOf(sendDepart, sendDestination, outboundDate, inboundDate)

                nextPageIntent.putExtra(EXTRA_MESSAGE, toSend)
                startActivity(nextPageIntent)
            }
            else {
                Toast.makeText(this@MainActivity, "Please choose valid airports and valid dates", Toast.LENGTH_SHORT).show()

            }
        }



    }

    //Converts single value dates to double value dates
    private fun convertDate(input: Int): String? {
        return if (input >= 10) {
            input.toString()
        } else {
            "0$input"
        }
    }


    //updates the arrays and spinners for the available airports.
    private fun updateTo() {
        var destinationSpinner: Spinner = findViewById(R.id.destinationSpinner)
        val toArrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ToList)
        toArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        destinationSpinner.setAdapter(toArrayAdapter)
        /*
        create listener for updating airport choice

        - Get the item that was clicked
        - Update the textViews that show the user what choice they made
         */

        var destListener:AdapterView.OnItemSelectedListener = object:
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(parent:AdapterView<*>, view:View, position:Int, id:Long) {
                    (destinationSpinner.getSelectedView() as TextView).setText("")

                    val toCodeText = findViewById<TextView>(R.id.ToCodeText)
                    val toLocation = findViewById<TextView>(R.id.ToLocation)

                    var item = destinationSpinner.selectedItem.toString()
                    if (item.equals("Pending...")) {
                        toCodeText.setText("Pending")
                        toLocation.setText("")
                    }
                    else{
                        var selected = destinationSpinner.selectedItem.toString()
                        toCodeText.setText(selected.split("(")[1].split(")")[0])
                        toLocation.setText(selected.split(",")[0])

                    }
                }
            }

        destinationSpinner.onItemSelectedListener = destListener


    }


    //updates the arrays and spinners for the available airports.
    private fun updateFrom() {
        var departingSpinner: Spinner = findViewById(R.id.departingSpinner)
        val fromArrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, FromList)
        fromArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        departingSpinner.setAdapter(fromArrayAdapter)

        /*
        create listener for updating airport choice

        - Get the item that was clicked
        - Update the textViews that show the user what choice they made
         */

        var depListener:AdapterView.OnItemSelectedListener = object:
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(parent:AdapterView<*>, view:View, position:Int, id:Long) {
                (departingSpinner.getSelectedView() as TextView).setText("")
                val fromCodeText = findViewById<TextView>(R.id.FromCodeText)
                val fromLocation = findViewById<TextView>(R.id.FromLocation)

                var item = departingSpinner.selectedItem.toString()
                if (item.equals("Pending...")) {
                    fromCodeText.setText("Pending")
                    fromLocation.setText("")
                }
                else{
                    var selected = departingSpinner.selectedItem.toString()
                    fromCodeText.setText(selected.split("(")[1].split(")")[0])
                    fromLocation.setText(selected.split(",")[0])

                }

            }
        }

        departingSpinner.onItemSelectedListener = depListener

    }


    //gets airports using from and to strings.
    /*
    asynchronous requests to the api as completed by the restAPI.kt
    onFailure called if bad request or api is down.
    onRequest can still be unsuccessful if is cast to wrong data type.
     */

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

                    Toast.makeText(this@MainActivity, "Enter a location", Toast.LENGTH_SHORT).show()

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
                    Toast.makeText(this@MainActivity, "Enter a location", Toast.LENGTH_SHORT).show()

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