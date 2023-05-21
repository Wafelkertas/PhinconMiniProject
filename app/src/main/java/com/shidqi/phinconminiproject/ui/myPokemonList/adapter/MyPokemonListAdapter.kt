package com.shidqi.phinconminiproject.ui.myPokemonList.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.shidqi.phinconminiproject.databinding.ItemViewholderBinding
import com.shidqi.phinconminiproject.models.remote.PokemonData
import com.shidqi.phinconminiproject.ui.pokemonList.adapter.PokemonListAdapter
import com.shidqi.phinconminiproject.utils.changeContainerColorWithGradient
import com.shidqi.phinconminiproject.utils.getIdFromPokemonUrl
import com.shidqi.phinconminiproject.utils.getIdFromPokemonUrlLocal
import com.shidqi.phinconminiproject.utils.getPokemonImage

class MyPokemonListAdapter(private val context: Context, private val itemClick : (data : PokemonData) -> Unit) : RecyclerView.Adapter<MyPokemonListAdapter.MyPokemonViewHolder>() {

    inner class MyPokemonViewHolder(private val binding: ItemViewholderBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: PokemonData){
            Log.d("bindData1", data.toString())
            binding.root.setOnClickListener {
                itemClick(data)
            }
            changeContainerColorWithGradient(
                context = context,
                imageUrl = getPokemonImage(getIdFromPokemonUrlLocal(data.url)?:0),
                imageView = binding.ivPokemon,
                imageViewContainer =binding.root
            )
            binding.ivPokemon.visibility = View.VISIBLE
            binding.loading.visibility = View.INVISIBLE
            binding.tvPokemonName.text = data.name.capitalize()

        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<PokemonData>() {
        override fun areItemsTheSame(oldItem: PokemonData, newItem: PokemonData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PokemonData, newItem: PokemonData): Boolean {
            return oldItem == newItem
        }
    }


    private val differ = AsyncListDiffer(this, diffCallback)

    var items: List<PokemonData>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPokemonViewHolder {
        val binding = ItemViewholderBinding.inflate(
            LayoutInflater.from(context), parent,
            false
        )
        return MyPokemonViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: MyPokemonViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
