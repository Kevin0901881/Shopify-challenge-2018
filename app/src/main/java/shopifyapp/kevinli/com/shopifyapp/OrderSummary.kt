package shopifyapp.kevinli.com.shopifyapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class OrderSummary : AppCompatActivity() {
    private var ordersByProvince = ArrayList<Orders>()
    private var ordersByYear = ArrayList<Orders>()
    var retrofit = Retrofit.Builder()
            .baseUrl("https://shopicruit.myshopify.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    var getOrders = retrofit.create(GetOrders::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_summary)
        orderByProvince()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.province -> {
                orderByProvince()
                return true
            }
            R.id.year -> {
                orderByYear()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun orderByProvince() {
        val ordersList = findViewById<ListView>(R.id.orders_list)
        if (ordersByProvince.isEmpty()) {
            var lastProvince = ""
            var provinceIndex = 0
            val provinces = ArrayList<String>()

            val call = getOrders.orderList as Call<OrderList>
            call.enqueue(object : Callback<OrderList> {
                override fun onResponse(call: Call<OrderList>?, response: Response<OrderList>?) {
                    for (item in response?.body()?.orders!!) {
                        try {
                            val province = item.customer.default_address.province
                            val id = item.id
                            val email = item.email
                            val totalPrice = item.total_price
                            if (lastProvince != province && !provinces.contains(province)) {
                                ordersByProvince.add(Orders(province, "", 0, "", "", 0))
                                lastProvince = province
                                provinces.add(province)
                            }
                            provinceIndex++
                            ordersByProvince.add(Orders(province, "", id, email, totalPrice, 0))
                        } catch (e: Exception) {
                            continue
                        }
                    }

                    val sortedOrders = ArrayList<Orders>(mergeSort(ordersByProvince))
                    val sortedProvinces = provinces.sorted()
                    var index = 0

                    for (item in sortedProvinces) {
                        var count = 0
                        while (index < sortedOrders.size && sortedOrders[index].province == item) {
                            count++
                            index++
                        }
                        sortedOrders[index - count] = Orders(item, "", 0, "", "", count - 1)
                    }

                    ordersByProvince = sortedOrders

                    val adapter = ProvinceAdapter(baseContext, ordersByProvince)
                    ordersList.adapter = adapter
                }

                override fun onFailure(call: Call<OrderList>?, t: Throwable?) {

                }
            })
        } else {
            val adapter = ProvinceAdapter(this, ordersByProvince)
            ordersList.adapter = adapter
        }
    }

    fun orderByYear() {
        val ordersList = findViewById<ListView>(R.id.orders_list)
        if (ordersByYear.isEmpty()) {
            var lastYear = ""
            val years = ArrayList<String>()

            val call = getOrders.orderList as Call<OrderList>
            call.enqueue(object : Callback<OrderList> {
                override fun onResponse(call: Call<OrderList>?, response: Response<OrderList>?) {
                    for (item in response?.body()?.orders!!.subList(0, 10)) {
                        try {
                            val year = item.created_at.substring(0, 4)
                            if (lastYear != year) {
                                ordersByYear.add(Orders("", year, 0, "", "", 0))
                                lastYear = year
                                years.add(year)
                            }
                            ordersByYear.add(Orders("", year, item.id, item.email, item.total_price, 0))
                        } catch (e: Exception) {
                            continue
                        }
                    }

                    var index = 0
                    for (item in years) {
                        var count = 0
                        while (index < ordersByYear.size && ordersByYear[index].year == item) {
                            count++
                            index++
                        }
                        ordersByYear[index - count] = Orders("", item, 0, "", "", count - 1)
                    }

                    val adapter = YearAdapter(baseContext, ordersByYear)
                    ordersList.adapter = adapter
                }

                override fun onFailure(call: Call<OrderList>?, t: Throwable?) {

                }
            })
        } else {
            val adapter = YearAdapter(this, ordersByYear)
            ordersList.adapter = adapter
        }
    }

    private fun mergeSort(list: List<Orders>): List<Orders> {
        if (list.size <= 1) {
            return list
        }

        val middle = list.size / 2
        val left = list.subList(0,middle)
        val right = list.subList(middle,list.size)

        return merge(mergeSort(left), mergeSort(right))
    }


    private fun merge(left: List<Orders>, right: List<Orders>): List<Orders>  {
        var indexLeft = 0
        var indexRight = 0
        val newList : MutableList<Orders> = mutableListOf()

        while (indexLeft < left.count() && indexRight < right.count()) {
            if (left[indexLeft].province <= right[indexRight].province) {
                newList.add(left[indexLeft])
                indexLeft++
            } else {
                newList.add(right[indexRight])
                indexRight++
            }
        }

        while (indexLeft < left.size) {
            newList.add(left[indexLeft])
            indexLeft++
        }

        while (indexRight < right.size) {
            newList.add(right[indexRight])
            indexRight++
        }

        return newList
    }
}
