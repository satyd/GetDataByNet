package com.levp.getdatabynet.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.levp.getdatabynet.network.ApiClient
import com.levp.getdatabynet.network.ApiInterface
import com.levp.getdatabynet.networkHabr.NetworkService
import com.levp.getdatabynet.networkHabr.Post
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeRepositoryHabr {
    private var apiInterface:ApiInterface?=null

    init {
        apiInterface = ApiClient.getApiClient().create(ApiInterface::class.java)
    }
    companion object{
        var text :String? = null
    }

    fun getData(){
        NetworkService.getInstance()
            .getJSONApi()
            ?.getPostWithID(1)
            ?.enqueue(object : Callback<Post> {

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    t.printStackTrace()
                    Log.e("response", "failed to get data")
                }

                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    val post = response.body()
                    Log.e("response", "data loaded successfully")

                    text = "${post!!.getId()}\n${post.getUserId()}\n${post.getTitle()}\n${post.getBody()}\n"
                }


            })
    }
}