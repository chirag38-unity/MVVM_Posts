package com.cr.mvvmposts.features.posts.presentation.view_post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.cr.mvvmposts.features.posts.domain.model.Post
import com.cr.mvvmposts.features.posts.presentation.util.SlideTransition
import com.cr.mvvmposts.features.posts.presentation.util.toReadableDate
import com.cr.mvvmposts.features.posts.presentation.view_post.components.DeletionConfirmationDialog
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.AddEditPostScreenDestination
import com.ramcosta.composedestinations.generated.destinations.ViewPostScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Destination<RootGraph>(
    style = SlideTransition::class
)
@Composable
fun ViewPostScreen(
    post: Post,
    rootNavigator: DestinationsNavigator,
    viewModel: ViewPostScreenViewModel = koinViewModel(),
) {

    ViewPostScreenContent(
        post = post,
        onScreenEvent = {
            when(it) {
                ViewPostsScreenEvents.DeletePost -> {
                    viewModel.deletePost(post)
                    rootNavigator.popBackStack()
                }
                ViewPostsScreenEvents.EditPost -> {
                    rootNavigator.navigate(AddEditPostScreenDestination(post = post)) {
                        popUpTo(ViewPostScreenDestination){
                            inclusive = true
                        }
                    }
                }
                ViewPostsScreenEvents.ReturnHome -> {
                    rootNavigator.popBackStack()
                }
            }
        },
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewPostScreenContent(
    post: Post,
    onScreenEvent : (ViewPostsScreenEvents) -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var isDeleteDialogVisible by remember { mutableStateOf(false) }

    Scaffold (
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {

                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "View Post",
                            modifier = Modifier.align(Alignment.Center)
                        )

                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = "Return to Posts Page",
                            modifier = Modifier.clickable(
                                onClick = {
                                    onScreenEvent.invoke(ViewPostsScreenEvents.ReturnHome)
                                }
                            )
                        )

                    }


                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { paddingValues ->

        Column (
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start

        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Post Title",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = post.title,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Post Description",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = post.description,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "By Author",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = post.authorName,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Date",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = post.timestamp.toReadableDate(),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon( imageVector = Icons.Filled.ThumbUp, contentDescription = "Up Vote Post" )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = post.upVotes.toString(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon( imageVector = Icons.Filled.ThumbDown, contentDescription = "Down Vote Post" )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = post.downVotes.toString(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

            }

            Spacer(modifier = Modifier.height(16.dp))

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = {
                        onScreenEvent.invoke(ViewPostsScreenEvents.EditPost)
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {

                    Row (
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Icon( imageVector = Icons.Filled.Edit, contentDescription = "Edit Post" )

                        Spacer(modifier = Modifier.width(3.dp))

                        Text(
                            text = "Edit",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }


                }

                OutlinedButton(
                    onClick = { isDeleteDialogVisible = true },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {

                    Row (
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Icon( imageVector = Icons.Filled.Delete, contentDescription = "Delete Post" )

                        Spacer(modifier = Modifier.width(3.dp))

                        Text(
                            text = "Delete",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }


                }

            }

        }

        if (isDeleteDialogVisible) {
            DeletionConfirmationDialog(
                onDelete = {
                    isDeleteDialogVisible = false
                    onScreenEvent.invoke(ViewPostsScreenEvents.DeletePost)
                },
                onCancel = { isDeleteDialogVisible = false }
            )
        }

    }

}