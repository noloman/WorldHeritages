package me.manulorenzo.worldheritages.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import me.manulorenzo.worldheritages.R
import me.manulorenzo.worldheritages.data.Resource
import org.koin.android.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {
    private val viewModel: MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.main_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.worldHeritagesLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    Log.i("manu", it.data.toString())
                }
                is Resource.Error -> {
                    Log.e("manu", it.message)
                }
            }
        })
    }

    companion object {
        operator fun invoke() = MainFragment()
    }
}
