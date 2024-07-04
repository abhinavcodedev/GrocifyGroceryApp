package com.example.groceryuserapp.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.groceryuserapp.CartListener
import com.example.groceryuserapp.R
import com.example.groceryuserapp.Utils
import com.example.groceryuserapp.adapters.AdapterPoduct
import com.example.groceryuserapp.databinding.FragmentCategoryBinding
import com.example.groceryuserapp.databinding.ItemViewProductBinding
import com.example.groceryuserapp.models.Product
import com.example.groceryuserapp.roomdb.CartProductTable
import com.example.groceryuserapp.viewmodels.UserViewModel
import kotlinx.coroutines.launch

class CategoryFragment : Fragment() {
    private lateinit var binding: FragmentCategoryBinding
    private  var category : String?=null
    private val viewModel : UserViewModel by viewModels()
    private lateinit var adapterProduct: AdapterPoduct
    private var cartListener : CartListener?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        getProductCategory()
        setToolBarTitle()
        onSearchMenuClick()
        fetchCategoryProduct()
        onNavigationIconClick()
        return binding.root
    }

    private fun onNavigationIconClick() {
        binding.tbSearchFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_categoryFragment_to_homeFragment2)
        }
    }

    private fun onSearchMenuClick() {
        binding.tbSearchFragment.setOnMenuItemClickListener {menuItem->
            when(menuItem.itemId){
                R.id.searchMenu ->{
                    findNavController().navigate(R.id.action_categoryFragment_to_searchFragment)
                    true
                }
                else-> {false}
            }
        }
    }

    private fun fetchCategoryProduct() {
        lifecycleScope.launch {
            viewModel.getCategryProduct(category!!).collect{
                if (it.isEmpty()){
                    binding.rvProducts.visibility = View.GONE
                    binding.tvText.visibility = View.VISIBLE
                }
                else{
                    binding.rvProducts.visibility = View.VISIBLE
                    binding.tvText.visibility = View.GONE
                }
                adapterProduct = AdapterPoduct(::onAddButtonClicked, ::onIncrementBtnClicked, ::onDecrementBtnClicked)
                binding.rvProducts.adapter = adapterProduct
                adapterProduct.differ.submitList(it)
            }
        }
    }

    private fun setToolBarTitle() {
        binding.tbSearchFragment.title = category
    }

    private fun getProductCategory() {
        val bundle = arguments
        category = bundle?.getString("category")
    }

    private fun onAddButtonClicked(product: Product, productBinding: ItemViewProductBinding){
        productBinding.tvAdd.visibility = View.GONE
        productBinding.llProductCount.visibility = View.VISIBLE

        //step 1. update views
        var itemCount = productBinding.tvProductCount.text.toString().toInt()
        itemCount++
        productBinding.tvProductCount.text = itemCount.toString()

        cartListener?.showCartlayout(1)



        //step 2. values store in db
        product.itemCount = itemCount
        lifecycleScope.launch {
            cartListener?.savingCartItemCount(1)
            saveProductInRoomdb(product)
            viewModel.updateItemCount(product,itemCount)
        }


    }


    private fun onIncrementBtnClicked(product: Product, productBinding: ItemViewProductBinding){
        var itemCountInc = productBinding.tvProductCount.text.toString().toInt()
        itemCountInc++

        if(product.productStock!! +1 > itemCountInc){
            productBinding.tvProductCount.text = itemCountInc.toString()

            cartListener?.showCartlayout(1)

            //step 2
            product.itemCount = itemCountInc
            lifecycleScope.launch {
                cartListener?.savingCartItemCount(1)
                saveProductInRoomdb(product)
                viewModel.updateItemCount(product,itemCountInc)
            }
        }
        else
        {
            Utils.showToast(requireContext(), "Can't add more item of this product")
        }


    }


    fun onDecrementBtnClicked(product: Product, productBinding: ItemViewProductBinding){
        var itemCountDec = productBinding.tvProductCount.text.toString().toInt()
        itemCountDec--

        //step 2
        product.itemCount = itemCountDec
        lifecycleScope.launch {
            cartListener?.savingCartItemCount(-1)
            saveProductInRoomdb(product)
            viewModel.updateItemCount(product,itemCountDec)
        }

        if(itemCountDec > 0){
            productBinding.tvProductCount.text = itemCountDec.toString()
        }
        else{
            lifecycleScope.launch {
                viewModel.deleteCartProduct(product.productRandomId!!)
            }
            productBinding.tvAdd.visibility = View.VISIBLE
            productBinding.llProductCount.visibility = View.GONE
            productBinding.tvProductCount.text = "0"
        }

        cartListener?.showCartlayout(-1)


    }


    private fun saveProductInRoomdb(product: Product) {
        val cartProduct = CartProductTable(
            productId = product.productRandomId!!,
            productTitle = product.productTitle,
            productQuantity = product.productQuantity.toString() + product.productUnit.toString(),
            productPrice = "₹" + "${product.productPrice}",
            productCount = product.itemCount,
            productStock = product.productStock,
            productImage = product.productImageUris?.get(0)!!,
            productCategory = product.productCategory,
            adminUid = product.adminUid,

        )
        lifecycleScope.launch {
            viewModel.insertCartProduct(cartProduct)
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CartListener){
            cartListener = context
        }
        else{
            throw ClassCastException("Please implement Cart Listener")
        }
    }
}