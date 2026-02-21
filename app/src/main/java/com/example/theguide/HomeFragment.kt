package com.example.theguide.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.theguide.Banner
import com.example.theguide.BannerAdapter
import com.example.theguide.CityAdapter
import com.example.theguide.R
import com.example.theguide.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val bannerAdapter = BannerAdapter { banner ->
        val args = Bundle().apply {
            putString("title", banner.title)
            putString("queryString", banner.queryString)
        }
        findNavController().navigate(R.id.cityListFragment, args)
    }

    private val cityAdapter = CityAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        binding.contentRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.contentRecycler.adapter = cityAdapter

        binding.bannerRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.bannerRecycler.adapter = bannerAdapter

        bannerAdapter.submitList(
            listOf(
                Banner("Capitals", "Center of the countries",
                    imageUrl = "https://i0.wp.com/fsk.org.tr/wp-content/uploads/2024/10/AnitKabir.jpeg?resize=880%2C660&ssl=1",
                    queryString = "name=Rome"
                ),
                Banner("Crowd Lovers", "Get lost in the crowd",
                    imageUrl = "https://bunny-wp-pullzone-nfqzsydbnl.b-cdn.net/wp-content/uploads/2023/03/du%CC%88nyanin-en-kalabalik-s%CC%A7ehirleri-tokyo.jpg",
                    queryString = "min_population=500000"
                ),
                Banner("Northern Lights", "Places to discover the Northern Lights",
                    imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQKwRwgt4CENKzmTElsUoo95Fn9fQ_iGd8m4g&s",
                    queryString = "country=FI"
                ),
                Banner("Test1", "Scroll1",
                    imageUrl = "",
                    queryString = ""
                ),
                Banner("Test2", "Scroll2",
                    imageUrl = "",
                    queryString = ""
                ),
            )
        )

        binding.searchInput.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}