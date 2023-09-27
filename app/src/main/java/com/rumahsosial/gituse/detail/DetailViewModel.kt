package com.rumahsosial.gituse.detail

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rumahsosial.gituse.detail.local.DbModule
import com.rumahsosial.gituse.model.ResponseUserGithubs
import com.rumahsosial.gituse.remote.ApiClient
import com.rumahsosial.gituse.util.Result
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch


//penambahan kode

class DetailViewModel(private val db : DbModule) : ViewModel() {

    val resultDetailUser = MutableLiveData<Result>()
    val resultFollowersUser = MutableLiveData<Result>()
    val resultFollowingUser = MutableLiveData<Result>()
    val resultSuksesFavorite = MutableLiveData<Boolean>()
    val resultDeleteFavorite = MutableLiveData<Boolean>()

    //++
    private var isFavorite = false
    fun setFavorite(item:ResponseUserGithubs.Item?){
        viewModelScope.launch {
            item?.let {
                if (isFavorite) {
                    db.userDao.delete(item)
                    resultDeleteFavorite.value = true
                }
                else{
                    db.userDao.insert(item)
                    resultSuksesFavorite.value = true
                }
            }
            isFavorite = !isFavorite
        }
    }

    fun findFavorite(id: Int, listenFavorite:()->Unit){
        viewModelScope.launch {
            val user = db.userDao.findById(id)
            if (user != null ) {
                listenFavorite()
                isFavorite = true
            }
        }
    }

    fun getDetailUser(username : String) {
        viewModelScope.launch {
            flow {
                val response = ApiClient
                    .githubService
                    .getDetailUserGithub(username)

                emit(response)
            }.onStart {
                //dijalankan ketika mulai/jalan
                resultDetailUser.value = Result.Loading(true)

            }.onCompletion {
                //dijalankan ketika selesai

                resultDetailUser.value = Result.Loading(false)

            }.catch {
                //error handling
                Log.e("Error", it.message.toString())
                it.printStackTrace()
                resultDetailUser.value = Result.Error(it)

            }.collect{
                //method untuk mendapatkan response
                resultDetailUser.value = Result.Success(it)

            }
        }
    }

    fun getFollowers(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiClient
                    .githubService
                    .getFollowersUserGithub(username)
                emit(response)
            }.onStart {
                //dijalankan ketika mulai/jalan
                resultFollowersUser.value = Result.Loading(true)

            }.onCompletion {
                //dijalankan ketika selesai
                resultFollowersUser.value = Result.Loading(false)

            }.catch {
                //error handling
                it.printStackTrace()
                resultFollowersUser.value = Result.Error(it)

            }.collect{
                //method untuk mendapatkan response
                resultFollowersUser.value = Result.Success(it)

            }
        }
    }



    fun getFollowing(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiClient
                    .githubService
                    .getfollowingUserGithub(username)

                emit(response)
            }.onStart {
                //dijalankan ketika mulai/jalan
                resultFollowingUser.value = Result.Loading(true)

            }.onCompletion {
                //dijalankan ketika selesai
                resultFollowingUser.value = Result.Loading(false)

            }.catch {
                //error handling
                it.printStackTrace()
                resultFollowingUser.value = Result.Error(it)

            }.collect{
                //method untuk mendapatkan response
                resultFollowingUser.value = Result.Success(it)

            }
        }


    }


    class Factory(private val db:DbModule) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = DetailViewModel(db) as T
    }


}