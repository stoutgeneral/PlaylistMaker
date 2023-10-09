package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
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
import com.example.playlistmaker.SearchHistory.Companion.HISTORY_TRACK_KEY
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
    private val trackList = ArrayList<Track>() // tracks

    private lateinit var trackAdapter: TrackAdapter
    private lateinit var inputEditText: EditText
    private lateinit var rvTracks: RecyclerView
    private lateinit var rvHistory: RecyclerView
    private lateinit var placeholderMessage: LinearLayout
    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderText: TextView
    private lateinit var placeholderButton: Button
    private lateinit var viewTrackList: LinearLayout
    private lateinit var resetTextButton: Button
    private lateinit var buttonClearHistory: Button
    private lateinit var titleTextHistory: TextView

    private lateinit var searchHistory: SearchHistory
    private lateinit var sharePrefListener: OnSharedPreferenceChangeListener

    private val trackHistoryAdapter = TrackHistoryAdapter()
    private var textSearch = ""
    private var lastRequest = ""

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val sharePref = getSharedPreferences(HISTORY_TRACK_KEY, MODE_PRIVATE)
        trackAdapter = TrackAdapter(sharePref)
        searchHistory = SearchHistory(sharePref)

        trackAdapter.trackList = trackList
        rvTracks = findViewById(R.id.rv_track)
        rvTracks.adapter = trackAdapter

        placeholderMessage = findViewById(R.id.placeholder_message)
        placeholderImage = findViewById(R.id.placeholder_image)
        placeholderText = findViewById(R.id.placeholder_text)
        placeholderButton = findViewById(R.id.placeholder_button)
        inputEditText = findViewById(R.id.search_bar)
        viewTrackList = findViewById(R.id.track_list_view)

        // Выход из экрана настроек
        val arrowBack: ImageView = findViewById(R.id.arrow_back_search)
        arrowBack.setOnClickListener {
            finish()
        }

        // Кнопка "Очистить историю"
        buttonClearHistory = findViewById(R.id.clear_history)
        buttonClearHistory.setOnClickListener {
            searchHistory.clearHistoryTrack()
            viewTrackList.visibility = View.GONE
        }

        sharePrefListener = OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == HISTORY_TRACK_KEY) {
                showHistoryTrack(searchHistory)
            }
        }

        sharePref.registerOnSharedPreferenceChangeListener(sharePrefListener)
        // Работа с экраном поиск.
        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textSearch = s.toString()

            }

            override fun afterTextChanged(s: Editable?) {
                resetTextButton.visibility = changeButtonVisibility(s)
                viewTrackList.visibility = if (inputEditText.hasFocus() && s?.isEmpty() == true) View.VISIBLE else View.GONE
            }
        }

        inputEditText.addTextChangedListener(searchTextWatcher)

        //Обработчик нажатия Done на клавиатуре
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (textSearch.isNotEmpty()) {
                    trackSearch()
                }
            }
            false
        }

        // Сброс введенных значений на экране поиск
        resetTextButton = findViewById(R.id.reset_text)
        resetTextButton.setOnClickListener {
            inputEditText.setText("")
            trackList.clear()
            trackAdapter.notifyDataSetChanged()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(window.decorView.windowToken, 0)

            placeholderMessage.visibility = View.GONE
            viewTrackList.visibility = View.VISIBLE
        }

        // Обработчик нажатия кнопки Обновить при проблемах со связью
        placeholderButton.setOnClickListener {
            trackSearch()
            viewTrackList.visibility = View.VISIBLE
        }

        // Логика показа истории треков
        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            val isNotEmpty = searchHistory.getHistoryTrack().isNotEmpty()
            viewTrackList.visibility = if (hasFocus && inputEditText.text.isEmpty()) {
                if (isNotEmpty) {
                    showHistoryTrack(searchHistory)
                    View.VISIBLE
                } else {
                    View.GONE
                }
            } else {
                View.GONE
            }
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
    private fun showHistoryTrack(searchHistory: SearchHistory) {
        val rvHistory = findViewById<RecyclerView>(R.id.rv_history)

        trackHistoryAdapter.trackList = searchHistory.getHistoryTrack()
        rvHistory.adapter = trackHistoryAdapter
        trackHistoryAdapter.notifyDataSetChanged()

        rvHistory.visibility = View.VISIBLE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showMessage(text: String, imageId: Int, button: Boolean) {
        placeholderMessage = findViewById(R.id.placeholder_message)
        placeholderImage = findViewById(R.id.placeholder_image)
        placeholderText = findViewById(R.id.placeholder_text)
        placeholderButton = findViewById(R.id.placeholder_button)
        viewTrackList = findViewById(R.id.track_list_view)

        if (textSearch.isNotEmpty()) {
            placeholderMessage.visibility = View.VISIBLE
            viewTrackList.visibility = View.GONE
            placeholderText.text = text
            placeholderImage.setImageResource(imageId)

            trackList.clear()
            trackAdapter.notifyDataSetChanged()
            if (button) {
                placeholderButton.visibility = View.VISIBLE
            } else {
                placeholderButton.visibility = View.GONE
            }
        } else {
            placeholderMessage.visibility = View.GONE
            viewTrackList.visibility = View.VISIBLE
        }
    }

    private fun trackSearch() {
        textSearch = inputEditText.text.toString()

        iTunesService.search(textSearch).enqueue(object : Callback<ITunesResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ITunesResponse>,
                response: Response<ITunesResponse>
            ) {
                val results = response.body()?.results

                if (response.code() == 200) {
                    trackList.clear()
                    if (results?.isNotEmpty() == true) {
                        trackList.addAll(results)
                        trackAdapter.notifyDataSetChanged()
                        lastRequest = ""
                    }
                    if (trackList.isEmpty()) {
                        showMessage(
                            getString(R.string.not_found),
                            R.drawable.il_nothing_found,
                            false
                        )
                        trackAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<ITunesResponse>, t: Throwable) {
                showMessage(
                    getString(R.string.not_connection),
                    R.drawable.il_connection_error,
                    true
                )
            }
        })
    }
}