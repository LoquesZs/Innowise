package by.beltelecom.innowise.common.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = PhotoEntity.TABLE_NAME)
data class PhotoEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = COLUMN_ID)
    val id: Long,
    val width: Int,
    val height: Int,
    val photographer: String,
    @Embedded
    val sources: SourceEntity
) {

    companion object {
        const val TABLE_NAME = "photos"

        const val COLUMN_ID = "id"
    }
}

@Entity
data class SourceEntity(
    val original: String,
    val portrait: String,
    val large2x: String,
    val large: String,
    val medium: String,
    val small: String
)
