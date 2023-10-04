package by.beltelecom.innowise.common.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = CommonEntity.TABLE_NAME)
data class CommonEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = COLUMN_ID)
    val id: Long,
    val url: String
) {
    companion object {
        const val TABLE_NAME = "common"

        const val COLUMN_ID = "id"
    }
}
