package com.levp.getdatabynet.network

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName



//POJO для JSON ответа

class Post {
    @SerializedName("userId")
    @Expose
    private var userId = 0

    @SerializedName("id")
    @Expose
    private var id = 0

    @SerializedName("title")
    @Expose
    private var title: String? = null

    @SerializedName("body")
    @Expose
    private var body: String? = null

    fun getUserId(): Int {
        return userId
    }

    fun setUserId(userId: Int) {
        this.userId = userId
    }

    fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun getTitle(): String? {
        return title
    }

    fun setTitle(title: String?) {
        this.title = title
    }

    fun getBody(): String? {
        return body
    }

    fun setBody(body: String?) {
        this.body = body
    }
}