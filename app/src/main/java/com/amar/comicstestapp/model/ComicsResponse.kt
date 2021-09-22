package com.amar.comicstestapp.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ComicsResponse(

    @SerializedName("data")
    var data: ComicsResponseData

)

data class ComicsResponseData (

    @SerializedName("offset")
    var offset: Int,

    @SerializedName("limit")
    var limit: Int,

    @SerializedName("total")
    var total: Int,

    @SerializedName("count")
    var count: Int,

    @SerializedName("results")
    var results: ArrayList<Comic>
)

@Entity(tableName = "comics")
data class Comic(

    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    var comicId: Int,

    @ColumnInfo(name = "title")
    @SerializedName("title")
    var title: String,

    @ColumnInfo(name = "description", defaultValue = "")
    @SerializedName("description")
    var description: String,

    @Embedded
    @SerializedName("thumbnail")
    var thumbnail: Thumbnail,

    ) : Serializable {
        public fun getImageUrl(): String {
            return String.format("%s.%s", thumbnail.path, thumbnail.extension)
        }
    }

data class Thumbnail(

    @ColumnInfo(name = "path", defaultValue = "")
    @SerializedName("path")
    var path: String,

    @ColumnInfo(name = "extension", defaultValue = "")
    @SerializedName("extension")
    var extension: String,

    ) : Serializable