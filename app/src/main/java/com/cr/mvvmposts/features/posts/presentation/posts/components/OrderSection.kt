package com.cr.mvvmposts.features.posts.presentation.posts.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cr.mvvmposts.features.posts.domain.util.OrderType
import com.cr.mvvmposts.features.posts.domain.util.PostOrder

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    postOrder: PostOrder = PostOrder.Date(OrderType.Descending),
    onOrderChange: (PostOrder) -> Unit
) {

    Card (
        elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)
    ) {
        Column(
            modifier = modifier
        ) {
            FlowRow (
                modifier = Modifier.fillMaxWidth(),
            ) {
//            DefaultRadioButton(
//                text = "Title",
//                selected = postOrder is PostOrder.Title,
//                onSelect = { onOrderChange(PostOrder.Title(postOrder.orderType)) }
//            )
//            Spacer(modifier = Modifier.width(8.dp))
                DefaultRadioButton(
                    text = "Date",
                    selected = postOrder is PostOrder.Date,
                    onSelect = { onOrderChange(PostOrder.Date(postOrder.orderType)) }
                )
                Spacer(modifier = Modifier.width(8.dp))
//            DefaultRadioButton(
//                text = "Author",
//                selected = postOrder is PostOrder.Author,
//                onSelect = { onOrderChange(PostOrder.Author(postOrder.orderType)) }
//            )
//            Spacer(modifier = Modifier.width(8.dp))
                DefaultRadioButton(
                    text = "Up Votes",
                    selected = postOrder is PostOrder.UpVotes,
                    onSelect = { onOrderChange(PostOrder.UpVotes(postOrder.orderType)) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                DefaultRadioButton(
                    text = "Down Votes",
                    selected = postOrder is PostOrder.DownVotes,
                    onSelect = { onOrderChange(PostOrder.DownVotes(postOrder.orderType)) }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                DefaultRadioButton(
                    text = "Ascending",
                    selected = postOrder.orderType is OrderType.Ascending,
                    onSelect = {
                        onOrderChange(postOrder.copy(OrderType.Ascending))
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                DefaultRadioButton(
                    text = "Descending",
                    selected = postOrder.orderType is OrderType.Descending,
                    onSelect = {
                        onOrderChange(postOrder.copy(OrderType.Descending))
                    }
                )
            }
        }
    }
    }

