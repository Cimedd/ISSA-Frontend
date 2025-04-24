package com.example.myapplication.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.example.myapplication.R
import com.example.myapplication.ui.fragment.HomeFragment
import com.example.myapplication.ui.fragment.NotifikasiFragment
import com.example.myapplication.ui.fragment.RiwayatFragment
import com.example.myapplication.ui.fragment.SettingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity: AppCompatActivity() {
    private lateinit var bottomNav : BottomNavigationView
    private lateinit var fragment : FragmentContainerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottomNavigation)
        fragment = findViewById(R.id.fragmentContainer)

        replaceFragmen(HomeFragment())
        bottomNav.setOnItemSelectedListener {
            when (it.itemId){
                R.id.home -> replaceFragmen(HomeFragment())
                R.id.history -> replaceFragmen(RiwayatFragment())
                R.id.notifikasi -> replaceFragmen(NotifikasiFragment())
                R.id.setting -> replaceFragmen(SettingFragment())
                else -> {
                }
            }
            true
        }
    }

    private fun replaceFragmen(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaksi = fragmentManager.beginTransaction()
        fragmentTransaksi.replace(R.id.fragmentContainer, fragment)
        fragmentTransaksi.commit()
    }
}