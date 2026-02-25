package com.example.theguide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.theguide.databinding.FragmentCityListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log

class CityListFragment : Fragment() {

    private var _binding: FragmentCityListBinding? = null
    private val binding get() = _binding!!

    private val cityAdapter = CityAdapter()
    private val api = CityApi(apiKey = BuildConfig.NINJAS_API_KEY)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Fragment title (Toolbar kullanıyorsan ayrıca ayarlamak gerekebilir)
        val title = arguments?.getString(ARG_TITLE).orEmpty()
        activity?.title = title
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCityListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("CityListFragment", "args = ${arguments}")

        binding.resultsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.resultsRecycler.adapter = cityAdapter

        val title = arguments?.getString("arg_title").orEmpty()
        val query = arguments?.getString("arg_query").orEmpty().trim()

        activity?.title = title

        if (query.isBlank()) {
            binding.errorText.text = "Query is empty."
            binding.errorText.visibility = View.VISIBLE
            binding.loading.visibility = View.GONE
            return
        }

        load(query)
        Log.d("CityListFragment", "args = ${arguments}")

    }


    private fun load(queryString: String) {
        val q = queryString.trim()
        Log.d("CityListFragment", "load() queryString='$q'")

        if (q.isBlank()) {
            binding.loading.visibility = View.GONE
            binding.errorText.text = "Query is empty."
            binding.errorText.visibility = View.VISIBLE
            return
        }

        binding.loading.visibility = View.VISIBLE
        binding.errorText.visibility = View.GONE

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    api.getCitiesByQuery(q)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_TITLE = "arg_title"
        private const val ARG_QUERY = "arg_query"
    }
}