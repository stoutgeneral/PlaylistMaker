package com.example.playlistmaker.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.presentation.library.FragmentLibraryPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LibraryPlaylistFragment : Fragment() {

    companion object {
        fun newinstance() = FavoritesFragment()
    }

    private val viewModel: FragmentLibraryPlaylistViewModel by viewModel()
    private lateinit var binding: FragmentPlaylistBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }
}