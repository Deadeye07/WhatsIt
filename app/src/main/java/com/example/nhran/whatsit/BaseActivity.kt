package com.example.nhran.whatsit

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

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
    fun doSearch(view: View) {
        val editText = findViewById<EditText>(R.id.editSearch)
        val searchTerm = editText.text.toString()

        //Go to search activity and pass in search term
        val intent = Intent(this, SearchActivity::class.java).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, searchTerm)
        }
        startActivity(intent)
    }

//    fun onSearchFieldTap(view: View){
//        //Show textfield drop down
//        //val popup = PopupMenu(this, view)
//        //val inflater: MenuInflater = popup.menuInflater
//        //inflater.inflate(R.menu.searchmenu, popup.menu)
//        //popup.show()
//
//        val toolbar = findViewById<LinearLayout>(R.id.toolbarMainLayout)
//        toolbar.layoutParams.height = 400
//        toolbar.requestLayout()
//    }
}
