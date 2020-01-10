package me.manulorenzo.worldheritages.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import kotlinx.android.synthetic.main.heritage_item_row.view.heritageImage
import kotlinx.android.synthetic.main.heritage_item_row.view.heritageName
import kotlinx.android.synthetic.main.heritage_item_row.view.heritageShortInfo
import me.manulorenzo.worldheritages.R
import me.manulorenzo.worldheritages.data.model.Heritage

class HeritagesAdapter(private val heritageList: List<Heritage?>?) :
    RecyclerView.Adapter<HeritagesAdapter.HeritagesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeritagesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.heritage_item_row, parent, false)
        return HeritagesViewHolder(view)
    }

    override fun onBindViewHolder(holder: HeritagesViewHolder, position: Int) {
        val heritage = heritageList?.get(position)
        with(holder) {
            heritageName.text = heritage?.name
            heritageShortInfo.text = heritage?.shortInfo
            heritageImage.load(heritage?.image)
        }
    }

    override fun getItemCount() = heritageList?.size ?: 0

    inner class HeritagesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val heritageName: TextView = view.heritageName
        val heritageShortInfo: TextView = view.heritageShortInfo
        val heritageImage: ImageView = view.heritageImage
    }
}