package com.cr.mvvmposts.features.posts.domain.usecase

import com.cr.mvvmposts.features.posts.data.repository.PostsRepository
import com.cr.mvvmposts.features.posts.domain.model.Post
import com.cr.mvvmposts.features.posts.domain.util.OrderType
import com.cr.mvvmposts.features.posts.domain.util.PostOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetPosts (
    private val repository: PostsRepository
) {

    suspend operator fun invoke (
        postOrder: PostOrder = PostOrder.Date(OrderType.Descending)
    ) : Flow<List<Post>> {
        return repository.getPosts().map { posts ->
            when (postOrder.orderType) {
                is OrderType.Ascending -> {
                    when (postOrder) {
                        is PostOrder.Author -> posts.sortedBy { it.authorName.lowercase() }
                        is PostOrder.Date -> posts.sortedBy { it.timestamp }
                        is PostOrder.Title -> posts.sortedBy { it.title.lowercase() }
                        is PostOrder.DownVotes -> posts.sortedBy { it.downVotes }
                        is PostOrder.UpVotes -> posts.sortedBy { it.upVotes }
                    }
                }
                is OrderType.Descending -> {
                    when (postOrder) {
                        is PostOrder.Author -> posts.sortedByDescending { it.authorName.lowercase() }
                        is PostOrder.Date -> posts.sortedByDescending { it.timestamp }
                        is PostOrder.Title -> posts.sortedByDescending { it.title.lowercase() }
                        is PostOrder.DownVotes -> posts.sortedByDescending { it.downVotes }
                        is PostOrder.UpVotes -> posts.sortedByDescending { it.upVotes }
                    }
                }
            }
        }
    }

}