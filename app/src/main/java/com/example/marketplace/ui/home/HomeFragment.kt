package com.example.marketplace.ui.home

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marketplace.ProductAdapter
import com.example.marketplace.ProductEntity
import com.example.marketplace.R
import com.example.marketplace.databinding.FragmentHomeBinding
import com.google.firebase.firestore.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class HomeFragment : Fragment(),SearchView.OnQueryTextListener, AdapterView.OnItemSelectedListener {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var recycleView: RecyclerView
    private var db = FirebaseFirestore.getInstance()
    private var listProduct = mutableListOf<ProductEntity>()
    private lateinit var productAdapter: ProductAdapter

    private lateinit var svSearch : SearchView

    private lateinit var spinnerCategory: Spinner
    private lateinit var spinnerSeller: Spinner
    private var listCategory = arrayListOf<String>()
    private var listSeller = arrayListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Recycler
        recycleView = root.findViewById<RecyclerView>(R.id.reclycler)
        recycleView.layoutManager = LinearLayoutManager(activity)
        recycleView.setHasFixedSize(true)

        getAllProduct()
        productAdapter = ProductAdapter(listProduct);
        recycleView.adapter = productAdapter;

        //Search
        svSearch =  root.findViewById<SearchView>(R.id.svSearch)
        svSearch.setOnQueryTextListener(this)

        //Spinner Category
        spinnerCategory = root.findViewById(R.id.category_spinner)
        spinnerCategory.onItemSelectedListener  = this
        listCategory.add(resources.getString(R.string.test_All))

        activity?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_item,
                listCategory
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerCategory.adapter = adapter
            }
        }

        //Spinner Seller
        spinnerSeller = root.findViewById(R.id.seller_spinner)
        spinnerSeller.onItemSelectedListener = this
        listSeller.add(resources.getString(R.string.test_All))

        activity?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_item,
                listSeller
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerSeller.adapter = adapter
            }
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getAllProduct() {

        db.collection("product").get().addOnSuccessListener { result ->
            for (document in result) {
                Log.d(TAG, "${document.id} => ${document.data}")

                if(!listCategory.contains(document.data["category"].toString())){
                    listCategory.add(document.data["category"].toString())
                }

                if(!listSeller.contains(document.data["seller"].toString())){
                    listSeller.add(document.data["seller"].toString())
                }

                listProduct.add(
                    ProductEntity(
                        document.data["imagen"].toString(),
                        document.data["title"].toString(),
                        document.data["cost"].toString(),
                        document.data["category"].toString(),
                        document.data["seller"].toString()
                    )
                )
            }
            productAdapter.notifyDataSetChanged();
        }

    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        return  true;
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        listProduct.clear()

        if(newText!!.isNotEmpty()){
            db.collection("product")
                .whereGreaterThanOrEqualTo("title", newText!!)
                .whereLessThanOrEqualTo("title", (newText!!+"\uF7FF"))
                .get().addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d(TAG, "${document.id} => ${document.data}")
                        listProduct.add(
                            ProductEntity(
                                document.data["imagen"].toString(),
                                document.data["title"].toString(),
                                document.data["cost"].toString(),
                                document.data["category"].toString(),
                                document.data["seller"].toString()
                            )
                        )
                    }
                    productAdapter.notifyDataSetChanged();
                }

        }else{
            getAllProduct()
        }

        return  true;
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }


}