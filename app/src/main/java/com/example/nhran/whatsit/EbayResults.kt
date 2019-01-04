package com.example.nhran.whatsit

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.support.v7.widget.RecyclerView
import android.widget.ListView
import org.json.JSONObject


class EbayResults : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ebay_results)

        val searchResults = intent.getStringExtra(EXTRA_MESSAGE)


        var adapter : EbaySearchArrayAdapter? = null
        var searchList : ArrayList<EbaySearchModel> = generateResultData(searchResults)
        val ebayResultsList = findViewById<ListView>(R.id.ebayResults)

        adapter = EbaySearchArrayAdapter(this, searchList)

        ebayResultsList.adapter = adapter

        val graph = findViewById<View>(R.id.graph) as GraphView
        val series = LineGraphSeries<DataPoint>(
            arrayOf<DataPoint>(
                DataPoint(0.0, 1.0),
                DataPoint(1.0, 5.0),
                DataPoint(2.0, 3.0),
                DataPoint(3.0, 2.0),
                DataPoint(4.0, 6.0)
            )
        )
        graph.addSeries(series)
    }
    private fun generateResultData(results: String?) :ArrayList<EbaySearchModel> {

        var result = ArrayList<EbaySearchModel>()

        var jsonResults = JSONObject(results)
        //turn into json object
        var jsonItems = jsonResults.getJSONArray("items")
        //loop through array of search results
            //create model of each result
        for (i in 0 until jsonItems.length()) {
            var searchResult = EbaySearchModel()
            val item = jsonItems.getJSONObject(i)
            searchResult.price = item.getString("price")
            searchResult.title = item.getString("title")
            searchResult.image = item.getString("picture")
            //searchResult.description = item.getString("")
            searchResult.url = item.getString("url")
            result.add(searchResult)
        }
        return result
    }
}
