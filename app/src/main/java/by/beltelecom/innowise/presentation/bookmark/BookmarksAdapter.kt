package by.beltelecom.innowise.presentation.bookmark

import by.beltelecom.innowise.presentation.PhotosAdapter
import by.beltelecom.innowise.presentation.PhotosViewHolder

internal class BookmarksAdapter(onPhotoClick: (id: Long) -> Unit) : PhotosAdapter(onPhotoClick) {
    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        val photo = getItem(position)
        holder.bind(
            id = photo.id,
            url = photo.sources.medium,
            photographer = photo.photographer
        )
    }
}