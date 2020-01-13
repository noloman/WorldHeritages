package me.manulorenzo.worldheritages.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import kotlinx.android.synthetic.main.heritage_item_row.view.heritageImage
import kotlinx.android.synthetic.main.heritage_item_row.view.heritageName
import kotlinx.android.synthetic.main.heritage_item_row.view.heritageShortInfo
import me.manulorenzo.worldheritages.R
import me.manulorenzo.worldheritages.data.model.Heritage

class HeritagesAdapter :
    PagedListAdapter<Heritage, HeritagesAdapter.HeritagesViewHolder>(HERITAGE_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeritagesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.heritage_item_row, parent, false)
        return HeritagesViewHolder(view)
    }

    override fun onBindViewHolder(holder: HeritagesViewHolder, position: Int) {
        val heritage = getItem(position)
        with(holder) {
            heritageName.text = heritage?.name
            heritageShortInfo.text = heritage?.shortInfo
            heritageImage.load(heritage?.image)
        }
    }

    inner class HeritagesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val heritageName: TextView = view.heritageName
        val heritageShortInfo: TextView = view.heritageShortInfo
        val heritageImage: ImageView = view.heritageImage
    }

    companion object {
        private val HERITAGE_COMPARATOR = object : DiffUtil.ItemCallback<Heritage>() {
            override fun areItemsTheSame(oldItem: Heritage, newItem: Heritage): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Heritage, newItem: Heritage): Boolean =
                oldItem == newItem
        }
    }
}