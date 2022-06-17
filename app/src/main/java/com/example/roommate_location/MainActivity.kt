package com.example.roommate_location

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.roommate_location.databinding.ActivityMainBinding
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding
    companion object{
        private val REQUEST_PERMISSION_REQUEST_CODE = 2020
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btn.setOnClickListener{
            //check permission
            if (
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                binding.addr.text = ""
                getCurrentLocation()
            }
            else{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),100)

            }


        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_REQUEST_CODE && grantResults.size > 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation()

            }else{
                Toast.makeText(this@MainActivity,"Permission Denied!",Toast.LENGTH_SHORT).show()
            }
        }
    }






    private fun getCurrentLocation() {

        var locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        //now getting address from latitude and longitude

        val geocoder = Geocoder(this@MainActivity, Locale.getDefault())
        var addresses:List<Address>

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        LocationServices.getFusedLocationProviderClient(this@MainActivity)
            .requestLocationUpdates(locationRequest,object :LocationCallback(){
                override fun onLocationResult(p0: LocationResult) {
                    if (p0 != null) {
                        super.onLocationResult(p0)
                    }
                    LocationServices.getFusedLocationProviderClient(this@MainActivity)
                        .removeLocationUpdates(this)
                    if (p0 != null && p0.locations.size > 0){
                        var locIndex = p0.locations.size-1

                        var latitude = p0.locations.get(locIndex).latitude
                        var longitude = p0.locations.get(locIndex).longitude


                        addresses = geocoder.getFromLocation(latitude,longitude,1)

                        var address:String = addresses[0].getAddressLine(0)
                       binding.addr.text = address

                    }
                }
            }, Looper.getMainLooper())

    }



}


