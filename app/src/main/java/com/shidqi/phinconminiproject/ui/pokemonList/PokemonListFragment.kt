package com.shidqi.phinconminiproject.ui.pokemonList

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.shidqi.phinconminiproject.databinding.FragmentPokemonListBinding
import com.shidqi.phinconminiproject.models.remote.PokemonData
import com.shidqi.phinconminiproject.ui.ViewModel
import com.shidqi.phinconminiproject.ui.pokemonDetail.PokemonDetailActivity
import com.shidqi.phinconminiproject.ui.pokemonList.adapter.PokemonListAdapter
import com.shidqi.phinconminiproject.ui.pokemonList.adapter.PokemonLoadStateAdapter
import com.shidqi.phinconminiproject.utils.getIdFromPokemonUrl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PokemonListFragment : Fragment() {

    private lateinit var pokemonAdapter : PokemonListAdapter
    private lateinit var binding: FragmentPokemonListBinding
    private val viewModel: ViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPokemonListBinding.inflate(inflater, container, false)
        setupAdapter()

        lifecycleScope.launch {
            viewModel.data.observe(requireActivity()){data ->
                pokemonAdapter.submitData(lifecycle,data)
            }
        }
        return binding.root
    }

    private fun setupAdapter(){

        pokemonAdapter = PokemonListAdapter(context = requireActivity(), itemClick = {
            goToDetail(it)
        })

        val lm =
            GridLayoutManager(requireActivity(), 2, GridLayoutManager.VERTICAL,false)


        binding.rvListPokemon.layoutManager = lm
        binding.rvListPokemon.setHasFixedSize(true)
        binding.rvListPokemon.itemAnimator = null
        val adapterWithFooterHeader = pokemonAdapter.withLoadStateFooter(
            footer = PokemonLoadStateAdapter({ pokemonAdapter.retry() }, binding)
        )
        binding.rvListPokemon.apply {
            layoutManager = lm
            adapter = adapterWithFooterHeader
        }
        pokemonAdapter.addLoadStateListener {loadState ->
            val errorState = when {
                loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                loadState.prepend is LoadState.Error ->  loadState.prepend as LoadState.Error
                loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                else -> null
            }
            errorState?.let {
                binding.loading.isVisible = false
                binding.errorContainer.isVisible = true
            }
            if (pokemonAdapter.itemCount >= 10){
                binding.loading.visibility = View.GONE
            }

        }


    }

    private fun goToDetail(pokemonData :PokemonData){
        val intent = Intent(requireActivity(), PokemonDetailActivity::class.java)
        intent.putExtra(PokemonDetailActivity.Argument, getIdFromPokemonUrl(pokemonData.url))
        startActivity(intent)
    }

}

