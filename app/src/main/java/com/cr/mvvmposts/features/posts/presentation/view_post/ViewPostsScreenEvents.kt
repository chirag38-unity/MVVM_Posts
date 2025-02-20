package com.cr.mvvmposts.features.posts.presentation.view_post

import com.cr.mvvmposts.features.posts.presentation.posts.PostsScreenEvent

sealed class ViewPostsScreenEvents {
    data object EditPost: ViewPostsScreenEvents()
    data object DeletePost: ViewPostsScreenEvents()
    data object ReturnHome: ViewPostsScreenEvents()
}