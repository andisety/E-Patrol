package com.andi.epatrol.data

data class Absen(
    val id:String?,
    val idUser:String,
    val lokasi:String?,
    val tgl:String
){
    constructor():this("","","","")
}

