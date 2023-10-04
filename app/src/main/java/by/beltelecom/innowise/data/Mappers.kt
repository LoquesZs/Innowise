package by.beltelecom.innowise.data

import by.beltelecom.innowise.common.database.entities.PhotoEntity
import by.beltelecom.innowise.common.database.entities.SourceEntity
import by.beltelecom.innowise.common.network.models.CollectionModel
import by.beltelecom.innowise.common.network.models.CommonPhotoResponse
import by.beltelecom.innowise.common.network.models.PhotoModel
import by.beltelecom.innowise.common.network.models.SourceModel
import by.beltelecom.innowise.domain.entities.CollectionDomain
import by.beltelecom.innowise.domain.entities.Photo
import by.beltelecom.innowise.domain.entities.Photos
import by.beltelecom.innowise.domain.entities.Source

fun PhotoEntity.toDomain(): Photo {
    return Photo(
        id = id,
        width = width,
        height = height,
        photographer = photographer,
        sources = sources.toDomain()
    )
}

fun SourceEntity.toDomain(): Source {
    return Source(
        original = original,
        portrait = portrait,
        large2x = large2x,
        large = large,
        medium = medium,
        small = small
    )
}

fun Photo.toEntity(): PhotoEntity {
    return PhotoEntity(
        id = id,
        width = width,
        height = height,
        photographer = photographer,
        sources = sources.toEntity()
    )
}

fun Source.toEntity(): SourceEntity {
    return SourceEntity(
        original = original,
        portrait = portrait,
        large2x = large2x,
        large = large,
        medium = medium,
        small = small
    )
}

fun PhotoModel.toDomain(): Photo {
    return Photo(
        id = id,
        width = width,
        height = height,
        photographer = photographer,
        sources = sources.toDomain()
    )
}

fun SourceModel.toDomain(): Source {
    return Source(
        original = original,
        portrait = portrait,
        large2x = large2x,
        large = large,
        medium = medium,
        small = small
    )
}

fun CollectionModel.toDomain(): CollectionDomain {
    return CollectionDomain(
        id = id,
        title = title,
        description = description,
        mediaCount = mediaCount,
        photosCount = photosCount,
        videosCount = videosCount
    )
}

fun CommonPhotoResponse.toDomain(): Photos {
    return Photos(
        page = page,
        photos = photos.map { it.toDomain() },
        total = total
    )
}