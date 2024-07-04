package com.example.groceryuserapp.models

import com.example.groceryuserapp.roomdb.CartProductTable

data class Orders(
    val orderId : String? = null,
    val orderList : List<CartProductTable>? = null,
    val userAddress : String? = null,
    val orderStatus : Int?= 0,
    val orderDate : String? = null,
    val orderinguserId : String? = null,
    val orderPrice : Int? =null,
    val orderTitle : String?=null
)
