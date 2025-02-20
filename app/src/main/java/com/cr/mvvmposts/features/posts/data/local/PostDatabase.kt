package com.cr.mvvmposts.features.posts.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cr.mvvmposts.features.posts.domain.model.Post

@Database(
    entities = [Post::class],
    version = 1
)
abstract class PostDatabase : RoomDatabase() {

    abstract val postDao : PostDao

    companion object {
        const val DATABASE_NAME = "posts_db"
    }

}