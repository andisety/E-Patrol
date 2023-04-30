package com.andi.epatrol.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.andi.epatrol.R
import com.andi.epatrol.data.sharepref.SavePref
import com.andi.epatrol.databinding.FragmentHomeBinding
import com.andi.epatrol.util.DateHelper
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    companion object{
        lateinit var mainModel: ScanViewModel
        var sharePref:SharedPreferences?=null
        var editor:SharedPreferences.Editor?=null
        lateinit var title: List<String>
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivProfileAtHome.setOnClickListener {
            findNavController(it).navigate(R.id.action_navigation_home_to_navigation_profile)
        }



        binding.tvTglNow.text = DateHelper.getCurrentDateforDashboard()
        mainModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[ScanViewModel::class.java]
        mainModel.user.observe(viewLifecycleOwner){user->
            binding.tvName.text = user?.name
            Glide.with(this)
                .load(user?.url)
                .into(binding.ivProfileAtHome)
        }
        sharePref = activity?.getSharedPreferences(SavePref.PREF_NAME, Context.MODE_PRIVATE)
        editor = sharePref?.edit()
        val uid= sharePref?.getString(SavePref.UID,"")
        mainModel.getUser(uid.toString())
        val shift = sharePref?.getString(SavePref.SHIFT,"1")
        binding.tvShift.text = shift
        when(shift){
            "1"->{
                binding.tvJamKerja.text = "08.00-16.00"
                title = listOf("08.00", "11.00", "14.00")
                initTabLayout(title)
            }
            "2"->{
                binding.tvJamKerja.text = "16.00-24.00"
                title = listOf("16.00", "19.00", "22.00")
                initTabLayout(title)
            }
            "3"->{
                binding.tvJamKerja.text = "24.00-08.00"
                title = listOf("24.00", "03.00", "06.00")
                initTabLayout(title)
            }
        }
    }

    private fun initTabLayout( title:List<String>){

        val sectionPageAdapter = SectionPageAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPageAdapter
        val tabs: TabLayout = binding.tabLayout

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            when(position){
                0-> {tab.text = title[position]}
                1->{
                    tab.text=title[position]
                }
                2->{
                    tab.text=title[position]
                }
            }
        }.attach()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}