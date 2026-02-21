package com.example.theguide

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.theguide.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val bannerAdapter = BannerAdapter()
    private val cityAdapter = CityAdapter()

    private val api = CityApi(apiKey = BuildConfig.NINJAS_API_KEY)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Dikey liste (arama sonuçları)
        binding.contentRecycler.layoutManager = LinearLayoutManager(this)
        binding.contentRecycler.adapter = cityAdapter

        // Yatay bannerlar
        binding.bannerRecycler.layoutManager= LinearLayoutManager(this)
        binding.bannerRecycler.adapter = bannerAdapter

        bannerAdapter.submitList(
            listOf(
                Banner("Capitals", "Center of the countries", imageUrl = "https://i0.wp.com/fsk.org.tr/wp-content/uploads/2024/10/AnitKabir.jpeg?resize=880%2C660&ssl=1"),
                Banner("Crowd Lovers", "Get lost in the crowd", imageUrl = "https://bunny-wp-pullzone-nfqzsydbnl.b-cdn.net/wp-content/uploads/2023/03/du%CC%88nyanin-en-kalabalik-s%CC%A7ehirleri-tokyo.jpg"),
                Banner("İzmir", "Yemek & deniz", imageUrl = ""),
                Banner("Bursa", "Doğa kaçamağı", imageUrl = ""),
                Banner("Test", "Scroll", imageUrl = "")
            )
        )

        // Ara butonu
        binding.searchButton.setOnClickListener {
            val query = binding.searchInput.text?.toString().orEmpty()
            if (query.isNotBlank()) search(query)
        }
    }

    private fun search(query: String) {
        binding.searchButton.isEnabled = false

        scope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    api.searchCityByName(query)
                }
                cityAdapter.submitList(result)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                binding.searchButton.isEnabled = true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}