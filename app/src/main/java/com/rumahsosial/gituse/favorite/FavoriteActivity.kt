package com.rumahsosial.gituse.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rumahsosial.gituse.MainViewModel
import com.rumahsosial.gituse.R
import com.rumahsosial.gituse.UserAdapter
import com.rumahsosial.gituse.databinding.ActivityFavoriteBinding
import com.rumahsosial.gituse.detail.DetailActivity
import com.rumahsosial.gituse.detail.local.DbModule

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFavoriteBinding

    private val adapter by lazy {
        UserAdapter{ user ->
            //user di hapus jadi it nanti
            Intent(this, DetailActivity::class.java).apply {
                putExtra("item", user)
                startActivity(this)
            }
        }

    }

    private val viewModel by viewModels<FavoriteViewModel>{
        FavoriteViewModel.Factory(DbModule(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.adapter = adapter

        viewModel.getUserFavorite().observe(this) {
            adapter.setData(it)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem):Boolean{
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}