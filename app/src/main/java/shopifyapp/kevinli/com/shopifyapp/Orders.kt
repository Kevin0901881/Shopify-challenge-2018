package shopifyapp.kevinli.com.shopifyapp

/**
 * Created by Kevin on 5/9/2018.
 */

internal class Orders(_province: String, _year: String, _id: Long, _email: String, _price: String, _quantity: Int) {
    var province: String = _province
        get() = field
        set(value) {
            field = value
        }
    var year: String = _year
        get() = field
        set(value) {
            field = value
        }
    var id: Long = _id
        get() = field
        set(value) {
            field = value
        }
    var email: String = _email
        get() = field
        set(value) {
            field = value
        }
    var price: String = _price
        get() = field
        set(value) {
            field = value
        }
    var quantity: Int = _quantity
        get() = field
        set(value) {
            field = value
        }
}