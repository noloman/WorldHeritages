package me.manulorenzo.worldheritages.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import me.manulorenzo.worldheritages.data.model.Heritage

@Entity(tableName = "heritages_table")
data class HeritageEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val jsonId: String,
    val year: Int,
    val target: String,
    val name: String,
    val type: String,
    val region: String,
    val regionLong: String,
    val lat: Double,
    val lng: Double,
    val page: String,
    val image: String,
    val imageAuthor: String,
    val shortInfo: String,
    val longInfo: String?
)

fun HeritageEntity.toModel() = Heritage(
    jsonId,
    year,
    target,
    name,
    type,
    region,
    regionLong,
    lat,
    lng,
    page,
    image,
    imageAuthor,
    shortInfo,
    longInfo
)