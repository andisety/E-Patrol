package com.andi.epatrol.ui.home


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andi.epatrol.MainActivity.Companion.uidUser
import com.andi.epatrol.data.Absen
import com.andi.epatrol.data.Users
import com.andi.epatrol.ui.home.HomeFragment.Companion.title
import com.andi.epatrol.util.DateHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ScanViewModel : ViewModel() {

    private val _absensList = MutableLiveData<List<Absen>>()
    val absen: MutableLiveData<List<Absen>> = _absensList
    private val _absensList2 = MutableLiveData<List<Absen>>()
    val absen2: MutableLiveData<List<Absen>> = _absensList2
    private val _absensList3 = MutableLiveData<List<Absen>>()
    val absen3: MutableLiveData<List<Absen>> = _absensList3


    private val _user = MutableLiveData<Users?>()
    val user = _user

    init {
        getAbsens()

    }

    private fun getAbsens() {
       val ref = FirebaseDatabase.getInstance().getReference("absen").orderByChild("idUser").equalTo(uidUser)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){

                    val arrayAbsen= arrayListOf<Absen>()
                    val arrayAbsen2= arrayListOf<Absen>()
                    val arrayAbsen3= arrayListOf<Absen>()

                    for (value: DataSnapshot in snapshot.children){
                        val absen:Absen? = value.getValue(Absen::class.java)
                        if (absen != null) {
                            //kondisi date = date now
                            if (DateHelper.getCurrentDateforDashboard(absen.tgl)==DateHelper.getCurrentDateforDashboard()){
                                Log.e("DATE",DateHelper.getCurrentDateforDashboard(absen.tgl)+"== "+DateHelper.getCurrentDateforDashboard())
                                if (DateHelper.stringToDate(absen.tgl) >= title[0] && DateHelper.stringToDate(absen.tgl) < title[1]){
                                    arrayAbsen.add(absen)
                                }else if (DateHelper.stringToDate(absen.tgl) > title[1] && DateHelper.stringToDate(absen.tgl) < title[2]){
                                    arrayAbsen2.add(absen)
                                }else if (DateHelper.stringToDate(absen.tgl) > title[2]){
                                    arrayAbsen3.add(absen)
                                }

                            }
                        }
                    }
                    _absensList.value = arrayAbsen
                    _absensList2.value = arrayAbsen2
                    _absensList3.value = arrayAbsen3
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

     fun getUser(uid:String){
        val dbUser= FirebaseDatabase.getInstance().getReference("users").orderByChild("uid").equalTo(uid)
        dbUser.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (value:DataSnapshot in snapshot.children){
                        val user = value.getValue(Users::class.java)
                            _user.value = user
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}