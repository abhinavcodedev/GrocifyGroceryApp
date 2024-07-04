package com.example.groceryadminapp.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.groceryadminapp.databinding.ItemviewImgSelectionBinding

class AdapterSelectedImg(val imageUris : ArrayList<Uri>) :RecyclerView.Adapter<AdapterSelectedImg.SelectedImgViewHolder>() {
    class SelectedImgViewHolder(val binding: ItemviewImgSelectionBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedImgViewHolder {
        return SelectedImgViewHolder(ItemviewImgSelectionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
       return imageUris.size
    }

    override fun onBindViewHolder(holder: SelectedImgViewHolder, position: Int) {
        val image = imageUris[position]
        holder.binding.apply {
            ivImage.setImageURI(image)
        }
        holder.binding.closeButton.setOnClickListener {
            if (position < imageUris.size){
                imageUris.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }
}