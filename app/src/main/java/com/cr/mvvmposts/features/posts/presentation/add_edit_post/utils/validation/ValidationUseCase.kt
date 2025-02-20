package com.cr.mvvmposts.features.posts.presentation.add_edit_post.utils.validation

import com.cr.mvvmposts.features.posts.presentation.add_edit_post.utils.validation.usecase.ValidateAuthorName
import com.cr.mvvmposts.features.posts.presentation.add_edit_post.utils.validation.usecase.ValidateDescription
import com.cr.mvvmposts.features.posts.presentation.add_edit_post.utils.validation.usecase.ValidateTitle

data class ValidationUseCase(
    val validateTitle: ValidateTitle,
    val validateDescription: ValidateDescription,
    val validateAuthorName: ValidateAuthorName
)
