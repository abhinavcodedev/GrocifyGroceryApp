package com.example.groceryuserapp.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.GeneratedAdapter
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.groceryuserapp.R
import com.example.groceryuserapp.adapters.AdapterCartProducts
import com.example.groceryuserapp.databinding.FragmentOrderDetailBinding
import com.example.groceryuserapp.viewmodels.UserViewModel
import kotlinx.coroutines.launch


class OrderDetailFragment : Fragment() {
    private val viewModel : UserViewModel by viewModels()
    private lateinit var binding:FragmentOrderDetailBinding
    private lateinit var adapterCartProducts: AdapterCartProducts
    private var status :Int=0
    private var orderId = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderDetailBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        setStatusBarColor()
        getValue()
        //settingStatus()
        lifecycleScope.launch { getOrderedProducts() }
        onBackButtonClick()
        return binding.root
    }

    private fun onBackButtonClick() {
        binding.tbOrderDetailsFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_orderDetailFragment_to_ordersFragment)
        }
    }

    suspend fun getOrderedProducts() {
        viewModel.getOrderedProducts(orderId).collect{cartList->
            adapterCartProducts = AdapterCartProducts()
            binding.rvProductsItems.adapter = adapterCartProducts
            adapterCartProducts.differ.submitList(cartList)
        }
    }

    /*private fun settingStatus() {
        /*when(status){
            0->{
                binding.iv1.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue)
            }1->{
                binding.iv1.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue)
                binding.iv2.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue)
                binding.view1.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue)
            }2->{
                binding.iv1.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue)
                binding.iv1.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue)
                binding.view1.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue)
                binding.iv3.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue)
                binding.view2.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue)
            }3->{
                binding.iv1.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue)
                binding.iv1.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue)
                binding.view1.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue)
                binding.iv3.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue)
                binding.view2.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue)
                binding.iv4.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue)
                binding.view3.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue)
            }
        }*/

        val statusToViews = mapOf(
            0 to listOf(binding.iv1),
            1 to listOf(binding.iv1, binding.iv2, binding.view1),
            2 to listOf(binding.iv1, binding.iv2, binding.view1, binding.iv3, binding.view2),
            3 to listOf(binding.iv1, binding.iv2, binding.view1, binding.iv3, binding.view2, binding.iv4, binding.view3)
        )
        val viewsToTint = statusToViews.getOrDefault(status, emptyList())

        for (view in viewsToTint){
            view.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.blue)
        }

    }*/

    private fun getValue() {
        val bundle =arguments
        status= bundle?.getInt("status")!!
        orderId= bundle.getString("orderId").toString()
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