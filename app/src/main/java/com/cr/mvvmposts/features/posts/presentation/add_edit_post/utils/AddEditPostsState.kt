package com.cr.mvvmposts.features.posts.presentation.add_edit_post.utils

data class AddEditPostsState (
    var title : PostTextFieldState = PostTextFieldState(
        hint = "Enter Post Title..."
    ),
    var description : PostTextFieldState = PostTextFieldState(
        hint = "Write in brief about the post..."
    ),
    var authorName : PostTextFieldState = PostTextFieldState(
        hint = "John Doe"
    )
)