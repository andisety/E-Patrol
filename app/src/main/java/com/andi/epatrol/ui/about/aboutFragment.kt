package com.andi.epatrol.ui.about

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.andi.epatrol.MainActivity.Companion.auth
import com.andi.epatrol.R
import com.andi.epatrol.data.Users
import com.andi.epatrol.data.sharepref.SavePref
import com.andi.epatrol.databinding.FragmentAboutBinding
import com.andi.epatrol.ui.auth.LoginActivity
import com.andi.epatrol.ui.home.HomeFragment.Companion.mainModel
import com.andi.epatrol.ui.home.HomeFragment.Companion.sharePref
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class aboutFragment : Fragment(),AdapterAbout.ItemAdapterCallBack {

    private var _binding: FragmentAboutBinding? = null
    private lateinit var imgUrl: Uri
    var editor: SharedPreferences.Editor? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //sharepref
        sharePref = activity?.getSharedPreferences(SavePref.PREF_NAME, Context.MODE_PRIVATE)
        editor = sharePref?.edit()

        //set foto profile
        mainModel.user.observe(viewLifecycleOwner){user->
            Glide.with(this)
                .load(user?.url)
                .into(binding.ivProfile)
        }

        //list
        val listAbout = listOf<ItemAbout>(
            ItemAbout("Profile", R.drawable.ic_baseline_person_24,R.id.action_navigation_profile_to_navigation_edit_profile),
            ItemAbout("Riwayat Patroli", R.drawable.ic_baseline_remove_red_eye_24,0),
            ItemAbout("Bantuan", R.drawable.ic_baseline_help_outline_24,0),
        )

        setData(listAbout)
        mainModel.user.observe(viewLifecycleOwner) { user ->
            binding.tvNameProfile.text = user!!.name

        }

        //logout
        binding.btnLogout.setOnClickListener {
            auth.signOut()
            editor?.clear()?.apply()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }

        binding.ivProfile.setOnClickListener {
            intentCamera()
        }
    }

    private fun simpanBitmap(bm:Bitmap) {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos)
        val b = baos.toByteArray()
        val encode = Base64.encodeToString(b,Base64.DEFAULT)
        editor?.putString(SavePref.PROFILE_PHOTO,encode)?.apply()

    }

    private fun intentCamera() {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            resultLauncher.launch(intent)
        }

        var resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                binding.pBarAbout.visibility = View.VISIBLE
                if (result.resultCode == Activity.RESULT_OK) {
                    val uri = result.data!!.data
                    try {
                        val inputStream =
                            uri?.let { activity?.contentResolver?.openInputStream(it) }
                        val imgBitmap = BitmapFactory.decodeStream(inputStream)
                        val baos = ByteArrayOutputStream()
                        imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val image = baos.toByteArray()
                        val encode = Base64.encodeToString(image,Base64.DEFAULT)
                        //savepref
                        editor?.putString(SavePref.PROFILE_PHOTO,encode)?.apply()
                        binding.ivProfile.setImageBitmap(imgBitmap)
                        inputStream!!.close()

                        Toast.makeText(requireContext(), "Image Selected", Toast.LENGTH_LONG).show()
                        val ref = FirebaseStorage.getInstance().reference.child("img/${FirebaseAuth.getInstance().currentUser?.uid}")
                        ref.putBytes(image)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    ref.downloadUrl.addOnCompleteListener {
                                        it.result?.let { url ->
                                            imgUrl = url
                                            updateDataUrl(imgUrl)
                                            binding.ivProfile.setImageBitmap(imgBitmap)

                                        }
                                    }
                                }
                            }
                        binding.pBarAbout.visibility = View.GONE

                    } catch (e: Exception) {
                        binding.pBarAbout.visibility = View.GONE
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                    }


                }
            }

    private fun updateDataUrl(imgUrl: Uri) {
        val uid = sharePref?.getString(SavePref.UID,"").toString()
        val refUp = FirebaseDatabase.getInstance().getReference("users")
        mainModel.user.observe(viewLifecycleOwner){user->
            val u = Users(uid,user!!.name,user.email,user.noHp,imgUrl.toString())
            refUp.child(uid).setValue(u)
        }
    }


    fun setData(abt: List<ItemAbout>) {
            val adapterAbout = AdapterAbout(abt,this)
            val layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding.rcListAbout.layoutManager = layoutManager
            binding.rcListAbout.adapter = adapterAbout
        }


        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }

    override fun onClick(v: View, data: ItemAbout) {
        if (data.action>0){
            findNavController().navigate(data.action)
        }else{
            Toast.makeText(requireContext(),data.titile,Toast.LENGTH_LONG).show()
        }

    }

}