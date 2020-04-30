package com.uni.flightfinder

import com.google.gson.JsonObject

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface restAPI {

    /*

    Add all requests here!

    Example of making a REST API call with retrofit
    @Headers("Content-Type: application/json")
    @POST("booking")
    fun makeBooking(@Body book: Book?): Call<Book?>?

    */


    object Model {
        data class Result(val query: Query)
        data class Query(val searchInfo: SearchInfo)
        data class SearchInfo(val data: String)
    }

    //apikey = ra669332369799286187348279121593  ??

    @Headers(
        "x-rapidapi-host: skyscanner-skyscanner-flight-search-v1.p.rapidapi.com",
        "x-rapidapi-key: 0ce6064c72mshe1e6a1e471477f4p1bee12jsn6c32a225809c"
    )
    @GET("apiservices/autosuggest/v1.0/UK/GBP/en-GB/")
    fun getAirports(@Query("query") place: String): Call<JsonObject>


    @Headers(
        "x-rapidapi-host: skyscanner-skyscanner-flight-search-v1.p.rapidapi.com",
        "x-rapidapi-key: 0ce6064c72mshe1e6a1e471477f4p1bee12jsn6c32a225809c"
    )
    //use the skyscanner code in the title?
    //need to make the data struct for the airport itself
    @GET("apiservices/browsequotes/v1.0/UK/GBP/en-US/{departing}/{destination}/{outboundDate}/") //  query in this order: -String manipulation from to date out and query date in as a header
    fun getRoutes(@Path("departing") departing:String, @Path("destination") destination:String, @Path("outboundDate") outboundDate:String, @Query("inboundpartialdate") inboundpartialdate: String): Call<JsonObject>



    companion object {
        fun create():restAPI{
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(
                    RxJava2CallAdapterFactory.create())
                .addConverterFactory(
                    GsonConverterFactory.create())
                .baseUrl("https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/")
                //.client()
                .build()
            return retrofit.create(restAPI::class.java)
        }
    }

}