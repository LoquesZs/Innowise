package by.beltelecom.innowise.presentation

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import by.beltelecom.innowise.domain.entities.Photo

internal open class PhotosAdapter(
    private val onImageClick: (Long) -> Unit
) : ListAdapter<Photo, PhotosViewHolder>(PhotosDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        return PhotosViewHolder(parent, onImageClick)
    }

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        val photo = getItem(position)
        holder.bind(
            id = photo.id,
            url = photo.sources.medium
        )
    }

    private class PhotosDiffUtilCallback : DiffUtil.ItemCallback<Photo>() {

        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return newItem == oldItem
        }
    }
}