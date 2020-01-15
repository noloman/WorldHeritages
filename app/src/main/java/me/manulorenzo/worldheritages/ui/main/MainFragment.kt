package me.manulorenzo.worldheritages.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.main_fragment.errorImage
import kotlinx.android.synthetic.main.main_fragment.heritagesRecyclerView
import me.manulorenzo.worldheritages.R
import org.koin.android.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {
    private val viewModel: MainViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.main_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = HeritagesAdapter()
        heritagesRecyclerView.adapter = adapter
        viewModel.worldHeritagesLiveData?.observe(
            viewLifecycleOwner,
            Observer(adapter::submitList)
        )
        viewModel.errorPagedList.observe(viewLifecycleOwner, Observer(::changeVisibility))
    }

    private fun changeVisibility(isEmpty: Boolean) {
        if (isEmpty) {
            heritagesRecyclerView.visibility = View.GONE
            errorImage.visibility = View.VISIBLE
        } else {
            heritagesRecyclerView.visibility = View.VISIBLE
            errorImage.visibility = View.GONE
        }
    }

    companion object {
        operator fun invoke() = MainFragment()
    }
}