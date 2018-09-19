package com.example.delivery.feature.detail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import com.example.delivery.R
import com.example.delivery.database.RealmController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {


    companion object {
        const val INTENT_KEY = "data"
    }

    private lateinit var mMap: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        setSupportActionBar(detailToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if(android.R.id.home == item?.itemId){
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    /**
     *
     */
    private fun getDataFromIntent() {
        val itemId = intent.getIntExtra(INTENT_KEY, 0)
        val realmController = RealmController.getInstance()
        val result = realmController.getDeliveryItemFromId(itemId)
        Log.e("map", result.toString())

        val lat = result?.location?.latitude!!
        val lang = result.location?.longitude!!


        val sydney = LatLng(lat, lang)
        mMap.addMarker(MarkerOptions().position(sydney).title(result.description))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15f), 2000, null)

        detailGTImageView.loadImageFromUrl(result.imageUrl)
        detailDeliveryDescriptionTextView.text = result.description

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getDataFromIntent()
    }
}
