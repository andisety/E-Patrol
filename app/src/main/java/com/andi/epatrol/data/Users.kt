package com.andi.epatrol.data

data class Users(
    val uid:String,
    val name:String,
    val email:String,
    val noHp:String,
    val url:String
){
    constructor():this("","","","","")
}
