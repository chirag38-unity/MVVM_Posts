package com.cr.mvvmposts.features.posts.presentation.view_post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cr.mvvmposts.features.posts.domain.model.Post
import com.cr.mvvmposts.features.posts.domain.usecase.PostUseCase
import kotlinx.coroutines.launch

class ViewPostScreenViewModel constructor(
    private val postUseCase: PostUseCase
) : ViewModel() {

    fun deletePost(post: Post) {
        viewModelScope.launch {
            postUseCase.deletePost(post)
        }
    }

}