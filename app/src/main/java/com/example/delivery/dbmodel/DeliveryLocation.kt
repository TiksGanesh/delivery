package com.example.delivery.dbmodel

import io.realm.RealmObject

/**
 * Created by Ganesh Tikone on 18/9/18.
 * Class:
 */
open class DeliveryLocation : RealmObject {

    var latitude: Double? = null

    var longitude: Double? = null

    var address: String? = null


    constructor() {}

    constructor(latitude: Double, longitude: Double, address: String) {
        this.latitude = latitude
        this.longitude = longitude
        this.address = address
    }

    override fun toString(): String {
        return "DeliveryLocation{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", address='" + address + '\''.toString() +
                '}'.toString()
    }
}
