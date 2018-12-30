package com.example.nhran.whatsit

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class EbaySearchArrayAdapter (private var activity: Activity, private var items: ArrayList<EbaySearchModel>) :  BaseAdapter(){
    private class ViewHolder(row: View?) {
        var lblPrice: TextView? = null
        var lblDescription: TextView? = null
        var img: ImageView? = null
        var lblTitle: TextView? = null
        init {
            this.lblPrice = row?.findViewById(R.id.price)
            this.lblDescription = row?.findViewById(R.id.description)
            this.img = row?.findViewById(R.id.imageView)
            this.lblTitle = row?.findViewById(R.id.title)
        }
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder
        if (convertView == null) {
            val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.ebay_search_results_item, null)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        val result = items[position]
        viewHolder.lblPrice?.text = result.price
        viewHolder.lblDescription?.text = result.description
        viewHolder.lblTitle?.text = result.title
        //viewHolder.img?.setImageResource(result.image!!)

        return view as View
    }
    override fun getItem(i: Int): EbaySearchModel {
        return items[i]
    }
    override fun getItemId(i: Int): Long {
        return i.toLong()
    }
    override fun getCount(): Int {
        return items.size
    }
}