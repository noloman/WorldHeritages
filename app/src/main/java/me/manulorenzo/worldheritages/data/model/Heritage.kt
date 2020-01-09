package me.manulorenzo.worldheritages.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Heritage(
    val id: String,
    val year: Int,
    val target: String, // Something like country code such as ECU
    val name: String,
    val type: String,
    val region: String,
    val regionLong: String,
//    val coordinates: Coordinates, // "N0 0 14 W78 30 0"
    val lat: Double, // 0.0038888888888888888
    val lng: Double, // -78.5
    val page: String, //"http://whc.unesco.org/en/list/2"
    val image: String, // "http://whc.unesco.org//uploads/thumbs/site_0002_0001-750-0-20061215143310.jpg",
    val imageAuthor: String,
    val shortInfo: String,
    val longInfo: String
)