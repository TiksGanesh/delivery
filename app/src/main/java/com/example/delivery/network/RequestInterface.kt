package com.example.delivery.network

import com.example.delivery.model.DeliveryModel

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RequestInterface {

    @GET("deliveries")
    fun getDeliveries(@Query("offset") offset: String, @Query("limit") limit: String): Observable<List<DeliveryModel>>

}
