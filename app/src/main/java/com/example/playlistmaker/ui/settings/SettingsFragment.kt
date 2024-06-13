package com.example.playlistmaker.ui.settings

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Перевод в темную тему
        binding.themeSwitcher.isChecked =
            (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES)

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.upThemeSetting(checked)
        }

        // Кнопка "Поделиться приложением"
        binding.shareApp.setOnClickListener {
            viewModel.shareApp(url = getString(R.string.share_link))

        }

        // Кнопка "Написать в поддержку"
        binding.messageToSupport.setOnClickListener {
            viewModel.writeToSupport(
                email = getString(R.string.email),
                theme = getString(R.string.theme_message),
                text = getString(R.string.message)
            )
        }

        // Кнопка "Пользовательское соглашение"
        binding.userAgreementButton.setOnClickListener {
            viewModel.openUserAgreement(url = getString(R.string.user_agreement_link))
        }
    }
}
