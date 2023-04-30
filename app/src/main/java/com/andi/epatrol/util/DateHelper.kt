package com.andi.epatrol.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateHelper {
    @SuppressLint("SimpleDateFormat")
    fun getCurrentDateFull():String{
        val sdf = SimpleDateFormat("dd MMMM yyyy, HH:mm:ss")
        return sdf.format(Date())
    }
    fun getCurrentDateforDashboard():String{
        val sdf = SimpleDateFormat("dd MMMM yyyy")
        return sdf.format(Date())
    }
    fun getCurrentDateforDashboard(datePrm:String):String{
        val formatter = SimpleDateFormat("dd MMMM yyyy, HH:mm:ss")
        val date = formatter.parse(datePrm)
        val sdf = SimpleDateFormat("dd MMMM yyyy")
        return sdf.format(date)
    }

    fun getCurrentDateHour():String{
        val sdf = SimpleDateFormat("HH:mm")
        return sdf.format(Date())
    }

    fun stringToDate(date:String):String{
        val formatter = SimpleDateFormat("dd MMMM yyyy, HH:mm:ss")
        val date = formatter.parse(date)
        val sdf = SimpleDateFormat("HH:mm")
        return sdf.format(date)
    }
}