package com.shidqi.phinconminiproject.ui.myPokemonList

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.shidqi.phinconminiproject.R
import com.shidqi.phinconminiproject.databinding.FragmentMyPokemonListBinding
import com.shidqi.phinconminiproject.databinding.FragmentPokemonListBinding
import com.shidqi.phinconminiproject.models.local.toPokemonData
import com.shidqi.phinconminiproject.models.remote.PokemonData
import com.shidqi.phinconminiproject.ui.ViewModel
import com.shidqi.phinconminiproject.ui.myPokemonList.adapter.MyPokemonListAdapter
import com.shidqi.phinconminiproject.ui.pokemonDetail.PokemonDetailActivity
import com.shidqi.phinconminiproject.utils.getIdFromPokemonUrl
import com.shidqi.phinconminiproject.utils.getIdFromPokemonUrlLocal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPokemonList : Fragment() {

    private lateinit var binding : FragmentMyPokemonListBinding
    private lateinit var myPokemonListAdapter : MyPokemonListAdapter
    private val viewModel: ViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyPokemonListBinding.inflate(inflater, container, false)
        setupAdapter()
        bindViewModel()
        return binding.root
    }

    private fun bindViewModel(){
        viewModel.myPokemonList.observe(requireActivity()){pokemonList ->
            if(pokemonList.isEmpty()){
                binding.emptyState.isVisible = true
            }else{
                binding.emptyState.isVisible = false
            }
            myPokemonListAdapter.items = pokemonList.map { pokemon ->
                pokemon.toPokemonData()
            }
        }
    }

    private fun setupAdapter(){
        myPokemonListAdapter = MyPokemonListAdapter(requireActivity(), itemClick = {
            goToDetail(it)
        })
        val layoutManager =
            GridLayoutManager(requireActivity(), 2, GridLayoutManager.VERTICAL,false)

        binding.rvListMyPokemon.layoutManager = layoutManager
        binding.rvListMyPokemon.adapter = myPokemonListAdapter
    }

    private fun goToDetail(pokemonData : PokemonData){
        val intent = Intent(requireActivity(), PokemonDetailActivity::class.java)
        intent.putExtra(PokemonDetailActivity.Argument, getIdFromPokemonUrlLocal(pokemonData.url))
        startActivity(intent)
    }
}