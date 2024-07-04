package com.example.groceryuserapp.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.groceryuserapp.Constants
import com.example.groceryuserapp.R
import com.example.groceryuserapp.adapters.AdapterCategory
import com.example.groceryuserapp.databinding.FragmentHomeBinding
import com.example.groceryuserapp.models.Category
import com.example.groceryuserapp.viewmodels.UserViewModel


class HomeFragment : Fragment() {
    private val viewModel : UserViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        setStatusBarColor()
        setAllCategories()
        navigatingToSearchFragment()
        onProfileClicked()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun onProfileClicked() {
        binding.imgvProfile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }
    }

    private fun navigatingToSearchFragment() {
        binding.cvSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun setAllCategories() {
        val categoryList = ArrayList<Category>()


        for (i in 0 until Constants.allProductCategoryIcon.size){
            categoryList.add(Category(Constants.allProductCategory[i] , Constants.allProductCategoryIcon[i]))
        }

        binding.RvCategories.adapter = AdapterCategory(categoryList, ::onCategoryIconClicked)
    }

    
    fun onCategoryIconClicked(category: Category){
        val bundle = Bundle()
        bundle.putString("category", category.title)
        findNavController().navigate(R.id.action_homeFragment_to_categoryFragment, bundle)
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