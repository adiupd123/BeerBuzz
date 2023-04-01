package com.adiupd123.beerbuzz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.adiupd123.beerbuzz.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val beerViewModel by viewModels<BeerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        binding.textMain.setOnClickListener {
            beerViewModel.getAllBeers(1, 1)
        }
        binding.randomBtn.setOnClickListener {
            beerViewModel.getRandomBeer()
        }
        binding.searchBtn.setOnClickListener {
            beerViewModel.getSearchedBeers("punk", 1, 2)
        }
        return binding.root
    }
}