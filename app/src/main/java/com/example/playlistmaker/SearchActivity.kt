package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView

class SearchActivity : AppCompatActivity() {
    companion object {
        const val SEARCH_BAR = "SEARCH_BAR"
    }

    private var inputTextSearch = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Выход из экрана настроек
        val arrowBack: ImageView = findViewById(R.id.arrow_back_search)
        arrowBack.setOnClickListener {
            finish()
        }

        // Сброс введенных значений на экране поиск
        val inputEditText: EditText = findViewById(R.id.search_bar)
        val resetTextButton: Button = findViewById(R.id.reset_text)
        resetTextButton.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(window.decorView.windowToken, 0)
        }

        // Работа с экраном поиск.
        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                inputTextSearch = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                resetTextButton.visibility = resetTextButtonVisibiblity(s)
            }
        }

        inputEditText.addTextChangedListener(searchTextWatcher)
    }

    //Cохранение строки поиск
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(SEARCH_BAR, inputTextSearch)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputTextSearch = savedInstanceState.getString(SEARCH_BAR, "")
    }

    // Показ кнопки сброса
    fun resetTextButtonVisibiblity(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
    }
}