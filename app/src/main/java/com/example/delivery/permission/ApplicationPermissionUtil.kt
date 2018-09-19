package com.example.delivery.permission

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat


/**
 * Created by Ganesh Tikone on 19/9/18.
 * Class:
 */
object ApplicationPermissionUtil {


    private const val LOCATION_REQUEST_CODE = 2001
    private const val EXTERNAL_STORAGE_REQUEST_CODE = 2002

    /**
     *
     */
    fun requestLocationAndNetworkStatePermission(activity: Activity) {

        if (checkIfRunTimePermissionRequired()) {

            // Check and Request Location Permission if required

            if (!checkIfLocationPermissionAlreadyGranted(activity)) {
                requestLocationPermission(activity)
            }else {
                requestExternalStoragePermission(activity)
            }
        }
    }


    fun onRequestPermissionResult(requestCode: Int, permission: Array<String>, grantResult: IntArray, activity: Activity) {

        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResult.isEmpty() || grantResult[0] != PackageManager.PERMISSION_GRANTED) {
                showDialog(activity, "Google map required location permission to display map", LOCATION_REQUEST_CODE)
            }else{
                // Check and Request Location Permission if required
                if (!checkIfExternalStoragePermissionAlreadyGranted(activity)) {
                    requestExternalStoragePermission(activity)
                }
            }
        }

        if (requestCode == EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResult.isEmpty() || grantResult[0] != PackageManager.PERMISSION_GRANTED) {
                showDialog(activity, "Google maps required external storage access to display map", EXTERNAL_STORAGE_REQUEST_CODE)
            }
        }


    }

    private fun showDialog(activity: Activity, message: String, requestType: Int) {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->

            if (requestType == LOCATION_REQUEST_CODE) {
                requestLocationPermission(activity)
            }

            if (requestType == EXTERNAL_STORAGE_REQUEST_CODE) {
                requestLocationPermission(activity)
            }

        })

        builder.setNegativeButton("Cancel", null)

        builder.create().show()
    }

    /**
     *
     */
    private fun checkIfRunTimePermissionRequired(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    /**
     *
     */
    private fun checkIfLocationPermissionAlreadyGranted(activity: Activity): Boolean {
        val status = ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION)
        return PackageManager.PERMISSION_GRANTED == status
    }


    /**
     *
     */
    private fun checkIfExternalStoragePermissionAlreadyGranted(activity: Activity): Boolean {
        val status = ContextCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return PackageManager.PERMISSION_GRANTED == status
    }


    /**
     *
     */
    private fun requestLocationPermission(activity: Activity) {
        ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
    }


    /**
     *
     */
    private fun requestExternalStoragePermission(activity: Activity) {
        ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), EXTERNAL_STORAGE_REQUEST_CODE)
    }

}