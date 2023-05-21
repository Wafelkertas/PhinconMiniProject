package com.shidqi.phinconminiproject.ui.pokemonList.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.shidqi.phinconminiproject.databinding.FragmentPokemonListBinding
import com.shidqi.phinconminiproject.databinding.LayoutLoadStateFooterBinding

class PokemonLoadStateAdapter(private val retry: () -> Unit, private val parentLayoutBinding : FragmentPokemonListBinding):
    LoadStateAdapter<PokemonLoadStateAdapter.LoadStateViewHolder>()  {


    inner class LoadStateViewHolder(private val binding: LayoutLoadStateFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.buttonRetry.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            Log.d("pokemonLoadStateAdapter",loadState.toString())
            binding.apply {
                parentLayoutBinding.rvListPokemon.isVisible = loadState !is LoadState.Loading
                parentLayoutBinding.loading.isVisible = loadState !is LoadState.Loading
                progressBar.isVisible = loadState is LoadState.Loading
                buttonRetry.isVisible = loadState !is LoadState.Loading
                textViewError.isVisible = loadState !is LoadState.Loading
            }
        }
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = LayoutLoadStateFooterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return LoadStateViewHolder(binding)
    }
}

fun <T : Any, VH : RecyclerView.ViewHolder> PagingDataAdapter<T, VH>.loadFooter(binding: ViewBinding): ConcatAdapter {


    return this.withLoadStateFooter(
        footer = PokemonLoadStateAdapter(retry =  {
            this.retry()
        }, binding as FragmentPokemonListBinding)
    )
}