package com.andi.epatrol.ui.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.andi.epatrol.MainActivity
import com.andi.epatrol.MainActivity.Companion.uidUser
import com.andi.epatrol.data.sharepref.SavePref
import com.andi.epatrol.databinding.ActivityLoginBinding
import com.andi.epatrol.ui.home.ScanViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding:ActivityLoginBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var mainModel:ScanViewModel
    lateinit var sharePref:SharedPreferences
    lateinit var editor:SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        binding=ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.etEmail.setText("andi@gmail.com")
        binding.etPwd.setText("Andi123")

        //model
        mainModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[ScanViewModel::class.java]


        //firebase
        auth =FirebaseAuth.getInstance()

        binding.tvToSignup.setOnClickListener {
            val intent= Intent(this,SignupActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {

            binding.progressBarLogin.visibility = View.VISIBLE
            //textfield
            val email= binding.etEmail.text.toString().trim()
            val pwd= binding.etPwd.text.toString()

            if (email.isEmpty() ||  pwd.isEmpty() ) {
                if (email.isEmpty()) {
                    binding.etEmail.error = "Masukan Email"
                }
                if (pwd.isEmpty()) {
                    binding.etPwd.error = "Masukan Password"
                }
                binding.progressBarLogin.visibility = View.GONE

            } else{
                auth.signInWithEmailAndPassword(email,pwd)
                    .addOnCompleteListener{
                        if (it.isSuccessful){
                            //shareprfe
                            sharePref =getSharedPreferences(SavePref.PREF_NAME, Context.MODE_PRIVATE)
                            editor = sharePref.edit()

                            Toast.makeText(applicationContext,"Login Succes",Toast.LENGTH_LONG).show()
                            uidUser = auth.currentUser!!.uid
                            editor.putString(SavePref.UID, uidUser).apply()
                            editor.putString(SavePref.PWD, pwd).apply()
                            val uid=sharePref.getString(SavePref.UID,"0")
                            mainModel.getUser(uid.toString())

                            binding.progressBarLogin.visibility = View.GONE
                            showDialogShift()
                            Log.e("LOGINUID",uid.toString())

                        }else{
                            Toast.makeText(applicationContext,"Login Failure",Toast.LENGTH_LONG).show()
                            Log.e("LOGIN",it.exception?.message.toString())
                        }
                        binding.progressBarLogin.visibility = View.GONE
                    }
                    .addOnFailureListener {
                        Log.e("LOGIN",it.message.toString())
                    }
            }
        }
    }
    private fun toHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun showDialogShift(){
        val shift = arrayOf("1","2","3")
        var selectedIdx = 0
        var selectedShif = shift[selectedIdx]

        MaterialAlertDialogBuilder(this)
            .setTitle("Pilih Shift :")
            .setSingleChoiceItems(shift,selectedIdx){dialog,which->
                selectedIdx = which
                selectedShif = shift[which]
            }
            .setPositiveButton("Ok"){dialog,which->
                Toast.makeText(applicationContext,"Shift  $selectedShif",Toast.LENGTH_LONG).show()
                editor.putString(SavePref.SHIFT,selectedShif).apply()
                toHome()
            }
            .setNeutralButton("Cancel"){dialog,which->
                Toast.makeText(applicationContext,"cancel",Toast.LENGTH_LONG).show()
            }
            .show()


    }


}