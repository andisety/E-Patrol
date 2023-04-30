package com.andi.epatrol.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.andi.epatrol.data.Users
import com.andi.epatrol.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth
    private lateinit var database:DatabaseReference
    private lateinit var binding: ActivitySignupBinding



    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignupBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        //firebase
        auth=FirebaseAuth.getInstance()

        binding.etNama.setText("Andi")
        binding.etEmail.setText("Andi@gmail.com")
        binding.etNo.setText("0812360935454")
        binding.etPwd.setText("Andi123")
        binding.etKonfirmPwd.setText("Andi123")




        binding.tvToLogin.setOnClickListener {
            toLogin()
        }

        binding.btnSignup.setOnClickListener {
            //textfield
            val nama = binding.etNama.text.toString()
            val email= binding.etEmail.text.toString().trim()
            val noHp= binding.etNo.text.toString()
            val pwd= binding.etPwd.text.toString()
            val cpwd= binding.etKonfirmPwd.text.toString()



            binding.progressBarSignup.visibility = View.VISIBLE

                if (nama.isEmpty() || email.isEmpty() || noHp.isEmpty() || pwd.isEmpty() || cpwd.isEmpty()) {
                    if (nama.isEmpty()) {
                        binding.etNama.error = "Masukan Nama"
                    }
                    if (email.isEmpty()) {
                        binding.etEmail.error = "Masukan Email"
                    }
                    if (noHp.isEmpty()) {
                        binding.etNo.error = "Masukan No Hp"
                    }
                    if (pwd.isEmpty()) {
                        binding.etPwd.error = "Masukan Password"
                    }
                    if (cpwd.isEmpty()) {
                        binding.etKonfirmPwd.error = "Masukan Password"
                    }
                    binding.progressBarSignup.visibility = View.GONE


                } else if (noHp.length <= 10) {
                    binding.etNo.error = "Masukan No hp yang benar"
                } else if (pwd.length < 6) {
                    binding.etPwd.error = "Minimal password 6 karakter"
                } else if (cpwd != pwd) {
                    binding.etKonfirmPwd.error = "Password tidak sama"
                }else{
                    auth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener {
                        if (it.isSuccessful) {
                            database = FirebaseDatabase.getInstance().reference.child("users").child(auth.currentUser!!.uid)
                            val user= Users(auth.currentUser!!.uid,nama,email,noHp,"")
                            database.setValue(user).addOnCompleteListener {
                                if (it.isSuccessful){
                                    toLogin()
                                }else{
                                    Toast.makeText(applicationContext,"ada sesuatu yang salah",Toast.LENGTH_SHORT).show()
                                }
                            }
                        }else{
                            Log.e("REGISTER",it.exception?.message.toString())
                            Toast.makeText(applicationContext,"ada sesuatu yang salah2",Toast.LENGTH_SHORT).show()
                        }
                    }
                        .addOnFailureListener {
                            Log.e("REGISTER",it.message.toString())
                        }
                }
            }
    }

    private fun toLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}