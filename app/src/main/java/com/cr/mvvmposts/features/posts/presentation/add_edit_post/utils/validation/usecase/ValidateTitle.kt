package com.cr.mvvmposts.features.posts.presentation.add_edit_post.utils.validation.usecase

import com.cr.mvvmposts.features.posts.domain.model.Post
import com.cr.mvvmposts.features.posts.presentation.add_edit_post.utils.validation.ValidationResult

class ValidateTitle {

    operator fun invoke (title: String) : ValidationResult {
        if(title.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "The title can't be blank"
            )
        }

        return ValidationResult(
            successful = true
        )

    }

}