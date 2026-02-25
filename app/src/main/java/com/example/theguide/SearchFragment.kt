package com.example.theguide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.theguide.databinding.FragmentSearchBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val cityAdapter = CityAdapter()
    private val api = CityApi(apiKey = BuildConfig.NINJAS_API_KEY)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.resultsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.resultsRecycler.adapter = cityAdapter

        fun doSearch() {
            val q = binding.searchInput.text?.toString().orEmpty()
            if (q.isNotBlank()) search(q)
        }

        binding.searchButton.setOnClickListener { doSearch() }

        binding.searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch()
                true
            } else false
        }
    }

    private fun search(query: String) {
        binding.searchButton.isEnabled = false
        binding.loading.visibility = View.VISIBLE
        binding.errorText.visibility = View.GONE

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    api.searchCityByName(query)
                }

                cityAdapter.submitList(result)

                if (result.isEmpty()) {
                    binding.errorText.text = "No results found."
                    binding.errorText.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                binding.errorText.text = e.message ?: "Something went wrong."
                binding.errorText.visibility = View.VISIBLE
            } finally {
                binding.loading.visibility = View.GONE
                binding.searchButton.isEnabled = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}