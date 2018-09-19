package com.example.delivery.network

import com.example.delivery.database.RealmController
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RequestService
/**
 * Default constructor
 */
private constructor() {

    /**
     * RequestInterface object
     */
    private var requestInterface: RequestInterface? = null


    init {
        initRequestInterface()
    }

    /**
     *
     */
    private fun initRequestInterface() {

        requestInterface = Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(RequestInterface::class.java)
    }


    /**
     * Load data from api
     *
     * @param offset offset int
     * @return Observable<DeliveryModel>
    </DeliveryModel> */
    fun getAllDeliveries(offset: Int): Observable<Unit>? {

        return requestInterface!!.getDeliveries(offset.toString(), LIMIT.toString())
                .retry(5)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map {
                   RealmController.getInstance().updateDeliveries(it)
                }
    }

    companion object {

        /**
         * BASE URL
         */
        private val BASEURL = "https://mock-api-mobile.dev.lalamove.com/"

        /**
         * Records limit
         */
        private val LIMIT = 20


        /**
         * RequestService Instance
         */

        private var instance: RequestService? = null


        @Synchronized
        fun getInstance(): RequestService {
            if (null == instance) {
                instance = RequestService()
            }
            return instance!!
        }
    }


}
