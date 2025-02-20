package com.cr.mvvmposts.features.posts.presentation.add_edit_post.utils.validation.usecase

import com.cr.mvvmposts.features.posts.presentation.add_edit_post.utils.validation.ValidationResult

class ValidateDescription {

    operator fun invoke (description: String) : ValidationResult {
        if(description.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "The description can't be blank"
            )
        }

        return ValidationResult(
            successful = true
        )

    }

}