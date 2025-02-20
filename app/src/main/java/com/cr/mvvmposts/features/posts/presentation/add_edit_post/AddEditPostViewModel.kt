package com.cr.mvvmposts.features.posts.presentation.add_edit_post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cr.mvvmposts.features.posts.domain.model.InvalidPostException
import com.cr.mvvmposts.features.posts.domain.model.Post
import com.cr.mvvmposts.features.posts.domain.usecase.PostUseCase
import com.cr.mvvmposts.features.posts.presentation.add_edit_post.utils.AddEditPostEvent
import com.cr.mvvmposts.features.posts.presentation.add_edit_post.utils.AddEditPostsState
import com.cr.mvvmposts.features.posts.presentation.add_edit_post.utils.UiEvent
import com.cr.mvvmposts.features.posts.presentation.add_edit_post.utils.validation.ValidationUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddEditPostViewModel constructor(
    private val postUseCase: PostUseCase,
    private val validationUseCase: ValidationUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AddEditPostsState())
    val state = _state
        .onStart {  }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            AddEditPostsState()
        )

    private val _eventFlow = MutableSharedFlow<UiEvent>(
        replay = 0,                 // No replayed events (default)
        extraBufferCapacity = 1,    // Allows buffering of one extra event
        onBufferOverflow = BufferOverflow.DROP_OLDEST // Drops oldest event if buffer overflows
    )
    val eventFlow = _eventFlow.asSharedFlow()



    private var currentPost : Post? = null

    fun setPostData(post: Post) {

        viewModelScope.launch {
            currentPost = post

            _state.update {
                it.copy(
                    title = it.title.copy(
                        text = post.title,
                    ),
                    description = it.description.copy(
                        text = post.description,
                    ),
                    authorName = it.authorName.copy(
                        text = post.authorName,
                    )
                )
            }

        }

    }

    fun processEvent(event : AddEditPostEvent) {

        _state.update {
            it.copy(
                title = it.title.copy(
                    error = null
                ),
                description = it.description.copy(
                    error = null
                ),
                authorName = it.authorName.copy(
                    error = null
                ),
            )
        }

        when (event) {
            is AddEditPostEvent.EnteredTitle -> {
                _state.update {
                    it.copy(
                        title = it.title.copy( text = event.value )
                    )
                }
            }

            is AddEditPostEvent.EnteredDescription -> {
                _state.update {
                    it.copy(
                        description = it.description.copy( text = event.value )
                    )
                }
            }

            is AddEditPostEvent.EnteredAuthorName -> {
                _state.update {
                    it.copy(
                        authorName = it.authorName.copy( text = event.value )
                    )
                }
            }

            AddEditPostEvent.SavePost -> {

                viewModelScope.launch {
                    val titleResult = validationUseCase.validateTitle(_state.value.title.text)
                    val descriptionResult = validationUseCase.validateDescription(_state.value.description.text)
                    val authorResult = validationUseCase.validateAuthorName(_state.value.authorName.text)

                    val hasError = listOf(
                        titleResult,
                        descriptionResult,
                        authorResult,
                    ).any { !it.successful }

                    if(hasError) {
                        _state.update {
                            it.copy(
                                title = it.title.copy(
                                    error = titleResult.errorMessage
                                ),
                                description = it.description.copy(
                                    error = descriptionResult.errorMessage
                                ),
                                authorName = it.authorName.copy(
                                    error = authorResult.errorMessage
                                )
                            )
                        }

                        return@launch
                    }

                    try {
                        postUseCase.addPost(
                            Post(
                                title = _state.value.title.text,
                                description = _state.value.description.text,
                                timestamp = System.currentTimeMillis(),
                                authorName = _state.value.authorName.text,
                                upVotes = currentPost?.upVotes ?: 0,
                                downVotes = currentPost?.downVotes ?: 0,
                                id = currentPost?.id
                            )
                        )
                        _eventFlow.emit(UiEvent.SavePost)
                    } catch(e: InvalidPostException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save post"
                            )
                        )
                    }
                }
            }
        }
    }

}