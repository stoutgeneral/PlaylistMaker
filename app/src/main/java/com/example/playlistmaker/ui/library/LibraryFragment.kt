package com.example.playlistmaker.ui.library

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentLibraryBinding
import com.example.playlistmaker.presentation.library.FragmentLibraryPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LibraryFragment : Fragment() {

    companion object {
        private const val STATE_TAB = "stateTab"
    }

    private lateinit var tabLayoutMediator: TabLayoutMediator
    private lateinit var binding: FragmentLibraryBinding
    private val viewModel: FragmentLibraryPlaylistViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = LibraryViewPagerAdapter(childFragmentManager, lifecycle)

        tabLayoutMediator =
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.favorites_track)
                    1 -> tab.text = getString(R.string.library_playlist)
                }
            }
        tabLayoutMediator.attach()
    }

    override fun onPause() {
        viewModel.selectedTabIndex.value = binding.viewPager.currentItem
        super.onPause()

    }
    override fun onResume() {
        viewModel.observeSelectedTabIndex().observe(viewLifecycleOwner) {tabIndex ->
            binding.viewPager.setCurrentItem(tabIndex, false)
        }
        super.onResume()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabLayoutMediator.detach()
    }

    /*override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(STATE_TAB, binding.viewPager.currentItem)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            val savedTabIndex = savedInstanceState.getInt(STATE_TAB, 0)
            binding.viewPager.setCurrentItem(savedTabIndex, false)
        }
    }*/
}




