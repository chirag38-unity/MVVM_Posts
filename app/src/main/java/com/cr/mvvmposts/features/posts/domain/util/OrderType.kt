package com.cr.mvvmposts.features.posts.domain.util

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class OrderType : Parcelable {
    data object Ascending: OrderType()
    data object Descending: OrderType()
}