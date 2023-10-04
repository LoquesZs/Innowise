package by.beltelecom.innowise.presentation

import android.view.View
import android.view.ViewGroup
import by.beltelecom.innowise.R
import by.beltelecom.innowise.common.presentation.ViewBindingViewHolder
import by.beltelecom.innowise.databinding.PhotoCardBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class PhotosViewHolder(
    parent: ViewGroup,
    private val onItemClick: (id: Long) -> Unit
) : ViewBindingViewHolder<PhotoCardBinding>(parent, PhotoCardBinding::inflate) {

    fun bind(id: Long, url: String) {
        binding.root.setOnClickListener {
            onItemClick(id)
        }

        Glide.with(context)
            .asDrawable()
            .load(url)
            .placeholder(R.drawable.placeholder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.image)
    }
    fun bind(id: Long, url: String, photographer: String) {
        binding.root.setOnClickListener {
            onItemClick(id)
        }
        with(binding.photographer) {
            text = photographer
            visibility = View.VISIBLE
        }

        Glide.with(context)
            .asDrawable()
            .load(url)
            .placeholder(R.drawable.placeholder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.image)
    }
}