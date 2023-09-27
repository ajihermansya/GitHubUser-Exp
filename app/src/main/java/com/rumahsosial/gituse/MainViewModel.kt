package com.rumahsosial.gituse

import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rumahsosial.gituse.model.ResponseUserGithubs
import com.rumahsosial.gituse.remote.ApiClient
import com.rumahsosial.gituse.setting.preferences.SettingPreferences
import com.rumahsosial.gituse.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

//setting themen agar permanen
class MainViewModel(private val preferences : SettingPreferences) : ViewModel() {

    /*val resultSucces = MutableLiveData<MutableList<ResponseUserGithubs.Item>>()
    val resultError = MutableLiveData<String>()
    val resultLoading = MutableLiveData<Boolean>()
     */

    val resultUser = MutableLiveData<Result>()

    fun getTheme() = preferences.getThemeSetting().asLiveData()

    fun getUser() {
        viewModelScope.launch {
                flow {
                    val response = ApiClient
                        .githubService
                        .getUserGithub()

                    emit(response)
                }.onStart {
                    //dijalankan ketika mulai/jalan
                        resultUser.value = Result.Loading(true)

                }.onCompletion {
                    //dijalankan ketika selesai

                    resultUser.value = Result.Loading(false)

                }.catch {
                    //error handling
                    Log.e("Error", it.message.toString())
                    it.printStackTrace()
                    resultUser.value = Result.Error(it)

                }.collect{
                    //method untuk mendapatkan response
                    resultUser.value = Result.Success(it)

                }
        }
    }


    fun getUser(username : String) {
        viewModelScope.launch {
                flow {
                    val response = ApiClient
                        .githubService
                        .searchUserGithub(mapOf(
                            "q" to username,
                            "per_page" to 10
                        ))

                    emit(response)
                }.onStart {
                    //dijalankan ketika mulai/jalan
                        resultUser.value = Result.Loading(true)

                }.onCompletion {
                    //dijalankan ketika selesai

                    resultUser.value = Result.Loading(false)

                }.catch {
                    //error handling
                    Log.e("Error", it.message.toString())
                    it.printStackTrace()
                    resultUser.value = Result.Error(it)

                }.collect{
                    //method untuk mendapatkan response
                    resultUser.value = Result.Success(it.items)

                }
        }
    }

    class Factory(private val preferences: SettingPreferences) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            MainViewModel(preferences) as T

    }


}