package com.example.groceryuserapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

import com.example.groceryuserapp.databinding.ItemviewProductCategoryBinding
import com.example.groceryuserapp.models.Category
import kotlin.reflect.KFunction0

class AdapterCategory(
    val categoryList: ArrayList<Category>,
    val onCategoryIconClicked: (Category)-> Unit
): RecyclerView.Adapter<AdapterCategory.CategoryViewHolder>(){
    class CategoryViewHolder(val binding: ItemviewProductCategoryBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(ItemviewProductCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        holder.binding.apply {
            IvCategoyimg.setImageResource(category.image)
            TvCategoryTitle.text = category.title
        }
        holder.itemView.setOnClickListener {
            onCategoryIconClicked(category)
        }
    }
}