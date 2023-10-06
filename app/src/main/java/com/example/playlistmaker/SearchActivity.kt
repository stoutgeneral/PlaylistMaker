package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    companion object {
        const val SEARCH_BAR = "SEARCH_BAR"
    }

    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesApi::class.java)
    private val trackList = ArrayList<Track>()
    private val trackAdapter = TrackAdapter()

    private lateinit var inputEditText: EditText
    private lateinit var rvTracks: RecyclerView
    private lateinit var placeholderMessage: LinearLayout
    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderText: TextView
    private lateinit var placeholderButton: Button
    private lateinit var viewTrackList: LinearLayout

    private var textSearch = ""


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        rvTracks = findViewById(R.id.rv_track)
        rvTracks.adapter = trackAdapter

        placeholderMessage = findViewById(R.id.placeholder_message)
        placeholderImage = findViewById(R.id.placeholder_image)
        placeholderText = findViewById(R.id.placeholder_text)
        placeholderButton = findViewById(R.id.placeholder_button)
        inputEditText = findViewById(R.id.search_bar)
        viewTrackList = findViewById(R.id.track_list_view)

        trackAdapter.trackList = trackList

        // Выход из экрана настроек
        val arrowBack: ImageView = findViewById(R.id.arrow_back_search)
        arrowBack.setOnClickListener {
            finish()
        }

        // Сброс введенных значений на экране поиск
        val resetTextButton: Button = findViewById(R.id.reset_text)
        resetTextButton.setOnClickListener {
            inputEditText.setText("")
            trackList.clear()
            trackAdapter.notifyDataSetChanged()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(window.decorView.windowToken, 0)
        }

        // Работа с экраном поиск.
        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textSearch = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                resetTextButton.visibility = changeButtonVisibility(s)
            }
        }

        inputEditText.addTextChangedListener(searchTextWatcher)

        //Обработчик нажатия Done на клавиатуре
        inputEditText.setOnEditorActionListener {_, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (textSearch.isNotEmpty()) {
                    trackSearch()
                }
            }
            false
        }
    }

    //Cохранение строки поиск
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_BAR, textSearch)
    }

    // Загрузка сохраненных значений строки поиск
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        textSearch = savedInstanceState.getString(SEARCH_BAR, "")
        inputEditText.setText(textSearch)
    }

    // Показ кнопки сброса
    fun changeButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showMessage (textMessage: String, imageMessage: Int, buttonMessage: Boolean) {
        placeholderMessage = findViewById(R.id.placeholder_message)
        placeholderImage = findViewById(R.id.placeholder_image)
        placeholderText = findViewById(R.id.placeholder_text)
        placeholderButton = findViewById(R.id.placeholder_button)

        if (textMessage.isNotEmpty()) {
            placeholderMessage.visibility = View.VISIBLE
            placeholderText.text = textMessage
            placeholderImage.setImageResource(imageMessage)
            trackList.clear()
            trackAdapter.notifyDataSetChanged()

            if (buttonMessage) {
                placeholderButton.visibility = View.VISIBLE
            } else {
                placeholderButton.visibility = View.GONE
            }
        } else {
            placeholderMessage.visibility = View.GONE
            viewTrackList.visibility = View.VISIBLE

        }
    }

    private fun trackSearch () {
        iTunesService.search(textSearch).enqueue(object : Callback<ITunesResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<ITunesResponse>, response: Response<ITunesResponse>) {
                if (response.code() == 200) {
                    trackList.clear()
                    if (response.body()?.results?.isNotEmpty() == true) {
                        trackList.addAll(response.body()?.results!!)
                        trackAdapter.notifyDataSetChanged()
                    }
                    if (trackList.isEmpty()) {
                        showMessage(getString(R.string.not_found), R.drawable.il_nothing_found, false)
                    }
                } else {
                    showMessage(getString(R.string.not_connection), R.drawable.il_connection_error, true)
                }
            }

            override fun onFailure(call: Call<ITunesResponse>, t: Throwable) {
                showMessage(getString(R.string.not_connection), R.drawable.il_connection_error, true)
                t.printStackTrace()
            }
        })
    }
}