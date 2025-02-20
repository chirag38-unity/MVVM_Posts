package com.cr.mvvmposts.features.posts.domain.usecase

import com.cr.mvvmposts.features.posts.data.repository.PostsRepository
import com.cr.mvvmposts.features.posts.domain.model.InvalidPostException
import com.cr.mvvmposts.features.posts.domain.model.Post

class AddPost (
    private val repository: PostsRepository
) {

    @Throws(InvalidPostException::class)
    suspend operator fun invoke (post: Post) {
        if (post.title.isBlank()) {
            throw InvalidPostException("Title can't be empty")
        }
        if (post.description.isBlank()) {
            throw InvalidPostException("Description can't be empty")
        }
        if (post.authorName.isBlank()) {
            throw InvalidPostException("Author name can't be empty")
        }

        repository.upsertPost(post)

    }

}