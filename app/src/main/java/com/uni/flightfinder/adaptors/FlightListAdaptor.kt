package com.uni.flightfinder.adaptors


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uni.flightfinder.FlightInfo
import com.uni.flightfinder.ListFlights
import com.uni.flightfinder.R
import com.uni.flightfinder.ShowQR
import kotlinx.android.synthetic.main.activity_flight_info.view.*
import kotlinx.android.synthetic.main.flight_item.view.*

class FlightListAdaptor(private val flightList: List<FlightItem>) : RecyclerView.Adapter<FlightListAdaptor.ExampleViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.flight_item,
            parent,false)

        return ExampleViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
        val currentItem = flightList[position]

        //holder.imageView.setImageResource(currentItem.imageResource)
        holder.flightTime1.text = currentItem.departureTimes
        holder.airportID1.text = currentItem.departureAirports
        holder.direct1.text = currentItem.direct1
        holder.travelTime1.text = currentItem.departureTravelTime
        holder.flightTime2.text = currentItem.returnTimes
        holder.airportID2.text = currentItem.returnAirports
        holder.direct2.text = currentItem.direct2
        holder.travelTime2.text = currentItem.returnTravelTime
        holder.cost.text = currentItem.cost

        holder.itemView.setOnClickListener{
            println(currentItem)
            val context=holder.cost.context
            val intent = Intent(context, FlightInfo::class.java)
            intent.putExtra("FlightListAdaptor", currentItem)
            context.startActivity(intent)

            // BEN WHEN YOU WANT TO RECIEVE THE DATA JUST PUT THIS IN YOUR ON CREATE FUNCTION
            // val flightInfo = intent.getSerializableExtra("FlightListAdaptor") as? FlightItem
        }

    }

    override fun getItemCount() = flightList.size



    class ExampleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        //val imageView :ImageView = itemView.findViewById(R.id.image_view)
        val flightTime1: TextView = itemView.flight_time_view_1
        val airportID1: TextView = itemView.airportsID_view_1
        val travelTime1: TextView = itemView.travel_time_view_1
        val direct1: TextView = itemView.direct_view_1
        val flightTime2: TextView = itemView.flight_time_view_2
        val airportID2: TextView = itemView.airportsID_view_2
        val travelTime2: TextView = itemView.travel_time_view_2
        val direct2: TextView = itemView.direct_view_2
        val cost: TextView = itemView.price_view


    }
}