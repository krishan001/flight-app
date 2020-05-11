package com.uni.flightfinder.adaptors


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uni.flightfinder.FlightInfo
import com.uni.flightfinder.R
import kotlinx.android.synthetic.main.flight_item.view.*

class FlightListAdaptor(private val flightList: List<FlightItem>) : RecyclerView.Adapter<FlightListAdaptor.FlightViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.flight_item,
            parent,false)

        return FlightViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: FlightViewHolder, position: Int) {
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
            val context=holder.cost.context
            val intent = Intent(context, FlightInfo::class.java)
            intent.putExtra("FlightListAdaptor", currentItem)
            context.startActivity(intent)
        }

    }

    override fun getItemCount() = flightList.size


    class FlightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
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