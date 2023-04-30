package com.andi.epatrol.ui.about


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.andi.epatrol.R
import com.andi.epatrol.data.Users
import com.andi.epatrol.data.sharepref.SavePref
import com.andi.epatrol.databinding.FragmentEditProfileBinding
import com.andi.epatrol.ui.home.HomeFragment.Companion.mainModel
import com.andi.epatrol.ui.home.HomeFragment.Companion.sharePref
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    var editor: SharedPreferences.Editor? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //sharepref
        sharePref = activity?.getSharedPreferences(SavePref.PREF_NAME, Context.MODE_PRIVATE)
        editor = sharePref?.edit()

        binding.btnEdit.setOnClickListener {
            val nama=binding.etNama.text.toString()
            val email=binding.etEmail.text.toString()
            val noHp=binding.etNo.text.toString()
            editProfile(nama,email,noHp)
        }

    }

    private fun editProfile(nama:String,email:String,noHp:String) {
        val userAuth = FirebaseAuth.getInstance().currentUser

        val uid = sharePref?.getString(SavePref.UID,"").toString()
        val refUp = FirebaseDatabase.getInstance().getReference("users")
        mainModel.user.observe(viewLifecycleOwner){user->

            val credential = EmailAuthProvider.getCredential(user!!.email, sharePref?.getString(SavePref.PWD,"").toString())
            userAuth?.reauthenticate(credential)?.addOnCompleteListener {
                lifecycleScope.launchWhenResumed {
                    val usr=FirebaseAuth.getInstance().currentUser
                    usr?.updateEmail(email)?.addOnCompleteListener {
                        if (it.isSuccessful){
                            Log.e("UPDATE","email updated")
                        }
                    }
                }
            }


            val u = Users(uid,nama,user!!.email,noHp,user.url)
            refUp.child(uid).setValue(u).addOnCompleteListener {
                lifecycleScope.launchWhenResumed {
                    findNavController().navigate(R.id.action_navigation_editprofile_to_navigation_profile)
                }
            }

        }
    }



        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }

}