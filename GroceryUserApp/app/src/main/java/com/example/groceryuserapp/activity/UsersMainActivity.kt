package com.example.groceryuserapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.example.groceryuserapp.CartListener
import com.example.groceryuserapp.R
import com.example.groceryuserapp.adapters.AdapterCartProducts
import com.example.groceryuserapp.databinding.ActivityUsersMainBinding
import com.example.groceryuserapp.databinding.BsCartproductsBinding
import com.example.groceryuserapp.roomdb.CartProductTable
import com.example.groceryuserapp.viewmodels.UserViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class UsersMainActivity : AppCompatActivity(), CartListener {
    private val viewModel : UserViewModel by viewModels()
    private lateinit var binding: ActivityUsersMainBinding
    private lateinit var cartProductList : List<CartProductTable>
    private lateinit var adapterCartProducts: AdapterCartProducts
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getAllCartProducts()
        getTotalItemCountInCart()
        onCartCLicked()
        onNextButtonClicked()
    }

    private fun onNextButtonClicked() {
        binding.btnNxt.setOnClickListener {
            startActivity(Intent(this, OrderPlaceActivity::class.java))
        }
    }

    private fun getAllCartProducts() {
            viewModel.getAll().observe(this){
                for (i in it){
                    cartProductList = it
                }
            }
    }

    private fun onCartCLicked() {
        binding.llItemCart.setOnClickListener {
            val bsCartProductsBinding  = BsCartproductsBinding.inflate(LayoutInflater.from(this))

            val bs = BottomSheetDialog(this)
            bs.setContentView(bsCartProductsBinding.root)


            bsCartProductsBinding.tvNoOfProductCount.text = binding.tvNoOfProductCount.text

            bsCartProductsBinding.btnNxt.setOnClickListener {
                startActivity(Intent(this, OrderPlaceActivity::class.java))
            }

            adapterCartProducts = AdapterCartProducts()
            bsCartProductsBinding.rvProductsItems.adapter = adapterCartProducts
            adapterCartProducts.differ.submitList(cartProductList)

            bs.show()
        }
    }

    private fun getTotalItemCountInCart() {
        viewModel.fetchTotalCartItemCount().observe(this){
           if (it > 0){
               binding.llCart.visibility = View.VISIBLE
               binding.tvNoOfProductCount.text = it.toString()
           }
            else{
                binding.llCart.visibility = View.GONE
           }
        }
    }


    override fun showCartlayout(itemCount : Int) {
        val previousCount = binding.tvNoOfProductCount.text.toString().toInt()
        val updatedCount = previousCount + itemCount

        if (updatedCount > 0){
            binding.llCart.visibility = View.VISIBLE
            binding.tvNoOfProductCount.text = updatedCount.toString()
        }
        else{
            binding.llCart.visibility = View.GONE
            binding.tvNoOfProductCount.text = "0"
        }
    }

    override fun savingCartItemCount(itemCount: Int) {
        viewModel.fetchTotalCartItemCount().observe(this){
            viewModel.savingCartItemCount(it + itemCount)
        }

    }

    override fun hideCartLayout() {
        binding.llCart.visibility = View.GONE
        binding.tvNoOfProductCount.text = "0"
    }


}