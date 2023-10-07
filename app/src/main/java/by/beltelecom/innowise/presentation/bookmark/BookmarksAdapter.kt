package by.beltelecom.innowise.presentation.bookmark

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import by.beltelecom.innowise.domain.entities.Photo
import by.beltelecom.innowise.presentation.PhotosViewHolder

internal class BookmarksAdapter(private val onPhotoClick: (id: Long) -> Unit) : PagingDataAdapter<Photo, PhotosViewHolder>(COMPARATOR) {
    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        getItem(position)?.let { photo ->
            holder.bind(
                id = photo.id,
                url = photo.sources.medium,
                photographer = photo.photographer
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        return PhotosViewHolder(parent, onPhotoClick)
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return oldItem == newItem
            }
        }
    }
}