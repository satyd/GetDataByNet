package com.levp.getdatabynet

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.levp.getdatabynet.catApi.CatApi
import com.levp.getdatabynet.questionApi.QuestionsActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


@DelicateCoroutinesApi
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private val BASE_URL = "https://jsonplaceholder.typicode.com"
    var catNum = 401
    private val BASE_URL_CAT = "https://http.cat"

    //private val client = OkHttpClient()

    private var apiStr: String? = null

    private var text: String? = null
    private var currImage: Bitmap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinner = findViewById<Spinner>(R.id.choose_api_spinner)
        val adapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(
            this,
            R.array.api_list,
            android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                itemSelected: View?, selectedItemPosition: Int, selectedId: Long
            ) {
                val choose = resources.getStringArray(R.array.api_list)
                apiStr = choose[selectedItemPosition].toLowerCase(Locale.ROOT)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        getStarterCat()

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        get_cat_btn.setOnClickListener {
            Toast.makeText(this, "loading image...", Toast.LENGTH_SHORT).show()
            getStarterCat()
        }


        goto_btn.setOnClickListener {
            Log.d("api picker", apiStr?:"no string")

            when(apiStr){
                "question api"->{ startActivity(Intent(this, QuestionsActivity::class.java))}
                else -> {}
            }

        }
    }

    private fun getStarterCat() {
        GlobalScope.launch(Dispatchers.Main) {
            val catLink = getStarterCatLink()
            Log.d("cat link", catLink)
            Glide.with(this@MainActivity)
                .asBitmap()
                .load(catLink)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                    ) {
                        display_pic.setImageBitmap(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // this is called when imageView is cleared on lifecycle call or for
                        // some other reason.
                        // if you are referencing the bitmap somewhere else too other than this imageView
                        // clear it here as you can no longer have the bitmap
                    }
                })
        }
    }

    private suspend fun getStarterCatLink(): String {
        val URL = "https://aws.random.cat"
        val api = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CatApi::class.java)

        var ans = CompletableDeferred<String>()

        val operation = Job(GlobalScope.launch(Dispatchers.IO) {
            Log.d("launch", "get cat coroutine launched")
            val response = api.getCatPic()

            if (response.isSuccessful) {
                ans.complete(response.body()?.file ?: "https://http.cat/404")
                Log.d("cat response", "${ans}")

            } else {
                Log.e("response", "failed to load cat")
            }

        })

        return ans.await()
    }

    private fun getInitialData1() {
        lifecycleScope.launch {
            viewModel.loadData1()
            text = viewModel.text?.value

        }
        Toast.makeText(this, "$text loaded!", Toast.LENGTH_SHORT).show()
    }


    private fun showData() {

        val displayText = findViewById<TextView>(R.id.display_text)
        val displayPic = findViewById<ImageView>(R.id.display_pic)
        if (displayText != null)
            displayText.text = text ?: "Данные не пришли, ебац"
        if (displayPic != null && currImage != null) {
            displayPic.setImageBitmap(currImage)
        }

    }

}