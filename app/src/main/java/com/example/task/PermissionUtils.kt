package com.example.task

class PermissionUtils {

    companion object {
        const val STORAGE_PERMISSION = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        const val CAMERA_PERMISSION = android.Manifest.permission.CAMERA
        const val CALL_PERMISSION = android.Manifest.permission.CALL_PHONE
        val ACCESS_FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION
        val ACCESS_COARSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION
        val LOCATION_PERMISSIONS = arrayOf(
            ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION
        )

        val SETTINGS_PERMISSION_CODE = 10
        const val CAMERA_PERMISSION_CODE = 100
        const val STORAGE_PERMISSION_CODE = 101
        const val LOCATION_PERMISSION_CODE = 102

        const val IMAGE_REQ_CODE = 234
        const val FILE_REQ_CODE = 235
        const val LOCATION_REQ_CODE = 601
    }
}