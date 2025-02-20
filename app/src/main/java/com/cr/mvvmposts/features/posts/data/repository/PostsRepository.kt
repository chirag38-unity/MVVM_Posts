package com.cr.mvvmposts.features.posts.data.repository

import com.cr.mvvmposts.features.posts.data.local.PostDao
import com.cr.mvvmposts.features.posts.domain.model.Post
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class PostsRepository (
    private val postsDao: PostDao
) {

    suspend fun getPosts() : Flow<List<Post>> {
        return postsDao.getPosts()
            .flowOn(Dispatchers.IO)
    }

    suspend fun getPostById(id : Int) : Post? {
        return withContext(Dispatchers.IO) {
            postsDao.getPostById(id)
        }
    }

    suspend fun upsertPost (post: Post) = withContext(Dispatchers.IO) {
        try {
            postsDao.insertPost(post)
        } catch (e: Exception) {
            Napier.e("Failed to insert post: ${e.localizedMessage}", e)
        }
    }

    suspend fun deletePost (post: Post) = withContext(Dispatchers.IO) {
        try {
            postsDao.deletePost(post)
        } catch (e: Exception) {
            Napier.e("Failed to delete post: ${e.localizedMessage}", e)
        }
    }

}