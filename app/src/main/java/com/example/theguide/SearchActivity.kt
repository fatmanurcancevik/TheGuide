package com.example.theguide

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.theguide.databinding.ActivitySearchBinding
import kotlinx.coroutines.*

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val cityAdapter = CityAdapter()
    private val api = CityApi(apiKey = BuildConfig.NINJAS_API_KEY)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.resultsRecycler.layoutManager = LinearLayoutManager(this)
        binding.resultsRecycler.adapter = cityAdapter

        fun doSearch() {
            val q = binding.searchInput.text?.toString().orEmpty()
            if (q.isNotBlank()) search(q)
        }

        binding.searchButton.setOnClickListener { doSearch() }

        // Klavyeden Search'e basınca da ara
        binding.searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch()
                true
            } else false
        }
    }

    private fun search(query: String) {
        binding.searchButton.isEnabled = false
        binding.loading.visibility = android.view.View.VISIBLE
        binding.errorText.visibility = android.view.View.GONE

        scope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    api.searchCityByName(query)
                }
                cityAdapter.submitList(result)

                if (result.isEmpty()) {
                    binding.errorText.text = "No results found."
                    binding.errorText.visibility = android.view.View.VISIBLE
                }
            } catch (e: Exception) {
                binding.errorText.text = e.message ?: "Something went wrong."
                binding.errorText.visibility = android.view.View.VISIBLE
            } finally {
                binding.loading.visibility = android.view.View.GONE
                binding.searchButton.isEnabled = true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}