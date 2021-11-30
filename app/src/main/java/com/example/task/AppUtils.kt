package com.example.task

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.PrintWriter

class AppUtils @SuppressLint("CommitPrefEdits") constructor(private var context: Context) {

    companion object {
        private val TAG: String = AppUtils::class.java.simpleName
        const val USER_TOKEN="eyJ0eXAiOiJqd3QiLCJhbGciOiJIUzI1NiIsImNydCI6IjE2MzgxNzgzNzkifQ==.eyJ1c2VyX2lkIjoiNjE1MTdjZmQ0YjlkNjcyODk0MWU5MDg3IiwiYWNjZXNzIjoidXNlciIsImxpZmV0aW1lIjoiNDgifQ==.NDMzYzFiM2ZlNTdmNGNlMjEwMDgxNTBjNjY1M2Y1ZmQ3NzNkNmZiNzQ0NjliY2U2ODcwYTg5OTcwODc2NDFlNQ=="
        var snackbar: Snackbar? = null


        public fun makeToast(context: Context,message: String) {
            Toast.makeText(context.applicationContext, message, Toast.LENGTH_SHORT).show()
        }

        fun showNoInternetSnack(activity: Activity, parentLay: View, isConnected: Boolean) {
            if (snackbar != null) {
                snackbar?.dismiss()
                snackbar = null
            }

            if (!isConnected) {
                snackbar = Snackbar.make(
                    parentLay,
                    activity.getString(R.string.no_internet_connection),
                    Snackbar.LENGTH_INDEFINITE
                )
                snackbar?.show()
                Handler(activity.mainLooper).postDelayed(Runnable { dismiss() },2000)
            }
        }

        fun dismiss(){
            if (snackbar != null) {
                snackbar?.dismiss()
                snackbar = null
            }
        }

        fun isNetworkAvailable(context: Context?): Boolean {
            if (context == null) return false
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    when {
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            return true
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            return true
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                            return true
                        }
                    }
                }
            } else {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                    return true
                }
            }
            return false
        }

        fun downloadReceipt(historyItem: HistoryItem){
            val file=File(Environment.getExternalStorageDirectory().absolutePath)
            val fileNew=File(file,historyItem.rentalId+".txt")
            if(fileNew.exists())
                fileNew.delete()
            Log.d(TAG, "downloadReceipt: "+fileNew.absolutePath)
            fileNew.createNewFile()
            val writer=PrintWriter(fileNew)
            writer.append("ID :  "+historyItem.rentalId)
            writer.append("\nVehicle Detail : \n")
            writer.append("Info : "+historyItem.vehicle.info)
            writer.append("\nLicense Plate : "+historyItem.vehicle.licensePlate)
            writer.append("\nVehicle Color : "+historyItem.vehicle.vehicleColor)
            writer.append("\nVin Number : "+historyItem.vehicle.vinNumber)
            writer.append("\n\nStart From")
            writer.append("\nStation Name : "+historyItem.startedStation)
            writer.append("\nParking Lane  : "+historyItem.startedPakingLane)
            writer.append("\nTime : "+historyItem.startedAt)
            writer.append("\n\nEnded To")
            writer.append("\nStation Name : "+historyItem.endedStation)
            writer.append("\nParking Lane  : "+historyItem.endedParkingLane)
            writer.append("\nTime : "+historyItem.endedAt)
            writer.append("\n\nFare Details")
            writer.append("\nTotal Time : "+historyItem.fareDetails.totalTime)
            writer.append("\nTotal Cost : "+historyItem.fareDetails.totalCost)
writer.close()

        }

        /*public fun makeToast(message: String) {
            Toast.makeText(context.applicationContext, message, Toast.LENGTH_SHORT).show()
        }*/


    }


}
