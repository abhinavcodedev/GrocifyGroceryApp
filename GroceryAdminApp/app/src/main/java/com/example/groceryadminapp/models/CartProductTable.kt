package com.example.groceryadminapp.models



data class CartProductTable(

    val productId : String = "random",//can't apply nullability check here.

    val productTitle : String? = null,
    val productQuantity : String? = null,
    val productPrice : String? = null,
    var productCount : Int? = null,
    var productStock : Int? = null,
    var productImage : String?= null,
    var productCategory : String? = null,
    var adminUid : String? = null,

)