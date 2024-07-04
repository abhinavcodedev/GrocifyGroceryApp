package com.example.groceryadminapp.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController

import com.example.groceryadminapp.R
import com.example.groceryadminapp.adapter.AdapterOrders
import com.example.groceryadminapp.databinding.FragmentOrderBinding
import com.example.groceryadminapp.models.OrderedItems
import com.example.groceryadminapp.viewmodels.AdminViewModel
import kotlinx.coroutines.launch

class OrderFragment : Fragment() {
    private lateinit var binding: FragmentOrderBinding
    private val viewModel : AdminViewModel by viewModels()
    private lateinit var adapterOrders: AdapterOrders
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        setStatusBarColor()

        getAllOrders()
        return binding.root
    }


    private fun getAllOrders() {
        lifecycleScope.launch {
            viewModel.getAllOrders().collect{orderList->
                if (orderList.isNotEmpty()){

                    val orderedlist = ArrayList<OrderedItems>()

                    for (orders in orderList){
                        val title = StringBuilder()
                        var totalPrice = 0

                        // Check if orderList is not null before iterating over it
                        orders.orderList?.let { productList ->
                            for (products in productList) {
                                // Ensure productPrice is not null before operating on it
                                val price = products.productPrice?.substring(1)?.toIntOrNull()
                                val itemCount = products.productCount ?: 0
                                if (price != null) {
                                    totalPrice += price * itemCount
                                }

                                // Append product category to title
                                products.productCategory?.let { category ->
                                    title.append("$category,")
                                }
                            }
                        }
                        /*for(products in orders.orderList!!){
                             val price = products.productPrice?.substring(1)?.toInt() //₹18
                             val itemCount = products.productCount!!
                             totalPrice += (price?.times(itemCount)!!)

                             title.append("${products.productCategory},")
                         }*/

                        val orderedItems = OrderedItems(orders.orderId, orders.orderDate, orders.orderStatus, title.toString(), totalPrice, orders.userAddress)
                        orderedlist.add(orderedItems)
                    }
                    adapterOrders = AdapterOrders(requireContext(),::onOrderedItemViewClick)
                    binding.rvOders.adapter = adapterOrders
                    adapterOrders.differ.submitList(orderedlist)
                }
            }
        }
    }


    fun onOrderedItemViewClick(orderedItems: OrderedItems){
        val bundle =Bundle()
        bundle.putInt("status",orderedItems.itemStatus!!)
        bundle.putString("orderId",orderedItems.orderId)
        bundle.putString("userAddress",orderedItems.userAddress)

        findNavController().navigate(R.id.action_orderFragment_to_orderDetailsFragment, bundle)
    }



    private fun setStatusBarColor() {
        activity?.window?.apply {
            val statusBarColors = ContextCompat.getColor(requireContext(), R.color.royal_orange)
            statusBarColor = statusBarColors
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }

        }

    }
}