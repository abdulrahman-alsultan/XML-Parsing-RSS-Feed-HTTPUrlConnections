package com.example.xmlparsingrssfeedhttpurlconnections

import android.graphics.Point
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL


class DisplayData : AppCompatActivity() {

    private lateinit var fab: FloatingActionButton
    private lateinit var rvRSS: RecyclerView
    private lateinit var adapter: RSSRecyclerViewAdapter
    private lateinit var dataList: MutableList<RSSData>
    private lateinit var ship: ImageView
    private lateinit var constraint: ConstraintLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_data)

        this.title = "NASA Image of the Day"

        fab = findViewById(R.id.floatingActionButton)
        rvRSS = findViewById(R.id.rvRSS)
        dataList = mutableListOf()
        ship = findViewById(R.id.ship)
        constraint = findViewById(R.id.second_constraint)
        rvRSS.isNestedScrollingEnabled = false


        call()
        adapter = RSSRecyclerViewAdapter(dataList)
        rvRSS.layoutManager = LinearLayoutManager(this)


        fab.setOnClickListener {
            rvRSS.visibility = View.GONE
            fab.visibility = View.GONE
            constraint.visibility = View.VISIBLE
            call()
        }


    }

    private fun call(){
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        ship.animate().apply {
            duration = 30000
            translationYBy((-1 * size.y).toFloat() - 90F)
        }.withEndAction{
            ship.y = size.y.toFloat() - 90
            adapter = RSSRecyclerViewAdapter(dataList)
            rvRSS.adapter = adapter
            rvRSS.visibility = View.VISIBLE
            fab.visibility = View.VISIBLE
            constraint.visibility = View.GONE
        }.start()
        CoroutineScope(IO).launch {
            FetchData().execute()
        }
    }


    private inner class FetchData : AsyncTask<Void, Void, MutableList<RSSData>>() {
        val parser = XMLParser()



        override fun doInBackground(vararg params: Void?): MutableList<RSSData> {
            val urlConnection = URL("https://www.nasa.gov/rss/dyn/lg_image_of_the_day.rss").openConnection() as HttpURLConnection
            dataList =
                urlConnection.inputStream?.let {
                    parser.parse(it)
                } as MutableList<RSSData>
            return dataList
        }

        override fun onPostExecute(result: MutableList<RSSData>?) {
            super.onPostExecute(result)

            val display = windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)

            ship.animate().apply {
                duration = 1500
                translationYBy((-1 * size.y).toFloat() - 90F)
            }.withEndAction{
                ship.y = size.y.toFloat() - 90
                adapter = RSSRecyclerViewAdapter(dataList)
                rvRSS.adapter = adapter
                rvRSS.visibility = View.VISIBLE
                fab.visibility = View.VISIBLE
                constraint.visibility = View.GONE
            }.start()


        }
    }

}