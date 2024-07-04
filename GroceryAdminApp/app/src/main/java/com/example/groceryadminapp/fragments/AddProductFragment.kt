package com.example.groceryadminapp.fragments

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.groceryadminapp.Constants
import com.example.groceryadminapp.R
import com.example.groceryadminapp.Utils
import com.example.groceryadminapp.activity.AdminMainActivity
import com.example.groceryadminapp.adapter.AdapterSelectedImg
import com.example.groceryadminapp.databinding.FragmentAddProductBinding
import com.example.groceryadminapp.models.Product
import com.example.groceryadminapp.viewmodels.AdminViewModel
import com.google.firebase.storage.internal.Util
import kotlinx.coroutines.launch


class AddProductFragment : Fragment() {

    private val viewModel : AdminViewModel by viewModels()
    private lateinit var binding: FragmentAddProductBinding
    private val imageUris : ArrayList<Uri> = arrayListOf()
    //implicit activity
    val selectedImg = registerForActivityResult(ActivityResultContracts.GetMultipleContents()){listOfUri->
        val fiveImages = listOfUri.take(5)
        imageUris.clear()
        imageUris.addAll(fiveImages)

        binding.rvProductImg.adapter = AdapterSelectedImg(imageUris)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddProductBinding.inflate(layoutInflater)
        setStatusBarColor()
        setAutoCompleteTextView()
        onImgSelectbtnClick()
        onAddProdBtnClick()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun onAddProdBtnClick() {
        binding.btnAddProd.setOnClickListener {
            Utils.showDialog(requireContext(), "Uploading Images...")

            val productTitle = binding.etProductTitle.text.toString()
            val productQuantity = binding.etProductQuantity.text.toString()
            val productUnit = binding.etProductUnit.text.toString()
            val productPrice = binding.etProductPrice.text.toString()
            val productStock = binding.etProductStock.text.toString()
            val productCategory = binding.etProductCategory.text.toString()
            val productType = binding.etProductType.text.toString()

            if(productTitle.isEmpty() || productTitle.isEmpty() || productTitle.isEmpty() || productTitle.isEmpty() || productTitle.isEmpty() || productTitle.isEmpty() ||
            productTitle.isEmpty()){
                Utils.apply {
                    hideDialog()
                    showToast(requireContext(), "Empty fields are not allowed")
                }
            }
            else if (imageUris.isEmpty()){
                Utils.apply {
                    hideDialog()
                    showToast(requireContext(), "Please upload some images")
                }
            }
            else{
                val product = Product(
                    productTitle = productTitle,
                    productQuantity = productQuantity.toInt(),
                    productUnit = productUnit,
                    productPrice = productPrice.toInt(),
                    productStock = productStock.toInt(),
                    productCategory = productCategory,
                    productType = productType,
                    itemCount = 0,
                    adminUid = Utils.getCurrentUserId(),
                    productRandomId = Utils.getRandomId()
                )

                saveImage(product)
            }
        }
    }

    private fun saveImage(product: Product){
        viewModel.saveImageInDB(imageUris)
        lifecycleScope.launch {
            viewModel.isImagesUploaded.collect{
                if (it==true){
                    Utils.apply {
                        hideDialog()
                        showToast(requireContext(), "image saved")
                    }
                    getUrls(product)
                }
            }
        }
    }

    private fun getUrls(product: Product) {
        Utils.showDialog(requireContext(), "Publishing Product..")
        lifecycleScope.launch {
            viewModel.dowloadedUrls.collect{
                val urls = it
                product.productImageUris = urls
                saveProduct(product)
            }
        }
    }

    private fun saveProduct(product: Product) {
        viewModel.saveProduct(product)
        lifecycleScope.launch {
            viewModel.isProductSaved.collect{
                if (it==true){
                    Utils.hideDialog()
                    startActivity(Intent(requireActivity(), AdminMainActivity::class.java))
                    Utils.showToast(requireContext(), "Your Product is Live")
                }
            }
        }
    }

    private fun onImgSelectbtnClick() {
        binding.btnSelectImg.setOnClickListener {
            selectedImg.launch("image/*")
        }
    }

    private fun setAutoCompleteTextView() {
        val units = ArrayAdapter(requireContext(), R.layout.show_list, Constants.allUnitOfProducts)
        val category = ArrayAdapter(requireContext(), R.layout.show_list, Constants.allProductCategory)
        val productType = ArrayAdapter(requireContext(), R.layout.show_list, Constants.allProductsTypes)

        binding.apply {
            etProductUnit.setAdapter(units)
            etProductCategory.setAdapter(category)
            etProductType.setAdapter(productType)
        }
    }

    private fun setStatusBarColor(){
        activity?.window?.apply {
            val statusBarColors = ContextCompat.getColor(requireContext(), R.color.royal_orange)
            statusBarColor = statusBarColors
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }
}