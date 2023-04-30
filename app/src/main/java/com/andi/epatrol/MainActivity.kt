package com.andi.epatrol

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.andi.epatrol.databinding.ActivityMainBinding
import com.andi.epatrol.ui.auth.LoginActivity
import com.andi.epatrol.ui.scan.ScanActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    companion object{
         lateinit var auth:FirebaseAuth
         var uidUser:String=""
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //auth
        auth=FirebaseAuth.getInstance()
        //bottomnav
        binding.navView.background = null
//        binding.navView.menu.get(1).isEnabled = false
        //hide actianbar
        supportActionBar?.hide()
        //cek login
        if(auth.currentUser==null){
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }


        //to scan activity
        binding.fab.setOnClickListener {
            startActivity(Intent(this,ScanActivity::class.java))
        }


        val navView: BottomNavigationView = binding.navView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment

//        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val navController = navHostFragment.navController
        binding.navView.setupWithNavController(navHostFragment.navController)




        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_profile
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
    }


}