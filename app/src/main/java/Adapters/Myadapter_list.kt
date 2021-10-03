package Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.homework24.R
import kotlinx.android.synthetic.main.item_2.view.*
import models.Contact

class MyadapterList(var context: Context, var list: ArrayList<Contact>, var click: Click) :
    RecyclerView.Adapter<MyadapterList.Vh>() {
    inner class Vh(var itemRv: View) : RecyclerView.ViewHolder(itemRv) {
        fun onBind(contact: Contact, position: Int) {
            itemRv.text_contact_name.text = contact.name
            itemRv.text_number.text = contact.number

            itemRv.call_swipe.setOnClickListener {
                click.Call(contact.number!!)
            }
            itemRv.sms_swipe.setOnClickListener {
                click.Sms(contact.name!!, contact.number!!)
            }


        }
    }

    interface Click {
        fun Call(number: String)
        fun Sms(contact: String, number: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(LayoutInflater.from(parent.context).inflate(R.layout.item_2, parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size
}