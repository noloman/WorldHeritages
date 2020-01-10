package me.manulorenzo.worldheritages.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.main_fragment.heritagesProgressBar
import kotlinx.android.synthetic.main.main_fragment.heritagesRecyclerView
import me.manulorenzo.worldheritages.R
import me.manulorenzo.worldheritages.data.Resource
import org.koin.android.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {
    private val viewModel: MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.main_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.worldHeritagesLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    heritagesProgressBar.visibility = View.VISIBLE
                    heritagesRecyclerView.visibility = View.GONE
                }
                is Resource.Success -> {
                    heritagesProgressBar.visibility = View.GONE
                    heritagesRecyclerView.visibility = View.VISIBLE
                    heritagesRecyclerView.adapter = HeritagesAdapter(it.data)
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
