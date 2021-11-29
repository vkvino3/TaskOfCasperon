package com.example.task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.task.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    companion object {
    val TAG = MainActivity::class.java.simpleName
}
    lateinit var binding: ActivityMainBinding
    lateinit var mapFragment: MapFragment
    lateinit var historyFragment: HistoryFragment
    private lateinit var tabTitles: ArrayList<String>
    private var from: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tabTitles = arrayListOf<String> (getString(R.string.map) , getString(R.string.history))

        initView()

    }

    private fun initView(){

        binding.txtTitle.text = "Activity"
        binding.tabLay.addTab(binding.tabLay.newTab().setText(getString(R.string.map)))
        binding.tabLay.addTab(binding.tabLay.newTab().setText(getString(R.string.history)))
        binding.viewPager.offscreenPageLimit = 1

        val adapter = MainActivityAdapter(supportFragmentManager, binding.tabLay.tabCount)
        binding.viewPager.adapter = adapter
        binding.tabLay.setupWithViewPager(binding.viewPager)

        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLay))
        binding.tabLay.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null){
                    binding.viewPager.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

    inner class MainActivityAdapter(
        private val fm: FragmentManager,
        private val NUM_OF_TABS: Int
    ) :
        FragmentStatePagerAdapter(
            fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ) {
        override fun getCount(): Int {
            return NUM_OF_TABS
        }

        override fun getItem(position: Int): Fragment {
            if (position == 0) {
                mapFragment = MapFragment(this@MainActivity)
                return mapFragment
            } else {
                historyFragment = HistoryFragment(this@MainActivity)
                return historyFragment
            }
        }
    }
}