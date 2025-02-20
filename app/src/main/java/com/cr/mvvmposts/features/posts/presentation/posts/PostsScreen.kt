package com.cr.mvvmposts.features.posts.presentation.posts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cr.mvvmposts.features.posts.domain.model.Post
import com.cr.mvvmposts.features.posts.presentation.posts.components.OrderSection
import com.cr.mvvmposts.features.posts.presentation.posts.components.PostItem
import com.cr.mvvmposts.features.posts.presentation.util.SlideTransition
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.AddEditPostScreenDestination
import com.ramcosta.composedestinations.generated.destinations.ViewPostScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Destination<RootGraph>(
    start = true,
    style = SlideTransition::class
)
@Composable
fun PostsScreen(
    rootNavigator: DestinationsNavigator,
    viewModel: PostsScreenViewModel = koinViewModel()
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val posts = viewModel.posts

    PostsScreenContent(
        posts = posts,
        state = state,
        onPostEvent = { event ->
            viewModel.processEvent(event)
        },
        onPostScreenEvent = { event ->
            when (event) {
                PostsScreenEvent.AddPost -> {
                    rootNavigator.navigate(AddEditPostScreenDestination())
                }
                is PostsScreenEvent.OpenPost -> {
                    rootNavigator.navigate(ViewPostScreenDestination(post = event.post))
                }
            }
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun PostsScreenContent(
    posts : List<Post>,
    state: PostUIState,
    onPostEvent: (PostsEvent) -> Unit,
    onPostScreenEvent : (PostsScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold (
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onPostScreenEvent.invoke(PostsScreenEvent.AddPost)
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add note")
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "All Posts",
                            style = MaterialTheme.typography.headlineMedium
                        )

                        IconButton(
                            onClick = {
                                onPostEvent.invoke(PostsEvent.ToggleOrderSection)
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Sort,
                                contentDescription = "Sort"
                            )
                        }

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

        Box (
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {

            if (posts.isNotEmpty()) {
                LazyColumn (
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 15.dp, vertical = 10.dp)
                ) {
                    items(
                        items = posts,
                        key = { it.id!! }
                    ) { post ->

                        PostItem(
                            post = post,
                            upVotePost = {onPostEvent.invoke(PostsEvent.UpVotePost(post))},
                            downVotePost = {onPostEvent.invoke(PostsEvent.DownVotePost(post))},
                            viewPost = { onPostScreenEvent.invoke(PostsScreenEvent.OpenPost(post)) },
                            modifier = Modifier.animateItemPlacement()
                        )

                    }
                }
            } else {
                Text(
                    text = "Try adding a few new posts..." ,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                OrderSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    postOrder = state.postOrder,
                    onOrderChange = {
                        onPostEvent.invoke(PostsEvent.Order(it))
                    }
                )
            }


        }

    }

}