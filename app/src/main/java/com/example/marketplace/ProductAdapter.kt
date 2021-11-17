package com.example.marketplace
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.graphics.drawable.Drawable
import com.squareup.picasso.Picasso
import java.io.InputStream
import java.net.URL


public  class ProductAdapter(private val dataSet: List<ProductEntity>) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    public class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_product, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var titleItems = holder.itemView.findViewById<TextView>(R.id.titleItem);
        titleItems.text = dataSet[position].title;

        var costItems = holder.itemView.findViewById<TextView>(R.id.costItem);
        costItems.text = dataSet[position].cost;

        var imagenItem = holder.itemView.findViewById<ImageView>(R.id.imagenItem);
        Picasso.get().load(dataSet[position].imagen).into(imagenItem);

        var categoryItem = holder.itemView.findViewById<TextView>(R.id.categoryItem);
        categoryItem.text = dataSet[position].category;

        var sellerItem = holder.itemView.findViewById<TextView>(R.id.sellerItem);
        sellerItem.text = dataSet[position].seller;

    }

    override fun getItemCount() = dataSet.size



}