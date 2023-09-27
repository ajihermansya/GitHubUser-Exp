package com.rumahsosial.gituse.detail

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.rumahsosial.gituse.R
import com.rumahsosial.gituse.databinding.ActivityDetailBinding
import com.rumahsosial.gituse.detail.fragmendetail.FollowsFragment
import com.rumahsosial.gituse.detail.local.DbModule
import com.rumahsosial.gituse.detail.viewdetail.DetailAdapter
import com.rumahsosial.gituse.model.ResponseDetailUser
import com.rumahsosial.gituse.model.ResponseUserGithubs
import com.rumahsosial.gituse.util.Result

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    //penambhaan kode

    private val viewModel by viewModels<DetailViewModel>{
        DetailViewModel.Factory(DbModule(this))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        //get username di main actvity, ini kode diganti buat add room
        val item = intent.getParcelableExtra<ResponseUserGithubs.Item>("item")
        val username = item?.login ?:""

        viewModel.resultDetailUser.observe(this) {
            when(it) {
                is Result.Success<*> -> {
                    val user = it.data as ResponseDetailUser
                   binding.image.load(user.avatar_url) {
                       transformations(CircleCropTransformation())
                   }
                    binding.nama.text = user.name
                    //binding.id.text = user.login
                }
                is Result.Error -> {
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    binding.progressBar.isVisible = it.isLoading
                }
            }
        }
        viewModel.getDetailUser(username)



        val fragments = mutableListOf<Fragment>(
            FollowsFragment.newInstance(FollowsFragment.FOLLOWERS),
            FollowsFragment.newInstance(FollowsFragment.FOLLOWING),
        )
        val titleFragments = mutableListOf(
            getString(R.string.followers), getString(R.string.following)
        )
        val adapter = DetailAdapter(this, fragments)
        binding.viewpager.adapter = adapter

        TabLayoutMediator(binding.tab,binding.viewpager) { tab, posisi ->
            tab.text = titleFragments[posisi]
        }.attach()

        binding.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab?.position == 0) {
                    viewModel.getFollowers(username)
                } else {
                    viewModel.getFollowing(username)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        viewModel.getFollowers(username)


        //++
        viewModel.resultSuksesFavorite.observe(this){
            binding.btnFavorite.changeIconColor(R.color.red)
        }

        viewModel.resultDeleteFavorite.observe(this){
            binding.btnFavorite.changeIconColor(R.color.white)
        }

        binding.btnFavorite.setOnClickListener {
            viewModel.setFavorite(item)
        }

        viewModel.findFavorite(item?.id ?:0){
            binding.btnFavorite.changeIconColor(R.color.red)
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
fun FloatingActionButton.changeIconColor(@ColorRes color : Int){
    imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this.context, color))
}