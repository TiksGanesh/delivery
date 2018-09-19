package com.example.delivery.permission

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.text.TextUtils
import com.example.delivery.R
import java.util.*

/**
 * Created by Ganesh Tikone on 21/5/18.
 * Class: PermissionUtil
 */
object PermissionUtil {

    /**
     * Permission request util array
     */
    private val permissionReqList = ArrayList<PermissionReq>()

    interface ReqPermissionCallback {
        fun onResult(success: Boolean, requestCode: Int)
    }

    /**
     * Builder constructor
     */
    private class PermissionReq internal constructor(internal val activity: Activity,
                                                     internal val permission: String,
                                                     internal val reqCode: Int,
                                                     internal val reqReason: CharSequence,
                                                     internal val rejectedMsg: CharSequence,
                                                     internal val callback: ReqPermissionCallback)

    /**
     * Check if already has permission for requested permission
     */
    fun hasPermission(activity: Activity, permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Check if runtime permission is already granted or
     * if not request for permission with reason to access specific area.
     */
    fun checkPermission(activity: Activity,
                        permission: String,
                        reqCode: Int,
                        reqReason: CharSequence,
                        rejectedMsg: CharSequence,
                        callback: ReqPermissionCallback) {
        if (hasPermission(activity, permission)) {
            // we shouldn't callback directly, it will mix the sync and async logic
            // if you want to check permission sync, you should manually call hasPermission() method

            // callback.onResult(true);
            activity.window.decorView.post { callback.onResult(true, reqCode) }
        } else {
            val shouldShowReqReason = ActivityCompat
                    .shouldShowRequestPermissionRationale(activity, permission)
            val req = PermissionReq(
                    activity, permission, reqCode, reqReason, rejectedMsg, callback)
            if (shouldShowReqReason) {
                showReqReason(req)
            } else {
                reqPermission(req)
            }
        }
    }

    /**
     * Show request permission reason if required
     */
    private fun showReqReason(req: PermissionReq) {
        AlertDialog.Builder(req.activity)
                .setCancelable(false)
                .setMessage(req.reqReason)
                .setPositiveButton(R.string.ok) { dialog, which -> reqPermission(req) }
                .show()
    }

    /**
     * Request permission
     */
    private fun reqPermission(req: PermissionReq) {
        permissionReqList.add(req)
        ActivityCompat.requestPermissions(req.activity, arrayOf(req.permission), req.reqCode)
    }

    /**
     * If user rejects permission , show reason for request permission.
     */
    private fun showRejectedMsg(req: PermissionReq, requestCode: Int) {
        AlertDialog.Builder(req.activity)
                .setCancelable(false)
                .setMessage(req.rejectedMsg)
                .setPositiveButton(R.string.ok) { dialog, which ->
                    req.callback.onResult(false, requestCode)
                    permissionReqList.remove(req)
                }
                .setNegativeButton(R.string.change_setting) { dialog, which -> openAppDetailSetting(req) }
                .show()
    }

    /**
     * Show Setting screen if required
     */
    private fun openAppDetailSetting(req: PermissionReq) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", req.activity.packageName, null)
        intent.data = uri
        req.activity.startActivityForResult(intent, req.reqCode)
    }

    // Because this lib only supports request one permission at one time,
    // so this method called 'onRequestPermissionResult',
    // not 'onRequestPermissionsResult'
    fun onRequestPermissionResult(activity: Activity,
                                  requestCode: Int,
                                  permissions: Array<String>,
                                  grantResults: IntArray) {
        var targetReq: PermissionReq? = null
        for (req in permissionReqList) {
            if (req.activity == activity
                    && req.reqCode == requestCode
                    && req.permission == permissions[0]) {
                targetReq = req
                break
            }
        }
        if (targetReq != null) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                targetReq.callback.onResult(true, requestCode)
                permissionReqList.remove(targetReq)
            } else {
                if (TextUtils.isEmpty(targetReq.rejectedMsg)) {
                    targetReq.callback.onResult(false, requestCode)
                    permissionReqList.remove(targetReq)
                } else {
                    showRejectedMsg(targetReq, requestCode)
                }
            }
        }
    }


    /**
     *  Callback to handle permission result
     */

    fun onActivityResult(activity: Activity,
                         reqCode: Int) {
        var targetReq: PermissionReq? = null
        for (req in permissionReqList) {
            if (req.activity == activity && req.reqCode == reqCode) {
                targetReq = req
                break
            }
        }
        if (targetReq != null) {
            if (hasPermission(targetReq.activity, targetReq.permission)) {
                targetReq.callback.onResult(true, reqCode)
            } else {
                targetReq.callback.onResult(false, reqCode)
            }
            permissionReqList.remove(targetReq)
        }
    }
}
