package com.levp.getdatabynet.numFactApi

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.gson.GsonBuilder
import com.levp.getdatabynet.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_numfacts.*

import kotlinx.coroutines.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS

@DelicateCoroutinesApi
class NumFactActivity : AppCompatActivity() {

    private val BASE_URL_NUMBER_FACT = "http://numbersapi.com"
    private val NOT_LOADED_STR = "not loaded yet"
    val gson = GsonBuilder().setLenient().create()

    var currText: String? = null
    var currNum: Long? = null
    // ? enum class FactType{ TRIVIA, MATH, YEAR, DATE}
    // ? var currType : FactType = FactType.TRIVIA

    private val types = arrayListOf<String>("trivia", "math", "year", "date")
    var type: String = types[Random().nextInt(3)]


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_numfacts)

        val c = Calendar.getInstance()
        var year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        var day = c.get(Calendar.DAY_OF_MONTH)

        GlobalScope.launch(Dispatchers.Main) {
            getRandomFact(type)
            num_fact_TW.text = currText
        }
        val spinner = findViewById<Spinner>(R.id.numFacts_spinner)
        val adapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(
            this,
            R.array.num_facts_types,
            android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                itemSelected: View?, selectedItemPosition: Int, selectedId: Long
            ) {
                val choose = resources.getStringArray(R.array.num_facts_types)
                type = choose[selectedItemPosition].toLowerCase(Locale.ROOT)

                num_facts_ET.isFocusable = type != "date"
                num_facts_ET.isFocusableInTouchMode = type != "date"

                if (type == "date") {
                    num_facts_ET.setText("$day.${month + 1}")
                } else {
                    //Log.e("currNum", "${currNum}")
                    num_facts_ET.setText(currNum?.toString() ?: "")
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        num_facts_ET.setOnClickListener {
            if (type == "date") {
                Log.d("datepick", "date picker created")
                val check = num_facts_ET.text.toString()
                if (check.contains('.')) {
                    val txt = check.split(".")


                }
                val dpd = DatePickerDialog(this, { _, _, monthOfYear, dayOfMonth ->

                    // Display Selected date in textbox
                    num_facts_ET.setText("$dayOfMonth.${monthOfYear + 1}")
                    month = monthOfYear
                    day = dayOfMonth
                }, year, month, day)

                dpd.show()
            }
        }

        next_numfact_btn.setOnClickListener {

            val currNum = num_facts_ET.text.toString().toLongOrNull()
            if (currNum != null)
                GlobalScope.launch(Dispatchers.Main) {
                    getFact(currNum, type)

                    num_fact_TW.text = currText
                }
            else {
                GlobalScope.launch(Dispatchers.Main) {
                    getRandomFact(type)
                    num_fact_TW.text = currText
                }
            }
        }
    }

    suspend fun getFact(num: Long, fType: String) {
        val fact = CompletableDeferred<String>()
        val number = CompletableDeferred<Long>()

        val api = Retrofit.Builder()
            .baseUrl(BASE_URL_NUMBER_FACT)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(NumFactApi::class.java)

        GlobalScope.launch(Dispatchers.IO) {

            //Log.d("launch", "num coroutine launched")

            val response: Response<NumFact> = when (fType) {
                "trivia" -> {
                    api.getByValueTrivia(num)
                }
                "math" -> {
                    api.getByValueMath(num)
                }
                "year" -> {
                    api.getByValueYear(num)
                }
                "date" -> {
                    val txt = num_facts_ET.text.toString().split(".")
                    api.getByDate(txt[1].toLong(), txt[0].toLong())
                }
                else -> {
                    api.getRandom()
                }
            }

            if (response.isSuccessful) {
                val ans = response.body()
                fact.complete(ans?.text ?: NOT_LOADED_STR)
                number.complete(ans?.number ?: 404)
            } else {
                Log.e("response", "failed to load data")
            }
//            api.getComments().enque(object : Callback<List<Comment>> {
//                override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
//                    Log.e("response", "error occured")
//                }
//
//                override fun onResponse(
//                    call: Call<List<Comment>>,
//                    response: Response<List<Comment>>
//                ) {
//                    response.body()?.let {
//                        for (comment in it) {
//                            Log.d("response", comment.toString)
//                        }
//                    }
//                }
//            })
        }
        currText = fact.await()
        currNum = number.await()

        Log.e("number fact", "number:$currNum\nfact:$currText")

    }

    suspend fun getRandomFact(fType: String) {
        //Log.e("fetching random fact", "type:$fType")
        val fact = CompletableDeferred<String>()
        val number = CompletableDeferred<Long>()


        val api = Retrofit.Builder()
            .baseUrl(BASE_URL_NUMBER_FACT)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(NumFactApi::class.java)

        GlobalScope.launch(Dispatchers.IO) {

            Log.d("launch", "num coroutine launched")
            val response = if (fType != "date") {
                api.getRandomByType(fType)
            } else {
                val txt = num_facts_ET.text.toString().split(".")
                api.getByDate(txt[1].toLong(), txt[0].toLong())
            }

            if (response.isSuccessful) {
                val ans = response.body()
                fact.complete(ans?.text ?: NOT_LOADED_STR)
                number.complete(ans?.number ?: 404)
            } else {
                Log.e("response", "failed to load data")
            }

        }
        currText = fact.await()
        if (type != "date")
            currNum = number.await()

        Log.e("number fact", "number:$currNum\nfact:$currText")

    }
}