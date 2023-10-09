package com.example.delivery

@kotlinx.serialization.Serializable
class LoginObject(val token: String = "", val message: String = "ok")

@kotlinx.serialization.Serializable
open class Person(
    val message: String = "ok",
    val id: Int = 0,
    val category: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val username: String = "",
    val status: String = "",
    val email: String = "",
    val dateJoined: String = ""
)

@kotlinx.serialization.Serializable
class Seller : Person(){
    val image: String = ""
}

@kotlinx.serialization.Serializable
class Item(
    val id: Int = 0,
    val name: String = "Item",
    val sellerId: Int = 0,
    val sellerUsername: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val image: String = "",
    val rating: Double? = null,
    val numberOfRaters: Int = 0,
    val numberOfOrders: Int = 0,
    val numberOfBuyouts: Int = 0
)

@kotlinx.serialization.Serializable
class Review(
    val id: Int = 0,
    val dateCreated: String = "",
    val reviewerType: String = "",
    val reviewerId: Int = 0,
    val reviewedType: String = "",
    val reviewedId: Int = 0,
    val reviewerUsername: String = "",
    val rating: Int = 0,
    val text: String = "",
    var isMine: Boolean = false,
    var isText: Boolean = false){

    fun checkIfMine(myReviewId: Int) : Review{
        if(this.id == myReviewId) this.isMine = true
        return this
    }

    fun checkIfText() : Review{
        if(this.text !in listOf("", "null")) this.isText = true
        return this
    }
}


@kotlinx.serialization.Serializable
class BasketItem(
    val id: Int = 0,
    val customerId: Int = 0,
    val customerUsername: String = "",
    val orderId: Int = 0,
    val itemId: Int = 0,
    val itemName: String = "",
    val quantity: Int = 0,
    val totalPrice: Double = 0.0
)
