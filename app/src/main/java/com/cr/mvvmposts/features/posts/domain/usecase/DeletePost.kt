package com.cr.mvvmposts.features.posts.domain.usecase

import com.cr.mvvmposts.features.posts.data.repository.PostsRepository
import com.cr.mvvmposts.features.posts.domain.model.Post

class DeletePost (
    private val repository: PostsRepository
) {

    suspend operator fun invoke (post: Post) {
        repository.deletePost(post)
    }

}