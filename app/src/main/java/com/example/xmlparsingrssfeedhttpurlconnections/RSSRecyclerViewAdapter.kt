package com.example.xmlparsingrssfeedhttpurlconnections

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.rss_item_row.view.*

class RSSRecyclerViewAdapter(private val rssList: List<RSSData>): RecyclerView.Adapter<RSSRecyclerViewAdapter.ItemViewHolder>() {
    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.rss_item_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = rssList[position]

        holder.itemView.apply {
            Glide.with(holder.itemView.context).load(item.enclosure).thumbnail(0.2F).into(image_view)
            title.text = item.title
            description.text = item.description
            published.text = item.pubDate

            card_view.setOnClickListener {

                val builder = AlertDialog.Builder(holder.itemView.context)
                builder.setTitle("Do you want to open the article in the app or browser? ")
                builder.setPositiveButton("Browser") { _, _ ->
                    holder.itemView.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(item.link)))
                }

                builder.setNegativeButton("App"){_, _ ->
                    val intent = Intent(holder.itemView.context, OpenURL::class.java)
                    intent.putExtra("url", item.link)
                    intent.putExtra("title", item.title)
                    holder.itemView.context.startActivity(intent)
                    }
                builder.show()

            }
        }
    }

    override fun getItemCount(): Int = rssList.size
}