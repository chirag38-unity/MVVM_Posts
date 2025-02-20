package com.cr.mvvmposts.features.posts.presentation.add_edit_post.utils.validation

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)