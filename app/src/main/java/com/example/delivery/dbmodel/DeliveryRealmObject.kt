package com.example.delivery.dbmodel

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

/**
 * Created by Ganesh Tikone on 18/9/18.
 * Class:
 */
open class DeliveryRealmObject : RealmObject {

    @PrimaryKey
    var id: Int = 0

    @Required
    lateinit var description: String

    @Required
    lateinit var imageUrl: String

    var location: DeliveryLocation? = null


    constructor() {}

    constructor(id: Int, description: String, imageUrl: String, location: DeliveryLocation) {
        this.id = id
        this.description = description
        this.imageUrl = imageUrl
        this.location = location
    }

    override fun toString(): String {
        return "DeliveryRealmObject{" +
                "id=" + id +
                ", description='" + description + '\''.toString() +
                ", imageUrl='" + imageUrl + '\''.toString() +
                ", location=" + location!!.toString() +
                '}'.toString()
    }
}
