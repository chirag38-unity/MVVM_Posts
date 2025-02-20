package com.cr.mvvmposts.features.posts.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cr.mvvmposts.features.posts.domain.model.Post
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    @Query("SELECT * FROM post")
    fun getPosts() : Flow<List<Post>>

    @Query("SELECT * FROM post WHERE id = :id")
    suspend fun getPostById(id : Int) : Post?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post : Post)

    @Delete
    suspend fun deletePost(post : Post)

}