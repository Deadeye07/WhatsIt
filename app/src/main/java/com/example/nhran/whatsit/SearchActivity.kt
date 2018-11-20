package com.example.nhran.whatsit

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuInflater
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import android.R.attr.duration
import android.support.design.widget.Snackbar
import android.view.View


class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)


        // Get the Intent that started this activity and extract the string
        val searchTerm = intent.getStringExtra(EXTRA_MESSAGE)
        doSearch(searchTerm)


    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.maintoolbar, menu)
        return true
    }

    fun doSearch(term:String){
        val builder = Uri.Builder()
        builder.scheme("http")
            .authority("svcs.ebay.com")
            .appendPath("services")
            .appendPath("search")
            .appendPath("FindingService")
            .appendPath("v1")
            .appendQueryParameter("keywords", term.toString())
            .appendQueryParameter("OPERATION-NAME", "findItemsByKeywords")
            .appendQueryParameter("SERVICE-VERSION", "1.0.0")
            .appendQueryParameter("SECURITY-APPNAME", "Nicholas-WhatsIt-PRD-07f960b4b-63e9548e")
            .appendQueryParameter("GLOBAL-ID", "EBAY-US")
            .appendQueryParameter("RESPONSE-DATA-FORMAT", "JSON")

        val myUrl = builder.build().toString()

// Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)

// Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, myUrl,
            Response.Listener<String> { response ->
                //Take JSON and put into an array
                val myItems =arrayListOf<String>()

                val jObject = JSONObject(response)

                val test = jObject.getJSONArray("findItemsByKeywordsResponse").getJSONObject(0)

                val searchResult = test.getJSONArray("searchResult").getJSONObject(0)

                val items = searchResult.getJSONArray("item")

                val allPrices = arrayListOf<Int>()

                for (i in 0 until items.length()) {
                    val item = items.getJSONObject(i)
                    val sellingStatus = item.getJSONArray("sellingStatus")
                    val price = sellingStatus.getJSONObject(0)
                    val priceArray = price.getJSONArray("currentPrice")
                    val priceGetingCloser = priceArray.getJSONObject(0)
                    val finalPrice = priceGetingCloser.getInt("__value__")
                    //val name = item.getJSONArray("name").toInt()
                    allPrices.add(finalPrice)
                    //myItems.add(title.toString())
                }
                val averagePrice = allPrices.average().toString()
                val lowestPrice  = allPrices.min().toString()
                val highestPrice = allPrices.max().toString()
                //Create array adapter
                //val newAdapter =  ArrayAdapter<String>(this, R.layout.platform_results, averagePrice)

                //GEt the listview to populate
                val averageTextField = findViewById<TextView>(R.id.ebayAverage)
                val highTextField =findViewById<TextView>(R.id.highPrice)
                val lowTextField =findViewById<TextView>(R.id.lowPrice)

                averageTextField.setText(averagePrice)
                highTextField.setText(highestPrice)
                lowTextField.setText(lowestPrice)

            },
            Response.ErrorListener {
                Snackbar.make(findViewById(R.id.activitySearch),R.string.error, 5000).show()
            })

// Add the request to the RequestQueue.
        queue.add(stringRequest)
    }
}
