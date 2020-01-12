package me.manulorenzo.worldheritages.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.main_fragment.heritagesRecyclerView
import me.manulorenzo.worldheritages.R
import org.koin.android.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {
    private val viewModel: MainViewModel by viewModel()
    private val adapter = HeritagesAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.main_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        heritagesRecyclerView.adapter = adapter
        viewModel.worldHeritagesLiveData?.observe(
            viewLifecycleOwner,
            Observer(adapter::submitList)
        )
    }

    companion object {
        operator fun invoke() = MainFragment()
    }
}