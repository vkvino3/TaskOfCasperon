package com.example.task

import android.Manifest.permission
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.task.databinding.ActivityMainBinding
import androidx.core.view.ViewCompat

import androidx.appcompat.content.res.AppCompatResources

import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.task.helper.OnItemClicked
import com.google.android.material.tabs.TabLayout


class MainActivity : AppCompatActivity(), OnItemClicked {
    companion object {
        val TAG = MainActivity::class.java.simpleName
    }
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        initView()
    }

    private fun initView(){
        binding.txtTitle.text = "Activity"
        val adapter = MainActivityAdapter(supportFragmentManager)
        binding.viewPager.adapter = adapter
        binding.tabLay.setupWithViewPager(binding.viewPager)
        setTabBG(R.drawable.tab_left_select,R.drawable.tab_left_unselect)
        binding.tabLay.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(binding.tabLay.selectedTabPosition ==0) {
                    setTabBG(R.drawable.tab_left_select,R.drawable.tab_left_unselect);
                }
                else {
                    setTabBG(R.drawable.tab_left_unselect,R.drawable.tab_right_select);
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

    }

    private fun setTabBG(tab1: Int, tab2: Int) {
        val tabStrip = binding.tabLay.getChildAt(0) as ViewGroup
        val tabView1: View? = tabStrip.getChildAt(0)
        val tabView2: View? = tabStrip.getChildAt(1)
        if (tabView1 != null) {
            val paddingStart: Int = tabView1.paddingStart
            val paddingTop: Int = tabView1.paddingTop
            val paddingEnd: Int = tabView1.paddingEnd
            val paddingBottom: Int = tabView1.paddingBottom
            ViewCompat.setBackground(
                tabView1,
                AppCompatResources.getDrawable(tabView1.context, tab1)
            )
            ViewCompat.setPaddingRelative(
                tabView1,
                paddingStart,
                paddingTop,
                paddingEnd,
                paddingBottom
            )
        }
        if (tabView2 != null) {
            val paddingStart: Int = tabView2.paddingStart
            val paddingTop: Int = tabView2.paddingTop
            val paddingEnd: Int = tabView2.paddingEnd
            val paddingBottom: Int = tabView2.paddingBottom
            ViewCompat.setBackground(
                tabView2,
                AppCompatResources.getDrawable(tabView2.context, tab2)
            )
            ViewCompat.setPaddingRelative(
                tabView2,
                paddingStart,
                paddingTop,
                paddingEnd,
                paddingBottom
            )
        }
    }

    inner class MainActivityAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getCount(): Int {
            return 2
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return if (position == 0) {
                getString(R.string.map)
            } else {
                getString(R.string.history)
            }
        }

        override fun getItem(position: Int): Fragment {
            return if (position == 0) {
               MapFragment(this@MainActivity)
            } else {
               HistoryFragment(this@MainActivity)
            }
        }
    }

    override fun downloadReceipt(historyItem: HistoryItem) {
        if (ContextCompat.checkSelfPermission(this, permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission.WRITE_EXTERNAL_STORAGE), 100)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    AppUtils.downloadReceipt(historyItem)
                } else {
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    val uri: Uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }
            } else {
                AppUtils.downloadReceipt(historyItem)
            }

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==100){
            var permissionGranted = true
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(applicationContext, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionGranted = false
                    break
                }
            }
            if(permissionGranted){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (!Environment.isExternalStorageManager()) {
                        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                        val uri: Uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }
                }
            }
        }
    }
}