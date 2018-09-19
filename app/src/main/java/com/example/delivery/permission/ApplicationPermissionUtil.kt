package com.example.delivery.permission

import android.Manifest
import android.app.Activity
import com.example.delivery.R


/**
 * Created by Ganesh Tikone on 21/5/18.
 * Class: ApplicationPermissionUtil Singletone class
 *
 * Write function in one class to request for RunTime permission.
 * Permission Utility class for Marshmallow (API level 23) and above
 */
object ApplicationPermissionUtil {

    val WRITE_SD_REQ_CODE = 201


    val LOCATION_REQUEST_CODE = 203

    /**
     *  Request permission to write external storage
     * @param activity Activity object
     * @param callback PermissionUtil.ReqPermissionCallback object
     */
    fun checkWriteSD(activity: Activity, callback: PermissionUtil.ReqPermissionCallback) {

        PermissionUtil.checkPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                WRITE_SD_REQ_CODE,
                activity.getString(R.string.storage_request_reason),
                activity.getString(R.string.storage_reject_reason),
                callback)
    }


    /**
     * Request permission for access Location
     * @param activity Activity object
     * @param callback PermissionUtil.ReqPermissionCallback object
     */
    fun checkLocation(activity: Activity, callback: PermissionUtil.ReqPermissionCallback) {


        PermissionUtil.checkPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION,
                LOCATION_REQUEST_CODE,
                activity.getString(R.string.location_request_reason),
                activity.getString(R.string.location_reject_reason),
                callback)
    }
}