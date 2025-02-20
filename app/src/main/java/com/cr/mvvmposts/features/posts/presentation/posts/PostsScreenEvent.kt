package com.cr.mvvmposts.features.posts.presentation.posts

import com.cr.mvvmposts.features.posts.domain.model.Post

sealed class PostsScreenEvent {
    data object AddPost: PostsScreenEvent()
    data class OpenPost(val post: Post): PostsScreenEvent()
}