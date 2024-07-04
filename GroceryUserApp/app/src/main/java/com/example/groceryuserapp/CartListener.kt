package com.example.groceryuserapp

interface CartListener {

    fun showCartlayout(itemCount : Int)

    fun savingCartItemCount(itemCount : Int)

    fun hideCartLayout()
}