package shopifyapp.kevinli.com.shopifyapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.order_item.view.*
import kotlinx.android.synthetic.main.subheader.view.*

/**
 * Created by Kevin on 5/9/2018.
 */

internal class YearAdapter(context: Context, var data: List<Orders>) : BaseAdapter() {

    private var inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (data[position].price == "") {
            val subheader = inflater.inflate(R.layout.subheader, parent, false)
            subheader.subheading.text = data[position].year
            subheader.quantity.text = data[position].quantity.toString() + " orders"
            return subheader
        } else {
            val orderItem = inflater.inflate(R.layout.order_item, parent, false)
            orderItem.order_id.text = "ID: " + data[position].id.toString()
            orderItem.price.text = "$" + data[position].price
            if (data[position].email == "" ) {
                orderItem.email.text = "No Email"
            } else {
                orderItem.email.text = data[position].email
            }
            return orderItem
        }
    }
}