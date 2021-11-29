package com.example.marketplace
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.graphics.drawable.Drawable
import android.widget.Toast
import com.squareup.picasso.Picasso
import java.io.InputStream
import java.net.URL


public  class CarAdapter(private val dataSet: MutableList<CarEntity>) :
    RecyclerView.Adapter<CarAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var productEntity = dataSet[position]

        holder.titleItems.text = productEntity.title;

        var total = productEntity.cost.toInt() * productEntity.amount.toInt()
        holder.costItems.text = productEntity.cost +" x "+ productEntity.amount + " = " + total

        Picasso.get().load(productEntity.imagen).into(holder.imagenItem);

    }

    override fun getItemCount():Int{
        return dataSet.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleItems :TextView = itemView.findViewById<TextView>(R.id.titleItem)
        val costItems :TextView = itemView.findViewById<TextView>(R.id.costItem)
        val imagenItem :ImageView = itemView.findViewById<ImageView>(R.id.imagenItem)
    }


}