package com.example.delivery.feature.delivery

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import com.example.delivery.R
import com.example.delivery.adapter.DeliveryAdapter
import com.example.delivery.database.RealmController
import com.example.delivery.dbmodel.DeliveryRealmObject
import com.example.delivery.extra.DividerItemDecoration
import com.example.delivery.feature.detail.MapsActivity
import com.example.delivery.network.RequestService
import com.example.delivery.permission.ApplicationPermissionUtil
import kotlinx.android.synthetic.main.activity_delivery.*
import kotlinx.android.synthetic.main.content_delivery.*

class DeliveryActivity : AppCompatActivity(), RealmController.DeliveryChangeListener, DeliveryAdapter.OnDeliveryItemClickListener, RequestService.OnRequestServiceCompleteListener {


    private val adapter = DeliveryAdapter()
    private var offset = 1

    private var lastPage = false
    private var loading = false
    private val pageCount = 100 // Assuming total page count is 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery)
        setSupportActionBar(toolbar)
        initRecyclerView()
        showPermissionDialogIfRequired()
    }

    /**
     *
     */
    private fun initRecyclerView() {


        deliveryRecyclerView.visibility = View.GONE
        messageTextView.visibility = View.VISIBLE


        adapter.setListener(this)

        val linearLayoutManager = LinearLayoutManager(this@DeliveryActivity, LinearLayoutManager.VERTICAL, false)
        deliveryRecyclerView.setHasFixedSize(true)
        deliveryRecyclerView.layoutManager = linearLayoutManager
        deliveryRecyclerView.adapter = adapter
        deliveryRecyclerView.addItemDecoration(DividerItemDecoration(this@DeliveryActivity))

        deliveryRecyclerView.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {

            override val isLastPage: Boolean
                get() = lastPage

            override val isLoading: Boolean
                get() = loading

            override val totalPageCount: Int
                get() = pageCount

            override fun loadMoreItems() {

                if (!loading) {
                    loading = true
                    offset += 1
                    loadData()
                }

            }
        })

        loadData()

    }

    private fun showPermissionDialogIfRequired() {
        ApplicationPermissionUtil.requestLocationAndNetworkStatePermission(activity = this@DeliveryActivity)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        ApplicationPermissionUtil.onRequestPermissionResult(requestCode, permissions, grantResults, this@DeliveryActivity)
    }


    /**
     *
     */
    private fun loadData() {

        if (isNetworkConnected()) {
            getDeliveries(offset)
        } else {
            showOfflineSnackBar()
            displayDataFromDatabase()
        }
    }

    /**
     *
     */
    private fun showOfflineSnackBar() {
        val snackbar = Snackbar.make(deliveryCordinatorLayout, getString(R.string.offline_device), Snackbar.LENGTH_INDEFINITE)
        val view = snackbar.getView()
        view.setBackgroundColor(ContextCompat.getColor(this@DeliveryActivity, R.color.colorRed))
        val textView = view.findViewById<TextView>(android.support.design.R.id.snackbar_text)
        textView.setTextColor(ContextCompat.getColor(this@DeliveryActivity, R.color.textColorPrimary))
        snackbar.setAction("Ok", View.OnClickListener {
            snackbar.dismiss()
        })
        snackbar.setActionTextColor(ContextCompat.getColor(this@DeliveryActivity, R.color.textColorPrimary))
        snackbar.show()
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
        val offset = (page - 1) * 20 + 1
        val requestService = RequestService.getInstance()
        requestService.setOnRequestServiceCompleteListener(this)
        requestService.getAllDeliveries(offset)!!.subscribe()
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
        if (deliveries.isEmpty()) {
            messageTextView.visibility = View.VISIBLE
        } else {
            deliveryRecyclerView.visibility = View.VISIBLE
            adapter.updateData(deliveries)
            messageTextView.visibility = View.GONE
        }

    }


    // ---- Request Service Callback ---
    override fun onRequestCallCompleteListener() {
        loading = false
        displayDataFromDatabase()
    }
    // --- RecyclerView callback  ---

    override fun onItemClick(itemId: Int) {
        val mapIntent = Intent(this@DeliveryActivity, MapsActivity::class.java)
        mapIntent.putExtra(MapsActivity.INTENT_KEY, itemId)
        startActivity(mapIntent)
    }
}
