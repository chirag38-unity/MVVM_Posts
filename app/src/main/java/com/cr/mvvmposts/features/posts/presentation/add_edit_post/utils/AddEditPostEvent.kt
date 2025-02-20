package com.cr.mvvmposts.features.posts.presentation.add_edit_post.utils

sealed class AddEditPostEvent {
    data class EnteredTitle(val value: String): AddEditPostEvent()
    data class EnteredDescription(val value: String): AddEditPostEvent()
    data class EnteredAuthorName(val value: String): AddEditPostEvent()
    data object SavePost: AddEditPostEvent()
}
