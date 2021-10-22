package com.levp.getdatabynet.catApi

import retrofit2.Response
import retrofit2.http.GET

interface CatApi {

    @GET("/meow")
    suspend fun getCatPic() : Response<CatImageUrl>

}