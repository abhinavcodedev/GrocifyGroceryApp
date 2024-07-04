package com.example.groceryuserapp.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.groceryuserapp.R
import com.example.groceryuserapp.adapters.AdapterOrders
import com.example.groceryuserapp.databinding.FragmentOrdersBinding
import com.example.groceryuserapp.models.OrderedItems
import com.example.groceryuserapp.viewmodels.UserViewModel
import kotlinx.coroutines.launch


class OrdersFragment : Fragment() {

    private val viewModel : UserViewModel by viewModels()
    private lateinit var binding: FragmentOrdersBinding
    private lateinit var adapterOrders: AdapterOrders
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrdersBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        setStatusBarColor()
        onBackButtonClick()
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

                        val orderedItems = OrderedItems(orders.orderId, orders.orderDate, orders.orderStatus, title.toString(), totalPrice)
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

        findNavController().navigate(R.id.action_ordersFragment_to_orderDetailFragment, bundle)
    }

    private fun onBackButtonClick() {
        binding.tbProfileFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_ordersFragment_to_profileFragment)
        }
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