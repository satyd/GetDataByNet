package com.levp.getdatabynet.networkHabr


import retrofit2.Call
import retrofit2.http.*


interface JSONPlaceHolderApi {
    @GET("/posts/{id}")
    fun getPostWithID(@Path("id") id: Int): Call<Post>

    @GET("/posts")
    fun getAllPosts(): Call<List<Post>>

    @GET("/posts")
    fun getPostOfUser(@Query("userId") id: Int): Call<List<Post>>

    @POST("/posts")
    fun postData(@Body data: Post?): Call<Post?>?
}