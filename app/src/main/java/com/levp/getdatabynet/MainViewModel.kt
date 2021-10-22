package com.levp.getdatabynet

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.levp.getdatabynet.data.HomeRepository
import com.levp.getdatabynet.data.HomeRepositoryHabr
import com.levp.getdatabynet.data.PostModel

class MainViewModel(application: Application): AndroidViewModel(application) {
    private var homeRepositoryHabr:HomeRepositoryHabr?=null
    var postModelListLiveData : LiveData<List<PostModel>>?=null
    var createPostLiveData:LiveData<PostModel>?=null
    var deletePostLiveData:MutableLiveData<Boolean>?=null
    var text:MutableLiveData<String>?=null

    init {
        homeRepositoryHabr = HomeRepositoryHabr()

        //postModelListLiveData = MutableLiveData()
        text = MutableLiveData()


    }
    fun loadData1(){
        homeRepositoryHabr!!.getData()
        text!!.value = HomeRepositoryHabr.text
        Log.e("data", HomeRepositoryHabr.text.toString())
    }

    fun fetchAllPosts(){

    }
    fun createPost(postModel: PostModel){

    }

    fun deletePost(id:Int){

    }

}