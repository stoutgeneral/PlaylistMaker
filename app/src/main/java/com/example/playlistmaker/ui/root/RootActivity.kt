package com.example.playlistmaker.ui.root

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.root.RootViewModel
import com.example.playlistmaker.databinding.ActivityRootBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding
    private val viewModel: RootViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.setAppTheme()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.createPlaylistsFragment -> binding.bottomNavigationView.visibility = View.GONE
                R.id.detailsPlaylistFragment -> binding.bottomNavigationView.visibility = View.GONE
                R.id.editPlaylistFragment -> binding.bottomNavigationView.visibility = View.GONE
                R.id.audioPlayerFragment -> binding.bottomNavigationView.visibility = View.GONE
                else -> binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }

    }
}