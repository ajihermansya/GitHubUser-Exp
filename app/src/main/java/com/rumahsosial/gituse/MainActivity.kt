package com.rumahsosial.gituse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.rumahsosial.gituse.databinding.ActivityMainBinding
import com.rumahsosial.gituse.detail.DetailActivity
import com.rumahsosial.gituse.model.ResponseUserGithubs
import com.rumahsosial.gituse.util.Result

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    private val adapter by lazy {
        UserAdapter{ user ->
            //user di hapus jadi it nanti
            Intent(this, DetailActivity::class.java).apply {
                putExtra("item", user)
                startActivity(this)
            }
        }

    }

    private val viewModel by viewModels<MainViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        //agar ukuran item sama & untuk performa recyle view lwbih baik
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter

        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.getUser(newText.toString())
                return true
            }
        })

        viewModel.resultUser.observe(this) {
            when(it) {
                is Result.Success<*> -> {
                    adapter.setData(it.data as MutableList<ResponseUserGithubs.Item>)
                }
                is Result.Error -> {
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    binding.progressBar.isVisible = it.isLoading
                }
            }
        }
        viewModel.getUser()
    }
}


//Entity -> Table
//Dao -> Kumpulan Query
//AppDatabase -> Configurasi dari database room