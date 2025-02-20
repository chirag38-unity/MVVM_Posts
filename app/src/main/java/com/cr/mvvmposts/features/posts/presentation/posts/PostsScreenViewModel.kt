package com.cr.mvvmposts.features.posts.presentation.posts

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cr.mvvmposts.features.posts.domain.model.Post
import com.cr.mvvmposts.features.posts.domain.usecase.PostUseCase
import com.cr.mvvmposts.features.posts.domain.util.OrderType
import com.cr.mvvmposts.features.posts.domain.util.PostOrder
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PostsScreenViewModel constructor(
    private val postUseCase: PostUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val POST_ORDER_KEY = "post_order"
    }

    private val _posts = mutableStateListOf<Post>()
    val posts: List<Post> = _posts

    private val _uiState = MutableStateFlow(PostUIState())
    val uiState = _uiState
        .onStart {

            val savedPostOrder = savedStateHandle.get<PostOrder>(POST_ORDER_KEY)
                ?: PostOrder.Date(OrderType.Descending)

            getPosts(savedPostOrder)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            PostUIState()
        )

    private var getPostsJob: Job? = null

    fun processEvent(event: PostsEvent) {
        when (event) {
            is PostsEvent.Order -> {
                if (_uiState.value.postOrder::class == event.postOrder::class &&
                    _uiState.value.postOrder.orderType == event.postOrder.orderType
                ) {
                    return
                }
                savedStateHandle[POST_ORDER_KEY] = event.postOrder
                getPosts(event.postOrder)
            }

            is PostsEvent.ToggleOrderSection -> {
                _uiState.update {
                    it.copy(
                        isOrderSectionVisible = !it.isOrderSectionVisible
                    )
                }
            }

            is PostsEvent.DownVotePost -> {
                val updatedPost = event.post.apply {
                    downVotes++
                }
                viewModelScope.launch {
                    postUseCase.addPost(updatedPost)
                }
            }
            is PostsEvent.UpVotePost -> {
                val updatedPost = event.post.apply {
                    upVotes++
                }
                viewModelScope.launch {
                    postUseCase.addPost(updatedPost)
                }
            }
        }
    }

    private fun getPosts(postOrder: PostOrder) {
        getPostsJob?.cancel()

        viewModelScope.launch {
            getPostsJob = postUseCase.getPosts(postOrder)
                .onEach { posts ->

                    _posts.clear()
                    _posts.addAll(posts)

                    _uiState.update {
                        it.copy(
                            postOrder = postOrder
                        )
                    }
                }
                .launchIn(this)
        }
    }

}