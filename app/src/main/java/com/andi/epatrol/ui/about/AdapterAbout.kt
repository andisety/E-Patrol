package com.andi.epatrol.ui.about

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andi.epatrol.R

class AdapterAbout(
    private val listData:List<ItemAbout>,
    private val itemAdapterCallBack:ItemAdapterCallBack):RecyclerView.Adapter<AdapterAbout.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val view=layoutInflater.inflate(R.layout.item_about,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listData[position],itemAdapterCallBack)
    }

    override fun getItemCount(): Int {
        return listData.size
    }
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitleAbout)
        fun bind(data:ItemAbout,itemAdapterCallBack: ItemAdapterCallBack){
            itemView.apply {
                tvTitle.text = data.titile
                tvTitle.setCompoundDrawablesWithIntrinsicBounds(data.icon,0,0,0)
                itemView.setOnClickListener {
                    itemAdapterCallBack.onClick(it,data)
                }
            }
        }
    }

    interface ItemAdapterCallBack{
        fun onClick(v: View,data: ItemAbout)
    }

}