package com.cr.mvvmposts.features.posts.presentation.posts

import com.cr.mvvmposts.features.posts.domain.util.OrderType
import com.cr.mvvmposts.features.posts.domain.util.PostOrder

data class PostUIState (
    val postOrder: PostOrder = PostOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)