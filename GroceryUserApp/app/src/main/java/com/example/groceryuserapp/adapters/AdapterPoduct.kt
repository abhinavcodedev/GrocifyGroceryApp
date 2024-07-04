package com.example.groceryuserapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.denzcoskun.imageslider.models.SlideModel
import com.example.groceryuserapp.FilteringProducts

import com.example.groceryuserapp.databinding.ItemViewProductBinding
import com.example.groceryuserapp.models.Product

class AdapterPoduct(
    val onAddButtonClicked: (Product, ItemViewProductBinding) -> Unit,
    val onIncrementBtnClicked: (Product, ItemViewProductBinding) -> Unit,
    val onDecrementBtnClicked: (Product, ItemViewProductBinding) -> Unit
) : RecyclerView.Adapter<AdapterPoduct.ProductViewHolder>(), Filterable {
    class ProductViewHolder(val binding : ItemViewProductBinding) : ViewHolder(binding.root){

    }

    val diffutil = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.productRandomId == newItem.productRandomId
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffutil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(ItemViewProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = differ.currentList[position]

        holder.binding.apply {
            val imageList = ArrayList<SlideModel>()

            val productImage = product.productImageUris

            for (i in 0 until productImage?.size!!){
                imageList.add(SlideModel(product.productImageUris!!.get(i).toString()))
            }
            ivImageSlider.setImageList(imageList)

            tvProductTitle.text = product.productTitle
            tvProductQuantity.text = product.productQuantity.toString() + product.productUnit

            tvProductPrice.text ="₹" + product.productPrice

            if(product.itemCount!!>0){
                tvProductCount.text = product.itemCount.toString()
                tvAdd.visibility = View.GONE
                llProductCount.visibility = View.VISIBLE
            }

            tvAdd.setOnClickListener {
                onAddButtonClicked(product,this)
            }
            tvIncrementCount.setOnClickListener {
                onIncrementBtnClicked(product,this)
            }
            tvDecrementCount.setOnClickListener {
                onDecrementBtnClicked(product,this)
            }
        }

    }

    val filter : FilteringProducts? = null
    var originalList = ArrayList<Product>()
    override fun getFilter(): Filter {
        if(filter == null) return FilteringProducts(this, originalList)
        return filter
    }


}