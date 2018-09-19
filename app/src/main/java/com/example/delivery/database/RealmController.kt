package com.example.delivery.database

import android.util.Log
import com.example.delivery.dbmodel.DeliveryLocation
import com.example.delivery.dbmodel.DeliveryRealmObject
import com.example.delivery.model.DeliveryModel
import io.realm.Realm
import io.realm.RealmChangeListener

/**
 * Created by Ganesh Tikone on 12/7/18.
 * Class: RealmController Database Controller
 */
class RealmController private constructor() {

    companion object {

        /**
         * Signletone Instance of RealmController class
         */
        private var instance: RealmController? = null


        /**
         * get RealController Instance
         *
         * @return RealmController
         */
        fun getInstance(): RealmController {
            if (null == instance) {
                instance = RealmController()
            }
            return instance!!
        }
    }


    /**
     * Update database
     */
    fun updateDeliveries(listOfDeliveryModel: List<DeliveryModel>) {

        val realmInstance: Realm = Realm.getDefaultInstance()

        realmInstance.executeTransactionAsync({ realm ->
            listOfDeliveryModel.forEach {
                //Log.e("####", it.toString());
                realm.insertOrUpdate(DeliveryRealmObject(it.id, it.description, it.imageUrl, DeliveryLocation(it.location.lat, it.location.lng, it.location.address)))
            }
        }, {
            Log.e("####", "data added successfully")
            realmInstance.close()
        }, { error ->
            Log.e("###", error.message)
            realmInstance.close()
        })
    }


    /**
     *
     */
    fun getDeliveries(listener: DeliveryChangeListener) {

        val realmInstance: Realm = Realm.getDefaultInstance()

        realmInstance.where(DeliveryRealmObject::class.java)
                .findAllAsync()
                .addChangeListener(RealmChangeListener {

                    val listOfDeliveries = realmInstance.copyFromRealm(it)
                    listener.onDeliveryResultChange(listOfDeliveries)
                })
    }


    /**
     *
     */
    fun getDeliveryItemFromId(id: Int): DeliveryRealmObject? {

        val realmInstance: Realm = Realm.getDefaultInstance()

        return realmInstance.where(DeliveryRealmObject::class.java)
                .equalTo("id", id)
                .findFirst()
    }


    interface DeliveryChangeListener {
        fun onDeliveryResultChange(deliveries: MutableList<DeliveryRealmObject>)
    }

}
