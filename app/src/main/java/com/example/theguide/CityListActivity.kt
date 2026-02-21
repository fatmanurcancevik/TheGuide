package com.example.theguide

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.theguide.databinding.ActivityCityListBinding
import kotlinx.coroutines.*

class CityListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCityListBinding
    private val cityAdapter = CityAdapter()
    private val api = CityApi(apiKey = BuildConfig.NINJAS_API_KEY)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra(EXTRA_TITLE).orEmpty()
        val query = intent.getStringExtra(EXTRA_QUERY).orEmpty()

        this.title = title

        binding.resultsRecycler.layoutManager = LinearLayoutManager(this)
        binding.resultsRecycler.adapter = cityAdapter

        load(query)
    }

    private fun load(queryString: String) {
        binding.loading.visibility = View.VISIBLE
        binding.errorText.visibility = View.GONE

        scope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    api.getCitiesByQuery(queryString)
                }
                cityAdapter.submitList(result)

                if (result.isEmpty()) {
                    binding.errorText.text = "No results found."
                    binding.errorText.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                binding.errorText.text = e.message ?: "An error occurred."
                binding.errorText.visibility = View.VISIBLE
            } finally {
                binding.loading.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    companion object {
        private const val EXTRA_TITLE = "extra_title"
        private const val EXTRA_QUERY = "extra_query"

        fun start(context: Context, title: String, queryString: String) {
            val i = Intent(context, CityListActivity::class.java)
                .putExtra(EXTRA_TITLE, title)
                .putExtra(EXTRA_QUERY, queryString)
            context.startActivity(i)
        }
    }
}