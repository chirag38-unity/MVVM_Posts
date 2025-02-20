package com.cr.mvvmposts.features.posts.presentation.posts

import com.cr.mvvmposts.features.posts.domain.model.Post
import com.cr.mvvmposts.features.posts.domain.util.PostOrder

sealed class PostsEvent {
    data class Order(val postOrder: PostOrder): PostsEvent()
    data class UpVotePost(val post: Post): PostsEvent()
    data class DownVotePost(val post: Post): PostsEvent()
    data object ToggleOrderSection: PostsEvent()
}