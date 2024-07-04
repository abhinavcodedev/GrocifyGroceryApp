package com.example.groceryuserapp.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.groceryuserapp.CartListener
import com.example.groceryuserapp.R
import com.example.groceryuserapp.Utils
import com.example.groceryuserapp.adapters.AdapterCartProducts
import com.example.groceryuserapp.databinding.ActivityOrderPlaceBinding
import com.example.groceryuserapp.databinding.AddressLayoutBinding
import com.example.groceryuserapp.models.Orders
import com.example.groceryuserapp.viewmodels.UserViewModel
import kotlinx.coroutines.launch

class OrderPlaceActivity : AppCompatActivity() {
    private val viewModel : UserViewModel by viewModels()
    private lateinit var adapterCartProducts: AdapterCartProducts
    private lateinit var binding: ActivityOrderPlaceBinding
    private var cartListener : CartListener?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setStatusBarColor()
        backToUserMainActivity()
        getAllCartProducts()

        onPlaceOrderClicked()
    }


    private fun onPlaceOrderClicked() {
        binding.btnNxt.setOnClickListener {
            viewModel.getAddressStatus().observe(this){status->
                if(status){
                    //payment work
                    Utils.showToast(this@OrderPlaceActivity, "Payment accept on Cash On Delivery")
                    //order save, delete products
                    saveOrders()
                  //  viewModel.deleteCartProducts()
                    viewModel.savingCartItemCount(0)
                    cartListener?.hideCartLayout()
                    Utils.hideDialog()
                    startActivity(Intent(this@OrderPlaceActivity, UsersMainActivity::class.java))
                    finish()
                }
                else{
                    val addressLayoutBinding = AddressLayoutBinding.inflate(LayoutInflater.from(this))

                    val alertDialog =AlertDialog.Builder(this)
                        .setView(addressLayoutBinding.root)
                        .create()
                    alertDialog.show()

                    addressLayoutBinding.btnAdd.setOnClickListener {
                        saveAddress(alertDialog , addressLayoutBinding)
                    }
                }
            }
        }
    }

    private fun saveOrders() {
        viewModel.getAll().observe(this){cartProductsList->
            if (cartProductsList.isEmpty() || cartProductsList.isNotEmpty()){
                viewModel.getUserAddress {
                        address->
                    val order = Orders(
                        orderId = Utils.getRandomId(),  orderList = cartProductsList,
                        userAddress = address, orderStatus = 0, orderDate = Utils.getCurrentDate(),
                        orderinguserId = Utils.getCurrentUserId()
                    )
                    viewModel.saveOrderedProducts(order)
                }
                for (products in cartProductsList){
                    val count = products.productCount
                    val stock = products.productStock?.minus(count!!)
                    if (stock != null) {
                        viewModel.saveProductsAfterOrder(stock, products)
                    }
                }
            }
        }

    }


    private fun saveAddress(alertDialog: AlertDialog?, addressLayoutBinding: AddressLayoutBinding) {
            Utils.showDialog(this, "Processing...")
            val userPincode = addressLayoutBinding.etPinCode.text.toString()
            val userPhoneNumber = addressLayoutBinding.etPhoneNumber.text.toString()
            val userState = addressLayoutBinding.etState.text.toString()
            val userdistrict = addressLayoutBinding.etDistrict.text.toString()
            val userAddress = addressLayoutBinding.etDescriptiveAddress.text.toString()

            val address ="$userPincode, $userdistrict($userState), $userAddress, $userPhoneNumber"


        lifecycleScope.launch {
            viewModel.saveUserAddress(address)
            viewModel.saveAddressStatus()
        }
        Utils.showToast(this, "Saved...")
        alertDialog?.dismiss()
        Utils.hideDialog()

        //
    }

    private fun backToUserMainActivity() {
        binding.tbOrderActivity.setNavigationOnClickListener {
            startActivity(Intent(this, UsersMainActivity::class.java))
            finish()
        }
    }

    private fun getAllCartProducts() {
        viewModel.getAll().observe(this){cartProductList->

            adapterCartProducts = AdapterCartProducts()
            binding.rvProductsItems.adapter = adapterCartProducts
            adapterCartProducts.differ.submitList(cartProductList)

            var totalPrice = 0
            for(products in cartProductList){
                val price = products.productPrice?.substring(1)?.toInt() //₹18
                val itemCount = products.productCount!!
                totalPrice += (price?.times(itemCount)!!)
            }

            binding.tvSubTotal.text = totalPrice.toString()
            if (totalPrice <200){
                binding.tvDeliveryCharge.text = "₹15"
                totalPrice+=15
            }

            binding.tvGrandTotal.text = totalPrice.toString()
        }
    }

    private fun setStatusBarColor(){
        window?.apply {
            val statusBarColors = ContextCompat.getColor(this@OrderPlaceActivity, R.color.royal_orange)
            statusBarColor = statusBarColors
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }
}