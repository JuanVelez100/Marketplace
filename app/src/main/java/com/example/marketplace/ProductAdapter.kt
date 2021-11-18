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


public  class ProductAdapter(private val dataSet: MutableList<ProductEntity>) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var productEntity = dataSet[position]

        holder.titleItems.text = productEntity.title;

        holder.costItems.text = productEntity.cost;

        Picasso.get().load(productEntity.imagen).into(holder.imagenItem);

        holder.categoryItem.text = productEntity.category;

        holder.sellerItem.text = productEntity.seller;
    }

    override fun getItemCount():Int{
        return dataSet.size
    }

    public class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val titleItems :TextView = itemView.findViewById<TextView>(R.id.titleItem)
        val costItems :TextView = itemView.findViewById<TextView>(R.id.costItem)
        val imagenItem :ImageView = itemView.findViewById<ImageView>(R.id.imagenItem)
        val categoryItem:TextView = itemView.findViewById<TextView>(R.id.categoryItem);
        var sellerItem :TextView= itemView.findViewById<TextView>(R.id.sellerItem);
    }

}