package com.levp.getdatabynet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.levp.getdatabynet.data.PostModel
import kotlinx.android.synthetic.main.activity_get_data.*

class GetDataActivity : AppCompatActivity(), HomeAdapter.HomeListener  {

    private lateinit var vm:HomeViewModel
    private lateinit var adapter: HomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_data)
        vm = ViewModelProvider(this)[HomeViewModel::class.java]

        initAdapter()

        vm.fetchAllPosts()

        vm.postModelListLiveData?.observe(this, Observer {
            if (it!=null){
                rv_home.visibility = View.VISIBLE
                adapter.setData(it as ArrayList<PostModel>)
            }else{
                Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
            }
            progress_home.visibility = View.GONE
        })

    }
    private fun initAdapter() {
        adapter = HomeAdapter(this)
        rv_home.layoutManager = LinearLayoutManager(this)
        rv_home.adapter = adapter
    }

    override fun onItemDeleted(postModel: PostModel, position: Int) {
        postModel.id?.let { vm.deletePost(it) }
        vm.deletePostLiveData?.observe(this, Observer {
            if (it!=null){
                adapter.removeData(position)
            }else{
                showToast("Cannot delete post at the moment!")
            }
        })
    }
    private fun showToast(msg:String){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
    }
}