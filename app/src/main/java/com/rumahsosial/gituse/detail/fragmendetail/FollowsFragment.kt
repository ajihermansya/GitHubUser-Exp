package com.rumahsosial.gituse.detail.fragmendetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rumahsosial.gituse.R
import com.rumahsosial.gituse.UserAdapter
import com.rumahsosial.gituse.databinding.FragmentFollowsBinding
import com.rumahsosial.gituse.detail.DetailViewModel
import com.rumahsosial.gituse.model.ResponseUserGithubs
import com.rumahsosial.gituse.util.Result


class FollowsFragment : Fragment() {
    private var binding : FragmentFollowsBinding? = null
    private val adapter by lazy {
        UserAdapter{

        }
    }

    private val viewModel by activityViewModels<DetailViewModel>()
    var type = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFollowsBinding.inflate(layoutInflater)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.rvFollows?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = this@FollowsFragment.adapter
        }
        when(type){

           FOLLOWERS -> {
                viewModel.resultFollowersUser.observe(viewLifecycleOwner, this::manageResultFollows)
            }
            FOLLOWING -> {
                viewModel.resultFollowingUser.observe(viewLifecycleOwner, this::manageResultFollows)
            }
        }
    }


    private fun manageResultFollows(state:Result){
        when(state) {
            is Result.Success<*> -> {
                adapter.setData(state.data as MutableList<ResponseUserGithubs.Item>)
            }
            is Result.Error -> {
                Toast.makeText(requireContext(), state.exception.toString(), Toast.LENGTH_SHORT).show()
            }
            is Result.Loading -> {
                binding?.progressBar?.isVisible = state.isLoading
            }
        }
    }

    companion object{
        const val FOLLOWING = 100
        const val FOLLOWERS = 101
        fun newInstance(type:Int) = FollowsFragment()
            .apply {
                this.type = type
            }
    }

}