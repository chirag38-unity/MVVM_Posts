package com.cr.mvvmposts.features.posts.presentation.add_edit_post.utils

sealed class UiEvent {
    data class ShowSnackbar(val message: String): UiEvent()
    data object SavePost: UiEvent()
}