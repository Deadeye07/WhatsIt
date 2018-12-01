package com.example.nhran.whatsit

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.AbsListView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupMenu
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        //LOAD BANNER AD
        MobileAds.initialize(this, "ca-app-pub-2805994884844744~5962103159")
        val mAdView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        //SET LISTENER FOR KEYBOARD ENTER ON SEARCHFIELD
        val searchField = findViewById<EditText>(R.id.editSearch)

        searchField.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                //Perform Code
                doSearch(searchField)
                return@OnKeyListener true
            }
            false
        })
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.maintoolbar, menu)
        return true
    }
    fun doSearch(view: View) {
        val editText = findViewById<EditText>(R.id.editSearch)
        val searchTerm = editText.text.toString()

        //Go to search activity and pass in search term
        val intent = Intent(this, SearchActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, searchTerm)
        }
        startActivity(intent)
    }
    fun onSearchFieldTap(view: View){
        //Show textfield drop down
        //val popup = PopupMenu(this, view)
        //val inflater: MenuInflater = popup.menuInflater
        //inflater.inflate(R.menu.searchmenu, popup.menu)
        //popup.show()

        val toolbar = findViewById<LinearLayout>(R.id.toolbarMainLayout)
        toolbar.layoutParams.height = 400
        toolbar.requestLayout()
    }
}
