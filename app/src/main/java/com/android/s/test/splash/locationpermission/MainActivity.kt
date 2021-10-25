package com.android.s.test.splash.locationpermission

import android.Manifest
import android.annotation.SuppressLint
import android.content.ClipboardManager
import android.content.pm.PackageManager
import android.location.LocationProvider
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import org.w3c.dom.Text
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var layout: View
    private val PERMISSION_RQUEST_ACCESS_FINE = 1234
    private val PERMISSION_RQUEST_ACCESS_COARSE = 2345

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        layout = findViewById(R.id.main_layout)

        findViewById<Button>(R.id.button).setOnClickListener { requestFineLocationPermission() }
        findViewById<Button>(R.id.button2).setOnClickListener { requestCoarseLocationPermission() }
    }

    private fun requestFineLocationPermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(layout, "ACCESS_FINE_LOCATION is granted", Snackbar.LENGTH_SHORT)
                    .show()
            getLocation()
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_RQUEST_ACCESS_FINE)
        }
    }

    private fun requestCoarseLocationPermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED) {
            Snackbar.make(layout, "ACCESS_COARSE_LOCATION is granted", Snackbar.LENGTH_SHORT)
                .show()
            getLocation()
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                PERMISSION_RQUEST_ACCESS_COARSE)
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_RQUEST_ACCESS_FINE) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(layout, "ACCESS_FINE_LOCATION is granted", Snackbar.LENGTH_SHORT)
                    .show()
                getLocation()
            } else {
                Snackbar.make(layout, "ACCESS_FINE_LOCATION is denied", Snackbar.LENGTH_SHORT)
                    .show()            }
        }

        if (requestCode == PERMISSION_RQUEST_ACCESS_COARSE) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(layout, "ACCESS_COARSE_LOCATION is granted", Snackbar.LENGTH_SHORT)
                    .show()
                getLocation()
            } else {
                Snackbar.make(layout, "ACCESS_COARSE_LOCATION is denied", Snackbar.LENGTH_SHORT)
                    .show()            }
        }
    }

    private fun getLocation() {
       val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(60)
            fastestInterval = TimeUnit.SECONDS.toMillis(30)
            maxWaitTime = TimeUnit.MINUTES.toMillis(2)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val currentLocation = locationResult.lastLocation
                Log.d("====", "==== currentLocation: $currentLocation")
                findViewById<TextView>(R.id.textView).text = "currentLocation: $currentLocation"
            }
        }

        Log.d("===", "requestLocationUpdates")
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.getMainLooper())
    }

}