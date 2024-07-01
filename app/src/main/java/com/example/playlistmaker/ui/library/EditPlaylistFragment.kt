package com.example.playlistmaker.ui.library

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.library.EditingPlaylistFragmentViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment : CreatePlaylistsFragment() {

    companion object {
        private const val PLAYLIST_ID = "playlistId"
        fun createArgs(playlistId: Int?): Bundle = bundleOf(PLAYLIST_ID to playlistId)
    }

    override val viewModel: EditingPlaylistFragmentViewModel by viewModel()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlistId = requireArguments().getInt(PLAYLIST_ID, 0)
        if (playlistId > 0) viewModel.getPlaylist(playlistId) else findNavController().popBackStack()

        binding.createPlaylistHeader.text = getText(R.string.playlist_edit)
        binding.createPlaylistBt.text = getText(R.string.create)
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        )

        viewModel.observePlaylist().observe(viewLifecycleOwner) {
            binding.inputEditTextName.setText(it.name)
            binding.inputEditTextDescription.setText(it.description)

            if (it.uri != null) {
                Glide.with(this)
                    .load(it.uri)
                    .placeholder(R.drawable.placeholder_image)
                    .transform(CenterCrop())
                    .into(binding.playlistImage)
            }
            tempPlaylist = it
        }

        binding.createPlaylistBt.setOnClickListener {
            lifecycleScope.launch {
                viewModel.savePlaylist(tempPlaylist)
                findNavController().popBackStack()
            }
        }
    }
}