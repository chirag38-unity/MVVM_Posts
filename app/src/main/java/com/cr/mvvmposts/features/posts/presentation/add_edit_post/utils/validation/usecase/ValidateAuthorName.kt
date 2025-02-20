package com.cr.mvvmposts.features.posts.presentation.add_edit_post.utils.validation.usecase

import com.cr.mvvmposts.features.posts.presentation.add_edit_post.utils.validation.ValidationResult

class ValidateAuthorName {

    operator fun invoke (authorName: String) : ValidationResult {
        if(authorName.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "The author name can't be blank"
            )
        }

        return ValidationResult(
            successful = true
        )

    }

}