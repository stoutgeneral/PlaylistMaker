package com.example.playlistmaker.ui.library

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivivtyLibraryBinding

class LibraryActivity : AppCompatActivity() {

    private lateinit var tabLayoutMediator: TabLayoutMediator
    private lateinit var binding: ActivivtyLibraryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivivtyLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = LibraryViewPagerAdapter(supportFragmentManager, lifecycle)

        tabLayoutMediator =
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.favorites_track)
                    1 -> tab.text = getString(R.string.library_playlist)
                }
            }
        tabLayoutMediator.attach()

        binding.arrowBackLibrary.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabLayoutMediator.detach()
    }
}


