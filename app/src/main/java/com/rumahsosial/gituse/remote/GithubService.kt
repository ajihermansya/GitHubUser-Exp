package com.rumahsosial.gituse.remote

import com.rumahsosial.gituse.BuildConfig
import com.rumahsosial.gituse.model.ResponseDetailUser
import com.rumahsosial.gituse.model.ResponseUserGithubs
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface GithubService {
    @JvmSuppressWildcards
    @GET("/users")
    suspend fun getUserGithub(
        @Header("Authorization")
        Authorization : String = BuildConfig.TOKEN,
    ) : MutableList<ResponseUserGithubs.Item>

    @JvmSuppressWildcards
    @GET("/users/{username}")
    suspend fun getDetailUserGithub(
        @Path("username") username : String,
        @Header("Authorization")
        Authorization : String = BuildConfig.TOKEN,
        ): ResponseDetailUser

    @JvmSuppressWildcards
    @GET("users/{username}/followers")
    suspend fun getFollowersUserGithub(
        @Path("username") username : String,
        @Header("Authorization")
        Authorization : String = BuildConfig.TOKEN,

    ): MutableList<ResponseUserGithubs.Item>


    @JvmSuppressWildcards
    @GET("users/{username}/following")
    suspend fun getfollowingUserGithub(
        @Path("username") username : String,
        @Header("Authorization")
        Authorization : String = BuildConfig.TOKEN,
        ) : MutableList<ResponseUserGithubs.Item>@JvmSuppressWildcards


    @GET("search/users")
    suspend fun searchUserGithub(
        @QueryMap params : Map<String, Any>,
        @Header("Authorization")
        Authorization : String = BuildConfig.TOKEN,
        ): ResponseUserGithubs



    //@QueryMap map: Map<String, Any>
}