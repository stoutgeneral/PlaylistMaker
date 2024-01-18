package com.example.playlistmaker.ui.settings

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.presentation.settings.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]

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

        // Возврат на главный экран
        binding.arrowBackSettings.setOnClickListener {
            finish()
        }
    }
}
