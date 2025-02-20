package com.cr.mvvmposts.features.posts.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Entity
@Serializable
@Parcelize
data class Post (
    @PrimaryKey val id : Int? = null,
    var title : String,
    var description : String,
    var authorName : String,
    var timestamp : Long,
    var upVotes : Int = 0,
    var downVotes : Int = 0
) : Parcelable

class InvalidPostException(message : String) : Exception(message)