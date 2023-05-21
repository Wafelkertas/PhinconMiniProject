package com.shidqi.phinconminiproject.ui.pokemonList.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.shidqi.phinconminiproject.databinding.ItemViewholderBinding
import com.shidqi.phinconminiproject.models.remote.PokemonData
import com.shidqi.phinconminiproject.utils.changeContainerColorWithGradient
import com.shidqi.phinconminiproject.utils.getIdFromPokemonUrl

class PokemonListAdapter(private val context: Context, private val itemClick : (data : PokemonData) -> Unit) : PagingDataAdapter<PokemonData, PokemonListAdapter.PokemonViewHolder>(DiffUtilCallBack) {
    inner class PokemonViewHolder(private val binding:ItemViewholderBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data:PokemonData){
            this@PokemonListAdapter.addLoadStateListener { loadState ->
                Log.d("loadState"," ${data.name} $loadState")
                when(loadState.source.append){
                    is LoadState.Loading -> {
                        binding.loading.visibility = View.VISIBLE
                        binding.ivPokemon.visibility = View.INVISIBLE
                        binding.tvPokemonName.visibility = View.INVISIBLE
                    }
                    else -> {
                        binding.ivPokemon.visibility = View.VISIBLE
                        binding.tvPokemonName.visibility = View.VISIBLE
                        binding.loading.visibility = View.GONE
                    }
                }
            }

            binding.root.setOnClickListener {
                itemClick(data)
            }
            changeContainerColorWithGradient(
                context = context,
                imageUrl = data.imageUrl,
                imageView = binding.ivPokemon,
                imageViewContainer =binding.root
            )

            binding.tvPokemonName.text = data.name.capitalize()

        }
    }


    object DiffUtilCallBack : DiffUtil.ItemCallback<PokemonData>() {
        override fun areItemsTheSame(oldItem: PokemonData, newItem: PokemonData): Boolean {
            return getIdFromPokemonUrl(oldItem.url) == getIdFromPokemonUrl(newItem.url)
        }

        override fun areContentsTheSame(oldItem: PokemonData, newItem: PokemonData): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = ItemViewholderBinding.inflate(
            LayoutInflater.from(context), parent,
            false
        )
        return PokemonViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }


}