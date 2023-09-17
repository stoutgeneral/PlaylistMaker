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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Выход из экрана настроек
        val arrowBack = findViewById<ImageView>(R.id.arrow_back_search)
        arrowBack.setOnClickListener {
            finish()
        }

        // Сброс введенных значений на экране поиск
        val resetTextButton = findViewById<Button>(R.id.reset_text)
        val inputEditText = findViewById<EditText>(R.id.search_bar)
        resetTextButton.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(window.decorView.windowToken, 0)
        }

        fun resetTextButtonVisibiblity(s: CharSequence?): Int {
            return if (s.isNullOrEmpty()) {
                View.INVISIBLE
            } else {
                View.VISIBLE
            }
        }

        // Работа с экраном поиск.
        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                resetTextButton.visibility = resetTextButtonVisibiblity(s)
                SEARCH_BAR.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        inputEditText.addTextChangedListener(searchTextWatcher)
    }

    //Cохранение строки поиск
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("SEARCH_BAR", SEARCH_BAR)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.getString(SEARCH_BAR, "")
    }
}