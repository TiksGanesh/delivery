package com.example.delivery.feature.delivery

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import com.example.delivery.R
import com.example.delivery.adapter.DeliveryAdapter
import com.example.delivery.database.RealmController
import com.example.delivery.dbmodel.DeliveryRealmObject
import com.example.delivery.extra.DividerItemDecoration
import com.example.delivery.feature.detail.MapsActivity
import com.example.delivery.network.RequestService
import com.example.delivery.permission.ApplicationPermissionUtil
import com.example.delivery.permission.PermissionUtil
import kotlinx.android.synthetic.main.activity_delivery.*
import kotlinx.android.synthetic.main.content_delivery.*

class DeliveryActivity : AppCompatActivity(), RealmController.DeliveryChangeListener, DeliveryAdapter.OnDeliveryItemClickListener, PermissionUtil.ReqPermissionCallback {


    private val adapter = DeliveryAdapter()
    private val offset = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery)
        setSupportActionBar(toolbar)
        initRecyclerView()
        loadData()
    }

    /**
     *
     */
    private fun initRecyclerView() {

        adapter.setListener(this)

        deliveryRecyclerView.setHasFixedSize(true)
        deliveryRecyclerView.layoutManager = LinearLayoutManager(this@DeliveryActivity, LinearLayoutManager.VERTICAL, false)
        deliveryRecyclerView.adapter = adapter
        deliveryRecyclerView.addItemDecoration(DividerItemDecoration(this@DeliveryActivity))

    }

    override fun onResume() {
        super.onResume()
        showPermissionDialog()
    }



    private fun showPermissionDialog() {

        ApplicationPermissionUtil.checkLocation(this@DeliveryActivity, this@DeliveryActivity)
    }


    private fun loadData() {
        if (isNetworkConnected()) {
            getDeliveries(offset)
        }
        displayDataFromDatabase()
    }

    /**
     *
     */
    private fun displayDataFromDatabase() {
        val realmController = RealmController.getInstance()
        realmController.getDeliveries(this@DeliveryActivity)
    }

    /**
     *
     */
    private fun getDeliveries(page: Int) {

        val requestService = RequestService.getInstance()
        requestService.getAllDeliveries(page)!!.subscribe()
    }


    /**
     *
     */
    private fun isNetworkConnected(): Boolean {
        val cm = this@DeliveryActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }

    // --- RealmController.DeliveryChangeListener callback ---

    override fun onDeliveryResultChange(deliveries: MutableList<DeliveryRealmObject>) {
        adapter.updateData(deliveries)
    }

    // --- RecyclerView callback  ---

    override fun onItemClick(itemId: Int) {
        val mapIntent = Intent(this@DeliveryActivity, MapsActivity::class.java)
        mapIntent.putExtra(MapsActivity.INTENT_KEY, itemId)
        startActivity(mapIntent)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionUtil.onRequestPermissionResult(this@DeliveryActivity,requestCode,permissions,grantResults)
    }

    override fun onResult(success: Boolean, requestCode: Int) {
        if (ApplicationPermissionUtil.LOCATION_REQUEST_CODE == requestCode) {
            Log.e("###", "permission granted")
            if (success) {
                ApplicationPermissionUtil.checkWriteSD(this@DeliveryActivity, this)
            }
        } else if (ApplicationPermissionUtil.WRITE_SD_REQ_CODE == requestCode) {
            Log.e("###", "permission granted sd card")
        }
    }
}