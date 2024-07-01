package com.example.playlistmaker.ui.library


import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.Manifest
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreatePlaylistsBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.presentation.library.CreatePlaylistFragmentViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

open class CreatePlaylistsFragment : Fragment() {

    companion object {
        private var parentAudioPlayer = false
        fun newInstance(parent: Boolean) : CreatePlaylistsFragment {
            parentAudioPlayer = parent
            return CreatePlaylistsFragment()
        }
    }

    open lateinit var binding: FragmentCreatePlaylistsBinding
    open val viewModel: CreatePlaylistFragmentViewModel by viewModel()

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        if (it != null) {
            Glide.with(this)
                .load(it.toString())
                .placeholder(R.drawable.placeholder_image)
                .transform(CenterCrop(), RoundedCorners(resources.getDimensionPixelOffset(R.dimen.distance_between_elements)))
                .into(binding.playlistImage)

            viewModel.addImage(it.toString())
        } else {
            Log.d("PhotoPicker", R.string.image_is_not_selected.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreatePlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

     val requester = PermissionRequester.instance()
     var tempPlaylist = Playlist(tracks = ArrayList())
     lateinit var confirmDialog: MaterialAlertDialogBuilder

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observePlaylistState().observe(viewLifecycleOwner) { playlist ->
            tempPlaylist = playlist
        }

        confirmDialog = MaterialAlertDialogBuilder(requireContext(), R.style.AllertDialog)
            .setTitle(R.string.playlist_alert_title)
            .setMessage(R.string.playlist_alert_message)
            .setNeutralButton(R.string.alert_cancel) { _, _ -> }
            .setPositiveButton(R.string.playlist_alert_confirm) { _, _ ->
                navigateBack()
            }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    showAllert(tempPlaylist)
                }
            }
        )

        binding.back.setOnClickListener {
            showAllert(tempPlaylist)
        }

        binding.playlistImage.setOnClickListener {
            if (checkPermission()) {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            } else {
                lifecycleScope.launch {
                    requester.request(getPermissionType()).collect { result ->
                        when (result) {
                            is PermissionResult.Granted -> pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            is PermissionResult.Denied.DeniedPermanently -> {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                intent.data = Uri.fromParts("package", requireContext().packageName, null)
                                requireContext().startActivity(intent)
                            }
                            is PermissionResult.Denied.NeedsRationale -> {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.notification_permission),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            is PermissionResult.Cancelled -> return@collect
                        }
                    }
                }
            }
        }

        binding.inputEditTextName.addTextChangedListener(getTextWatcherName())
        binding.inputEditTextDescription.addTextChangedListener(getTextWatrcherDescription())
        binding.createPlaylistBt.setOnClickListener {
            lifecycleScope.launch { viewModel.playlistSave() }

            Toast.makeText(
                requireContext(),
                getString(R.string.playlist_created, binding.inputEditTextName.text.toString()),
                Toast.LENGTH_SHORT
            ).show()
            navigateBack()
        }
    }

    private fun navigateBack() {
        if (parentAudioPlayer) {
            parentFragmentManager.popBackStack()
            parentAudioPlayer = false
            requireActivity().findViewById<ConstraintLayout>(R.id.main_player_screen).visibility = View.VISIBLE // хм..
        } else {
            findNavController().popBackStack()
        }
    }

    private fun showAllert(playlist: Playlist) {
        if ((playlist.name == null || playlist.name == "") && (playlist.description == null || playlist.description == "") && (playlist.uri == null || playlist.uri == "")) {
            navigateBack()
        } else {
            confirmDialog.show()
        }
    }

    private fun checkPermission(): Boolean {
        val permissionProvided = ContextCompat.checkSelfPermission(
            requireContext(),
            getPermissionType()
        )
        return permissionProvided == PackageManager.PERMISSION_GRANTED
    }

    private fun getPermissionType(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_IMAGES
        else Manifest.permission.READ_EXTERNAL_STORAGE
    }

    private fun getTextWatcherName(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.addName(s.toString())
                binding.createPlaylistBt.isEnabled = s?.isNotEmpty() == true
            }

            override fun afterTextChanged(s: Editable?) {}

        }
    }

    private fun getTextWatrcherDescription(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.addDescription(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}

        }
    }
}