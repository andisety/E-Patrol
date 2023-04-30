package com.andi.epatrol.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andi.epatrol.R
import com.andi.epatrol.data.Absen
import com.andi.epatrol.util.DateHelper

class AdapterScan(private val listData:List<Absen>):RecyclerView.Adapter<AdapterScan.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val view=layoutInflater.inflate(R.layout.item_scan,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int {
        return listData.size
    }
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val tvLokasi = itemView.findViewById<TextView>(R.id.tvLokasi)
        val tvjam = itemView.findViewById<TextView>(R.id.tvJam)
        fun bind(data:Absen){
            itemView.apply {
                tvLokasi.text = data.lokasi
                tvjam.text = DateHelper.stringToDate(data.tgl)
            }
        }

    }

}