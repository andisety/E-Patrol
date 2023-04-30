package com.andi.epatrol.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.andi.epatrol.data.Absen
import com.andi.epatrol.databinding.FragmentItemBinding

class ItemFragment : Fragment() {

    private var _binding: FragmentItemBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainModel:ScanViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mainModel = ViewModelProvider(this,ViewModelProvider.NewInstanceFactory())[ScanViewModel::class.java]


        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        when(index){
            1-> {
               mainModel.absen.observe(viewLifecycleOwner){data->
                   setData(data)
               }
            }
            2->{
                mainModel.absen2.observe(viewLifecycleOwner){data->
                    setData(data)
                }
            }
            3->{
                mainModel.absen3.observe(viewLifecycleOwner){data->
                    setData(data)
                }
            }
        }
    }
    private fun setData(abs:List<Absen>){
        if (abs.isEmpty()){
            binding.tvDataEmpty.visibility = View.VISIBLE
        }
        val adapterScan = AdapterScan(abs)
        Log.e("DATA",abs.size.toString())
        val layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.rcList.layoutManager = layoutManager
        binding.rcList.adapter = adapterScan
    }
    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
    }



}