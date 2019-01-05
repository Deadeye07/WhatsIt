package com.example.nhran.whatsit

import android.net.Uri
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuInflater
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.widget.*
import com.amazonaws.HttpMethod
import com.amazonaws.http.HttpMethodName
import com.amazonaws.mobile.api.idhxyqcn3bb3.EbaySearchMobileHubClient
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory
import com.amazonaws.mobileconnectors.apigateway.ApiRequest
import com.amazonaws.util.IOUtils
import com.amazonaws.util.StringUtils
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.example.nhran.whatsit.R.id.graph
import com.jjoe64.graphview.helper.StaticLabelsFormatter
import kotlin.concurrent.thread

class SearchActivity : BaseActivity() {
    companion object {
        private val TAG = this::class.java.simpleName
    }
   // private var apiClient: EbaySearchMobileHubClient? = null
    var searchResults:String? = null
    var searchTerm:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        // Get the Intent that started this activity and extract the string
        searchTerm = intent.getStringExtra(EXTRA_MESSAGE)

        //Place search term in search bar
        val editText = findViewById<EditText>(R.id.editSearch)
        editText.setText(searchTerm)

        //Do ebay search
        //doSearch(searchTerm)


        //Set on click listener on tile to go to platform detail activity
        val ebayTile = findViewById<View>(R.id.ebayTile)
        ebayTile.setOnClickListener(View.OnClickListener { ebayTileClicked()})


        //Call test API

        callCloudLogic(searchTerm)
    }

    private fun callCloudLogic(body: String) {

        var apiClient = ApiClientFactory()
            .credentialsProvider(AWSMobileClient.getInstance().credentialsProvider)
            .build(EbaySearchMobileHubClient::class.java)

        val parameters = mapOf("lang" to "en_US")
        val headers = mapOf("Content-Type" to "application/json")

        val request = ApiRequest(apiClient::class.java.simpleName)
            .withPath("/ebay")
            .withHttpMethod(HttpMethodName.GET)
            .withHeaders(headers)
            .withParameters(parameters)
        if (body.isNotEmpty()) {
            val content = body.toByteArray()
            val contentLength = content.count()
            request
                .addHeader("Content-Length", contentLength.toString())
                .withBody(content)
        }
        thread(start = true) {
            try {
                Log.d(TAG, "Invoking API")
                val response = apiClient.execute(request)
                val responseContentStream = response.getContent()
                if (responseContentStream != null) {
                    val responseData = IOUtils.toString(responseContentStream)
                    // Do something with the response data here
                    doSearch(responseData)
                    Log.d(TAG, responseData)
                }
            } catch (ex: Exception) {
                Log.e(TAG, "Error invoking API")
            }
        }

    }

    private fun doSearch(response: String) {
        //Create graph
        val responseJSON = JSONObject(response)
        searchResults = response

        val graphJSONData = responseJSON.getJSONArray("graph")

        val groupOne = graphJSONData.getJSONObject(0).getString("group")
        val graph = findViewById<View>(R.id.miniGraph) as GraphView
        // use static labels for horizontal and vertical labels
        val staticLabelsFormatter = StaticLabelsFormatter(graph)
        staticLabelsFormatter.setHorizontalLabels(arrayOf(graphJSONData.getJSONObject(0).getString("group"), graphJSONData.getJSONObject(1).getString("group"),graphJSONData.getJSONObject(2).getString("group"),graphJSONData.getJSONObject(3).getString("group"), graphJSONData.getJSONObject(4).getString("group")))
        graph.gridLabelRenderer.labelFormatter = staticLabelsFormatter
        val series = BarGraphSeries<DataPoint>(
            arrayOf(
                DataPoint(0.0, graphJSONData.getJSONObject(0).getString("count").toDouble()),
                DataPoint(1.0, graphJSONData.getJSONObject(1).getString("count").toDouble()),
                DataPoint(2.0, graphJSONData.getJSONObject(2).getString("count").toDouble()),
                DataPoint(3.0, graphJSONData.getJSONObject(3).getString("count").toDouble()),
                DataPoint(4.0, graphJSONData.getJSONObject(4).getString("count").toDouble())

            )
        )
        graph.addSeries(series)
        //GEt the listview to populate
        val averageTextField = findViewById<TextView>(R.id.ebayAverage)
        val highTextField =findViewById<TextView>(R.id.highPrice)
        val lowTextField =findViewById<TextView>(R.id.lowPrice)

        val concatAverage = getString(R.string.dollar_sign) + responseJSON.getString("average")
        val concatHigh = getString(R.string.dollar_sign) + responseJSON.getString("max")
        val concatLow = getString(R.string.dollar_sign) + responseJSON.getString("min")
        runOnUiThread {
            averageTextField.text = concatAverage
            highTextField.text = concatHigh
            lowTextField.text = concatLow
        }

    }

    private fun ebayTileClicked(){
        //Go to search activity and pass in search term

        val intent = Intent(this, EbayResults::class.java)
        val extras = Bundle()
        extras.putString("searchResults", searchResults)
        extras.putString("searchTerm", searchTerm)
        intent.putExtras(extras)
        startActivity(intent)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.maintoolbar, menu)
        return true
    }

//     fun doSearch(term:String){
//        val builder = Uri.Builder()
//        builder.scheme("http")
//            .authority("svcs.ebay.com")
//            .appendPath("services")
//            .appendPath("search")
//            .appendPath("FindingService")
//            .appendPath("v1")
//            .appendQueryParameter("keywords", term.toString())
//            .appendQueryParameter("OPERATION-NAME", "findItemsByKeywords")
//            .appendQueryParameter("SERVICE-VERSION", "1.0.0")
//            .appendQueryParameter("SECURITY-APPNAME", "Nicholas-WhatsIt-PRD-07f960b4b-63e9548e")
//            .appendQueryParameter("GLOBAL-ID", "EBAY-US")
//            .appendQueryParameter("RESPONSE-DATA-FORMAT", "JSON")
//
//        val myUrl = builder.build().toString()
//
//// Instantiate the RequestQueue.
//        val queue = Volley.newRequestQueue(this)
//
//// Request a string response from the provided URL.
//        val stringRequest = StringRequest(
//            Request.Method.GET, myUrl,
//            Response.Listener<String> { response ->
//                //Take JSON and put into an array
//                val myItems =arrayListOf<String>()
//
//                val jObject = JSONObject(response)
//
//                val test = jObject.getJSONArray("findItemsByKeywordsResponse").getJSONObject(0)
//
//                val searchResult = test.getJSONArray("searchResult").getJSONObject(0)
//
//                val items = searchResult.getJSONArray("item")
//
//                val allPrices = arrayListOf<Int>()
//
//                for (i in 0 until items.length()) {
//                    val item = items.getJSONObject(i)
//                    val sellingStatus = item.getJSONArray("sellingStatus")
//                    val price = sellingStatus.getJSONObject(0)
//                    val priceArray = price.getJSONArray("currentPrice")
//                    val priceGetingCloser = priceArray.getJSONObject(0)
//                    val finalPrice = priceGetingCloser.getInt("__value__")
//                    //val name = item.getJSONArray("name").toInt()
//                    allPrices.add(finalPrice)
//                    //myItems.add(title.toString())
//                }
//                val averagePrice = allPrices.average().toString()
//                val lowestPrice  = allPrices.min().toString()
//                val highestPrice = allPrices.max().toString()
//                //Create array adapter
//                //val newAdapter =  ArrayAdapter<String>(this, R.layout.platform_results, averagePrice)
//
//                val rangeIncrement = (highestPrice.toInt().minus(lowestPrice.toInt())) / 3 //Create 3 price groups that will be bars
//
//                val firstIncrement = lowestPrice.toInt() + rangeIncrement
//                val secondIncrement = firstIncrement + rangeIncrement
//                val thirdIncrement = secondIncrement + rangeIncrement
//                //loop through prices, if it's less than increment, then add to that increments count and remove it from price array
//
//                val firstIncrementCount = allPrices.count { it < firstIncrement }
//                val secondIncrementCount = allPrices.count { it in firstIncrement..secondIncrement}
//                val thirdIncrementCount = allPrices.count { it in secondIncrement..thirdIncrement}
//
//
//
//            },
//            Response.ErrorListener {
//                Snackbar.make(findViewById(R.id.activitySearch),R.string.error, 5000).show()
//            })
//
//// Add the request to the RequestQueue.
//        queue.add(stringRequest)
//    }
}
