package com.uni.flightfinder.adaptors

data class FlightItem(val imageResource: Int,
                      val departureTimes: String,
                      val departureAirports:String,
                      val direct1: String,
                      val departureTravelTime:String,

                      val returnTimes: String,
                      val returnAirports:String,
                      val direct2: String,
                      val returnTravelTime:String,
                      val cost:String)