package com.levp.getdatabynet.numFactApi

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.viewModels
import com.levp.getdatabynet.R
import com.levp.getdatabynet.questionApi.toast

import kotlinx.android.synthetic.main.activity_numfacts.*
import kotlinx.android.synthetic.main.activity_numfacts.progressBar



import java.util.*



class NumFactActivity : AppCompatActivity() {


    private val viewModel: NumFactViewModel by viewModels()
    var currNum: Long? = null

    private val types = arrayListOf<String>("trivia", "math", "year", "date")
    var type: String = types[Random().nextInt(3)]


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_numfacts)
        viewModel.uiStateNF().observe(this, { uiState ->
            if (uiState != null) {
                render(uiState)
            }
        })
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        var day = c.get(Calendar.DAY_OF_MONTH)


        val spinner = findViewById<Spinner>(R.id.numFacts_spinner)
        val adapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(
            this,
            R.array.num_facts_types,
            android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
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

                    num_facts_ET.setText(currNum?.toString() ?: "")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        viewModel.getRandomFact(type)

        num_facts_ET.setOnClickListener {
            if (type == "date") {
                Log.d("datepick", "date picker created")

                val dpd = DatePickerDialog(this, { _, _, monthOfYear, dayOfMonth ->

                    num_facts_ET.setText("$dayOfMonth.${monthOfYear + 1}")
                    month = monthOfYear
                    day = dayOfMonth
                }, year, month, day)

                dpd.show()
            }
        }

        next_numfact_btn.setOnClickListener {
            val curr = num_facts_ET.text.toString().toLongOrNull()
            currNum = curr ?: currNum

            if (currNum != null || type == "date") {
                val date = num_facts_ET.text.toString()
                viewModel.getFact(currNum ?: 0, type, date)
            } else {
                viewModel.getRandomFact(type)
            }
        }
    }

    private fun render(uiState: UiStateNF) {
        when (uiState) {
            is UiStateNF.Loading -> {
                onLoad()
            }
            is UiStateNF.Success -> {
                onSuccess(uiState)

            }
            is UiStateNF.Error -> {
                onError(uiState)
            }
        }
    }

    private fun onLoad() {
        progressBar.visibility = View.VISIBLE
        next_numfact_btn.isEnabled = false
    }

    private fun onSuccess(uiState: UiStateNF.Success) {
        next_numfact_btn.isEnabled = true
        progressBar.visibility = View.GONE

        val response = uiState.numFactResponse
        val body = response.body()

        val fact = body?.text ?: "whoops..."
        num_fact_TW.text = fact
    }

    private fun onError(uiState: UiStateNF.Error) {
        progressBar.visibility = View.GONE
        next_numfact_btn.isEnabled = true
        toast(uiState.message)
    }
}