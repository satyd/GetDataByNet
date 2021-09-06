package com.levp.getdatabynet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_display.*
import kotlinx.coroutines.launch
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import java.io.IOException

class MainActivity : AppCompatActivity() {
    lateinit var frag:Fragment
    //private val client = OkHttpClient()
    private var res:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        frag = DisplayFragment()

        launch_btn.setOnClickListener {
            getInitialData()
            addFrag()
        }
        remove_btn.setOnClickListener {
            removeFrag()
        }
        load_btn.setOnClickListener {
            val goToGetData = Intent(this,GetDataActivity::class.java)
            startActivity(goToGetData)
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
            getInitialData()
    }

    private fun getInitialData(){
        val service = Retrofit.Builder()
                .baseUrl("https://randomuser.me/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(UserService::class.java)

        lifecycleScope.launch {
            val users = service.getUsers()
            res = users.toString()
            Log.d("TAG_", users.toString())
        }
    }

    private fun removeFrag(){
        val ft  = supportFragmentManager.beginTransaction()
        if(frag.isAdded){
            ft.remove(frag).commit()

            //ft.detach(frag)
        }
        else{
            Toast.makeText(this,"Nothing to remove :/",Toast.LENGTH_SHORT).show()
        }
    }
    private fun addFrag(){
        val ft  = supportFragmentManager.beginTransaction()

        if(!frag.isAdded) {
            ft.add(R.id.fragment_container,frag)
            ft.addToBackStack(null)
            ft.commit()

        }
        else
        {
            Toast.makeText(this,"fragment is added",Toast.LENGTH_SHORT).show()
        }
        val dt = findViewById<TextView>(R.id.display_text)
        if(dt!=null)
            dt.text = res ?: "Данные не пришли, ебац"
    }
    private fun loadData(){
        //run("https://api.github.com/users/Evin1-/repos")
        //Log.e("request launched","hehehehe")
    }
//    private fun run(url: String) {
//        val request = Request.Builder()
//                .url(url)
//                .build()
//
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {}
//            override fun onResponse(call: Call, response: Response) { res = response.body()?.string() }
//        })
//
//    }
}


data class UserResponse(val results: List<Userb>)
data class Userb(val email: String, val phone: String)

/* Retrofit service that maps the different endpoints on the API, you'd create one
 * method per endpoint, and use the @Path, @Query and other annotations to customize
 * these at runtime */
interface UserService {
    @GET("/api")
    suspend fun getUsers(): UserResponse
}