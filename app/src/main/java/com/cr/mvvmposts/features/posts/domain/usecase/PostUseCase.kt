package com.cr.mvvmposts.features.posts.domain.usecase

data class PostUseCase(
    val getPosts: GetPosts,
    val getPost: GetPost,
    val addPost: AddPost,
    val deletePost: DeletePost
)
