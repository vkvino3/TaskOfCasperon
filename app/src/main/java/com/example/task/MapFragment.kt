package com.example.task

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.task.databinding.FragmentMyMapBinding
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.common.internal.Constants
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import timber.log.Timber

class MapFragment(val activity: MainActivity) : Fragment(), OnMapReadyCallback {

    companion object {
        val TAG = MapFragment::class.java.simpleName
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    lateinit var binding: FragmentMyMapBinding
    lateinit var mContext: Context
    private lateinit var mMap: GoogleMap
    private var mCurrentLocation: Location? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var mSettingsClient: SettingsClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLocationSettingsRequest: LocationSettingsRequest? = null
    private var mLocationCallback: LocationCallback? = null
    private var mRequestingLocationUpdates = true
    private lateinit var locationManager: LocationManager
    private var center: LatLng = LatLng(0.0, 0.0)
    private var from: String? = null
    private var lat: Double = 0.0
    private var lng: Double = 0.0
    private var tempLat: Double = 0.0
    private var tempLng: Double = 0.0

    private var myLocationClicked: Boolean = true

    private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000
    private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2
    val REQUEST_CHECK_SETTINGS_GPS = 0x1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_map, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initValues()
        initView()

    }

    private fun initValues() {
        mContext = activity
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext)
        mSettingsClient = LocationServices.getSettingsClient(mContext);
        locationManager =
            getActivity()?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private fun initView() {
        var mapFragment : SupportMapFragment?=null
        mapFragment = fragmentManager?.findFragmentById(R.id.mapFragment) as SupportMapFragment?
        /*mapFragment = requireActivity().supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment*/
        mapFragment?.getMapAsync(this)

        createLocationCallback()
        createLocationRequest()
        buildLocationSettingsRequest()
    }

    private fun createLocationCallback() {
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                mCurrentLocation = locationResult.lastLocation
                if (myLocationClicked) {
                    myLocationClicked = false
                    try {
                        val cameraUpdate: CameraUpdate =
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    mCurrentLocation?.latitude!!,
                                    mCurrentLocation?.longitude!!
                                ), 15f
                            )
                        mMap.animateCamera(cameraUpdate)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                stopLocationUpdates()
            }
        }
    }

    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()

        if (mLocationRequest != null) {// Sets the desired interval for active location updates. This interval is
            // inexact. You may not receive updates at all if no location sources are available, or
            // you may receive them slower than requested. You may also receive updates faster than
            // requested if other applications are requesting location at a faster interval.
            mLocationRequest?.interval = UPDATE_INTERVAL_IN_MILLISECONDS

            // Sets the fastest rate for active location updates. This interval is exact, and your
            // application will never receive updates faster than this value.
            mLocationRequest?.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
            mLocationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun buildLocationSettingsRequest() {
        val builder = LocationSettingsRequest.Builder()
        try {
            builder.addLocationRequest(mLocationRequest!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mLocationSettingsRequest = builder.build()
    }

    private fun startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient!!.checkLocationSettings(mLocationSettingsRequest)
            .addOnSuccessListener(requireActivity(), OnSuccessListener {
                Timber.tag(TAG).i("All location settings are satisfied")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(requireContext(),PermissionUtils.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                            requireContext(),PermissionUtils.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // TODO: Consider calling
                        //    Activity#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        return@OnSuccessListener
                    }
                }
                if (ActivityCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        activity,
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
                    return@OnSuccessListener
                }
                fusedLocationClient.requestLocationUpdates(
                    mLocationRequest,
                    mLocationCallback, Looper.myLooper()
                )
            })
            .addOnFailureListener(requireActivity()) { e ->
                val statusCode = (e as ApiException).statusCode
                when (statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        Timber.tag(TAG).i(
                            "Location settings are not satisfied. Attempting to upgrade location settings"
                        )
                        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the
                                // result in onActivityResult().
                                val rae = e as ResolvableApiException
                                rae.startResolutionForResult(
                                    activity,
                                    REQUEST_CHECK_SETTINGS_GPS
                                )
                            } catch (sie: IntentSender.SendIntentException) {
                                Timber.tag(TAG).e(
                                    "PendingIntent unable to execute request."
                                )
                            }
                        } else {
                            try {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                    val isLocationEnabled = locationManager.isLocationEnabled
                                    if (!isLocationEnabled) {
                                        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                                        AppUtils.makeToast(requireContext(),"Enable Location Access in High Accuracy Mode")
                                    }
                                } else {
                                    var locationMode: Int =
                                        Settings.Secure.getInt(
                                            getActivity()?.contentResolver,
                                            Settings.Secure.LOCATION_MODE
                                        )
                                    if (locationMode != Settings.Secure.LOCATION_MODE_HIGH_ACCURACY) {
                                        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                                        AppUtils.makeToast(requireContext(),"Enable Location Access in High Accuracy Mode")
                                    }
                                }
                            } catch (e1: Settings.SettingNotFoundException) {
                                e1.printStackTrace()
                            }
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> Timber.tag(TAG).e(
                        "Location settings are inadequate, and cannot be fixed here. Fix in Settings."
                    )
                }
            }
    }

    private fun stopLocationUpdates() {
        /*if (!mRequestingLocationUpdates) {
            Log.d(TAG, "stopLocationUpdates: updates never requested, no-op.");
            return;
        }*/

        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        fusedLocationClient.removeLocationUpdates(mLocationCallback)
            .addOnCompleteListener(activity, object : OnCompleteListener<Void> {
                override fun onComplete(p0: Task<Void>) {
                    mRequestingLocationUpdates = false
                }

            })
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!
        setUpMap()
        // Add a marker in Sydney and move the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(center))
    }

    private fun setUpMap() {
        //1
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity,
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
        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = false
        // 2
        fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
            // Got last known location. In some rare situations this can be null.
            // 3
            mCurrentLocation = location
            if (from != null) {
                val currentLatLng = LatLng(lat, lng)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            } else
                if (location != null) {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                }
        }

        mMap.setOnCameraIdleListener(GoogleMap.OnCameraIdleListener { // TODO Auto-generated method stub
            center = mMap.cameraPosition.target
            lat = center.latitude
            lng = center.longitude
            mMap.clear()
        })

        mMap.setOnCameraMoveListener(GoogleMap.OnCameraMoveListener { // TODO Auto-generated method stub
            /*center = mMap.cameraPosition.target
            lat = center.latitude
            lng = center.longitude
            mMap.clear()*/
        })
    }


    override fun onResume() {
        if (!checkPermissions(PermissionUtils.LOCATION_PERMISSIONS)) {
            ActivityCompat.requestPermissions(
                activity,
                PermissionUtils.LOCATION_PERMISSIONS,
                PermissionUtils.LOCATION_PERMISSION_CODE
            )
        } else if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            turnGPSOn()
        } else if (myLocationClicked) {
            mRequestingLocationUpdates = true
            startLocationUpdates()
        } else if (mRequestingLocationUpdates) {
            startLocationUpdates()
        }
        super.onResume()
    }

    private fun checkPermissions(
        permissionList: Array<String>
    ): Boolean {
        var isPermissionsGranted = false
        for (permission in permissionList) {
            if (ContextCompat.checkSelfPermission(
                    activity, permission
                ) === PackageManager.PERMISSION_GRANTED
            ) {
                isPermissionsGranted = true
            } else {
                isPermissionsGranted = false
                break
            }
        }
        return isPermissionsGranted
    }

    fun turnGPSOn() {
        mSettingsClient
            ?.checkLocationSettings(mLocationSettingsRequest)
            ?.addOnSuccessListener(
                activity,
                object : OnSuccessListener<LocationSettingsResponse?> {
                    override fun onSuccess(p0: LocationSettingsResponse?) {
                        //  GPS is already enable, callback GPS status through listener
                    }

                })
            ?.addOnFailureListener(activity, object : OnFailureListener {
                override fun onFailure(e: java.lang.Exception) {

                    val statusCode: Int = (e as ApiException).getStatusCode()
                    when (statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            val rae: ResolvableApiException = e as ResolvableApiException
                            rae.startResolutionForResult(
                                activity,
                                REQUEST_CHECK_SETTINGS_GPS
                            )
                        } catch (sie: IntentSender.SendIntentException) {
                            Timber.tag(TAG).i("PendingIntent unable to execute request.")
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            val errorMessage = "Location settings are inadequate, and cannot be " +
                                    "fixed here. Fix in Settings."
                            AppUtils.makeToast(requireContext(),errorMessage)
                        }
                    }
                }

            })
    }


}

