package com.cr.mvvmposts.features.posts.domain.util

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class PostOrder(open val orderType: OrderType) : Parcelable {

    class Title(override val orderType: OrderType) : PostOrder(orderType)
    class Author(override val orderType: OrderType) : PostOrder(orderType)
    class Date(override val orderType: OrderType) : PostOrder(orderType)
    class UpVotes(override val orderType: OrderType) : PostOrder(orderType)
    class DownVotes(override val orderType: OrderType) : PostOrder(orderType)

    fun copy(orderType: OrderType) : PostOrder {
        return when(this) {
            is Title -> Title(orderType)
            is Author -> Author(orderType)
            is Date -> Date(orderType)
            is DownVotes -> DownVotes(orderType)
            is UpVotes -> UpVotes(orderType)
        }
    }

}