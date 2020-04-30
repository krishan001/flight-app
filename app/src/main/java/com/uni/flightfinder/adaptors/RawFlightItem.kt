package com.uni.flightfinder.adaptors

data class RawFlightItem ( val Quotes:List<RawQuote>, val Places:List<RawPlaces>)

data class RawQuote ( val QuoteId:Int,
                      val MinPrice:Int,
                      val Direct:Boolean,
                      val OutboundLeg:Leg,
                      val InboundLeg:Leg
                      )

data class Leg ( val OriginId:Int,
                 val DestinationId:Int,
                 val DepartureDate:String
)

data class RawPlaces ( val PlaceId:Int,
                       val IataCode:String,
                       val Name:String
)