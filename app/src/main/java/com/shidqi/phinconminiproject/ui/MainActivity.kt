package com.shidqi.phinconminiproject.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.shidqi.phinconminiproject.databinding.ActivityMainBinding
import com.shidqi.phinconminiproject.ui.myPokemonList.MyPokemonList
import com.shidqi.phinconminiproject.ui.pokemonList.PokemonListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel : ViewModel by viewModels()
    private lateinit var fragmentAdapter: FragmentAdapter
    private lateinit var mainActivityBinding : ActivityMainBinding
    private lateinit var myViewPager2 : ViewPager2



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        viewModel
        setupFragmentAdapter()
        setContentView(mainActivityBinding.root)
    }

    private fun setupFragmentAdapter(){

        myViewPager2 = mainActivityBinding.pager
        fragmentAdapter = FragmentAdapter(supportFragmentManager, lifecycle)
        fragmentAdapter.addFragment(PokemonListFragment())
        fragmentAdapter.addFragment(MyPokemonList())
        myViewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        myViewPager2.adapter = fragmentAdapter
        val tabLayout = mainActivityBinding.tabLayout
        TabLayoutMediator(tabLayout, myViewPager2){tab, position ->
            tab.text = if(position == 0)"Pokemon List" else "My Pokemon"
        }.attach()
    }

}