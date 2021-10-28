package com.levp.getdatabynet.listOfUsersApi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.levp.getdatabynet.R
import com.levp.getdatabynet.data.PostModel
import kotlinx.android.synthetic.main.home_rv_item_view.view.*

class HomeAdapter(var listener: HomeListener) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    private var data: ArrayList<PostModel>? = null

    interface HomeListener {
        fun onItemDeleted(postModel: PostModel, position: Int)
    }

    fun setData(list: ArrayList<PostModel>) {
        data = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.home_rv_item_view, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = data?.get(position)
        holder.bindView(item, listener, position)
    }

    fun addData(postModel: PostModel) {
        data?.add(0, postModel)
        notifyItemInserted(0)
    }

    fun removeData(position: Int) {
        data?.removeAt(position)
        notifyDataSetChanged()
    }

    class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: PostModel?, listener: HomeListener, pos: Int) {
            if(item!=null) {
                itemView.tv_home_item_title.text = item.title
                itemView.tv_home_item_body.text = item.body

                itemView.img_delete.setOnClickListener {
                    listener.onItemDeleted(item, pos)
                }
            }
        }

    }
}