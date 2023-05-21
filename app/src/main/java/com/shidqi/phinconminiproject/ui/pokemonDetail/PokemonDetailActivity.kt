package com.shidqi.phinconminiproject.ui.pokemonDetail

import android.content.Context
import android.content.res.ColorStateList
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.ui.text.capitalize
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.chip.Chip
import com.shidqi.phinconminiproject.R
import com.shidqi.phinconminiproject.databinding.ActivityPokemonDetailBinding
import com.shidqi.phinconminiproject.models.local.PokemonDetailLocal
import com.shidqi.phinconminiproject.models.remote.pokemonDetail.PokemonDetailResponse
import com.shidqi.phinconminiproject.ui.ViewModel
import com.shidqi.phinconminiproject.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokemonDetailActivity : AppCompatActivity() {
    private var pokemonId: Int? = null
    private val viewModel: ViewModel by viewModels()
    private lateinit var binding: ActivityPokemonDetailBinding
    private var pokemonDetailLocal : PokemonDetailLocal? = null
    private var pokemonDetailResponse :PokemonDetailResponse? = null

    companion object {
        const val Argument = "ArgumentDetail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pokemonId = intent.getIntExtra(Argument, 0)
        pokemonId?.let {
            Log.d("pokemonId", it.toString())
            viewModel.getPokemonDetail(it)
        }
        binding = ActivityPokemonDetailBinding.inflate(layoutInflater)
        binding.ivBackButton.setOnClickListener {
            onBackPressed()
        }

        bindOnButton()
        bindViewModel()
        setContentView(binding.root)
    }


    private fun bindViewModel() {
        viewModel.pokemonDetail.observe(this) {
            when (it) {
                is Resource.Success -> {
                    binding.tvPokemonNameDetail.visibility = View.VISIBLE
                    binding.ivPokemonDetail.visibility = View.VISIBLE
                    binding.container.visibility = View.VISIBLE
                    binding.chipGroup.visibility = View.VISIBLE
                    binding.loading.visibility = View.GONE
                    changeContainerColorWithGradient(
                        context = this,
                        imageUrl = it.data?.imageUrl ?: "",
                        imageView = binding.ivPokemonDetail,
                        imageViewContainer = binding.root
                    )
                    it.data?.let { pokemonData ->
                        pokemonDetailResponse = pokemonData
                        binding.tvPokemonNameDetail.text = if(pokemonDetailLocal == null)"#${pokemonId} ${pokemonData.name.capitalize()}" else generateRenamedName("#${pokemonId} ${pokemonData.name.capitalize()}", pokemonDetailLocal!!.rename_count)
                        binding.tvPokemonMove.text = "Moves: ${pokemonData.moves.take(5).map { it.move.name.capitalize() }.joinToString(separator = ", ", postfix = ".")}"
                        this.addChipToGroup( pokemonData,this)
                        pokemonData.stats.first { predicate ->
                            predicate.stat.name == "hp"
                        }.base_stat.toFloat().apply {
                            binding.progressHP.labelText = this.toString()
                            binding.progressHP.progress = this
                        }

                        pokemonData.stats.first { predicate ->
                            predicate.stat.name == "attack"
                        }.base_stat.toFloat().apply {
                            binding.progressATK.labelText = this.toString()
                            binding.progressATK.progress = this
                        }
                        pokemonData.stats.first { predicate ->
                            predicate.stat.name == "defense"
                        }.base_stat.toFloat().apply {
                            binding.progressDEF.labelText = this.toString()
                            binding.progressDEF.progress = this
                        }
                    }

                }
                is Resource.Error -> {
                    binding.errorContainer.isVisible = true
                    binding.loading.isVisible = false
                }
                is Resource.Loading -> {
                    binding.tvPokemonNameDetail.visibility = View.INVISIBLE
                    binding.ivPokemonDetail.visibility = View.INVISIBLE
                    binding.container.visibility = View.INVISIBLE
                    binding.chipGroup.visibility = View.INVISIBLE
                    binding.loading.visibility = View.VISIBLE
                }
                else -> {}
            }
        }

        viewModel.myPokemonList.observe(this){myPokemonList ->
             pokemonDetailLocal = myPokemonList.find { myPokemonDetail ->
                myPokemonDetail.id == pokemonId
            }
            if(pokemonDetailLocal != null){
                binding.tvPokemonNameDetail.text =  generateRenamedName("#${pokemonId} ${pokemonDetailResponse?.name?.capitalize()}", pokemonDetailLocal!!.rename_count)
                hideCatchButton(true)
            }else{
                pokemonDetailResponse?.let { pokemonName ->
                    binding.tvPokemonNameDetail.text = "#$pokemonId ${pokemonName.name.capitalize()}"
                }
                hideCatchButton(false)
            }
            Log.d("pokemonData", pokemonDetailLocal.toString())
        }
        viewModel.catchPokemonResponse.observe(this){
            when(it){
                is Resource.Success -> {

                    it.data?.let { data ->
                        if(data.probability == 1){
                            binding.renameButton.visibility = View.VISIBLE
                            binding.releaseButton.visibility = View.VISIBLE
                            binding.catchButton.visibility = View.GONE
                            binding.btnLoading.visibility = View.GONE
                            Toast.makeText(this,  "You Success Catch the Pokemon, value was ${data.probability}" , Toast.LENGTH_SHORT).show()
                        }else{
                            binding.renameButton.visibility = View.GONE
                            binding.releaseButton.visibility = View.GONE
                            binding.catchButton.visibility = View.VISIBLE
                            binding.btnLoading.visibility = View.GONE
                            Toast.makeText(this,  "Failed to Catch Pokemon, value was ${data.probability}" , Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(this,  "Something has Occur, try again later." , Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    binding.catchButton.visibility = View.GONE
                    binding.renameButton.visibility = View.GONE
                    binding.releaseButton.visibility = View.GONE
                    binding.btnLoading.visibility = View.VISIBLE
                }
                is Resource.Idle -> {}
                else -> {}
            }
        }
        viewModel.releasePokemonResponse.observe(this){
            when(it){
                is Resource.Success -> {

                    it.data?.let { data ->
                        if(data.success){
                            binding.renameButton.visibility = View.GONE
                            binding.releaseButton.visibility = View.GONE
                            binding.catchButton.visibility = View.VISIBLE
                            binding.btnLoading.visibility = View.GONE
                            Toast.makeText(this,  "You Success Release the Pokemon, value was:${data.message.split(' ').last()}" , Toast.LENGTH_SHORT).show()
                        }else{
                            binding.renameButton.visibility = View.VISIBLE
                            binding.releaseButton.visibility = View.VISIBLE
                            binding.catchButton.visibility = View.GONE
                            binding.btnLoading.visibility = View.GONE
                            Toast.makeText(this,  "Failed to Release Pokemon, value was:${data.message.split(' ').last()}" , Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(this,  "Something has Occur, try again later." , Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    binding.catchButton.visibility = View.GONE
                    binding.renameButton.visibility = View.GONE
                    binding.releaseButton.visibility = View.GONE
                    binding.btnLoading.visibility = View.VISIBLE
                }
                is Resource.Idle -> {}
                else -> {}
            }
        }
        viewModel.renamePokemonResponse.observe(this){
            when(it){
                is Resource.Success -> {
                    it.data?.let { data ->
                        if(data.pokemonName.isEmpty()){
                            binding.renameButton.visibility = View.GONE
                            binding.releaseButton.visibility = View.GONE
                            binding.catchButton.visibility = View.VISIBLE
                            binding.btnLoading.visibility = View.GONE
                            Toast.makeText(this,  "You Rename Rename the Pokemon, it was renamed ${it.data.pokemonName}" , Toast.LENGTH_SHORT).show()
                        }else{
                            binding.renameButton.visibility = View.VISIBLE
                            binding.releaseButton.visibility = View.VISIBLE
                            binding.catchButton.visibility = View.GONE
                            binding.btnLoading.visibility = View.GONE
                            Toast.makeText(this,  "Failed to Rename Pokemon." , Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(this,  "Something has Occur, try again later." , Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    binding.catchButton.visibility = View.GONE
                    binding.renameButton.visibility = View.GONE
                    binding.releaseButton.visibility = View.GONE
                    binding.btnLoading.visibility = View.VISIBLE
                }
                is Resource.Idle -> {}
                else -> {}
            }
        }
    }

    private fun addChipToGroup(pokemonData: PokemonDetailResponse, context: Context) {
        pokemonData.types.map { type ->
            val chip = Chip(context)
            chip.text = type.type.name
            chip.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white)))
            chip.chipIcon = ContextCompat.getDrawable(this, R.drawable.ic_launcher_background)
            chip.isChipIconVisible = false
            chip.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context, parseTypeToColor(type)))
            binding.chipGroup.addView(chip as View)
        }
    }

    private fun hideCatchButton(hide:Boolean){
        if (hide){
            binding.releaseButton.visibility = View.VISIBLE
            binding.renameButton.visibility = View.VISIBLE
            binding.catchButton.visibility = View.GONE

        }else{
            binding.releaseButton.visibility = View.GONE
            binding.renameButton.visibility = View.GONE
            binding.catchButton.visibility = View.VISIBLE
        }
    }

    private fun bindOnButton(){
        binding.releaseButton.setOnClickListener{
            viewModel.releasePokemon(pokemonDetailResponse!!.id)
        }
        binding.catchButton.setOnClickListener{
            pokemonDetailResponse?.let { viewModel.catchPokemon(pokemonId!!, pokemonDetailResponse!!.name) }

        }
        binding.renameButton.setOnClickListener{
            viewModel.rename(pokemonDetailLocal!!)
        }
    }


}