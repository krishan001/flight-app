package com.uni.flightfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.uni.flightfinder.adaptors.FlightItem
import com.uni.flightfinder.adaptors.FlightListAdaptor
import kotlinx.android.synthetic.main.activity_list_flights.*

class ListFlights : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_flights)
        val exampleList = generateDummyList(100)
        recycler_view.adapter = FlightListAdaptor(exampleList)
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
}