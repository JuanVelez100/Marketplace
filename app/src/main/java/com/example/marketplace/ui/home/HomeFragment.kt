package com.example.marketplace.ui.home

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marketplace.ProductAdapter
import com.example.marketplace.ProductEntity
import com.example.marketplace.R
import com.example.marketplace.databinding.FragmentHomeBinding
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    var recycleView: RecyclerView? = null;
    var db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //List
        var list :MutableList<ProductEntity> = mutableListOf()

        list.add(ProductEntity("https://firebasestorage.googleapis.com/v0/b/marketplace-18154.appspot.com/o/gafas.jfif?alt=media&token=709bfd9c-e9e3-49fd-b9c2-b045ea3439bd","Gafas Negras","200","Accesorios","RayBan"))

        db.collection("product").get().addOnSuccessListener { documents ->
            for (document in documents) {

                list.add(
                    ProductEntity(
                        document.get("imagen").toString(),
                        document.get("title").toString(),
                        document.get("cost").toString(),
                        document.get("category").toString(),
                        document.get("seller").toString()
                    )
                )
            }
        }

        //Recycler
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recycleView = view.findViewById<RecyclerView>(R.id.reclycler)
        recycleView!!.layoutManager = LinearLayoutManager(activity)
        var adapter = ProductAdapter(list);
        recycleView!!.setHasFixedSize(true);
        recycleView!!.adapter = adapter;


        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}