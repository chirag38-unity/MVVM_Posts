package com.cr.mvvmposts.features.posts.presentation.add_edit_post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cr.mvvmposts.features.posts.domain.model.Post
import com.cr.mvvmposts.features.posts.presentation.add_edit_post.utils.AddEditPostEvent
import com.cr.mvvmposts.features.posts.presentation.add_edit_post.utils.AddEditPostsState
import com.cr.mvvmposts.features.posts.presentation.add_edit_post.utils.UiEvent
import com.cr.mvvmposts.features.posts.presentation.util.SlideTransition
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Destination<RootGraph>(
    style = SlideTransition::class
)
@Composable
fun AddEditPostScreen(
    post: Post? = null,
    viewModel: AddEditPostViewModel = koinViewModel(),
    rootNavigator: DestinationsNavigator,
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    var isNewPost by remember { mutableStateOf(false) }

    LaunchedEffect ( Unit) {
        if (post != null) {
            viewModel.setPostData(post)
        } else {
            isNewPost = true
        }
    }

    AddEditPostScreenContent(
        state = state,
        eventFlow = viewModel.eventFlow,
        isNewPost = isNewPost,
        closeScreen = {
            rootNavigator.popBackStack()
        },
        sendEvent = { event ->
            viewModel.processEvent(event)
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEditPostScreenContent(
    state: AddEditPostsState,
    eventFlow : Flow<UiEvent>,
    isNewPost : Boolean,
    closeScreen : () -> Unit,
    sendEvent : (AddEditPostEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val titleFocusRequester = remember { FocusRequester() }
    val descriptionFocusRequester = remember { FocusRequester() }
    val authorFocusRequester = remember { FocusRequester() }

    LaunchedEffect(state) {
        when {
            !state.title.error.isNullOrEmpty() -> titleFocusRequester.requestFocus()
            !state.description.error.isNullOrEmpty() -> descriptionFocusRequester.requestFocus()
            !state.authorName.error.isNullOrEmpty() -> authorFocusRequester.requestFocus()
        }
    }

    LaunchedEffect (Unit) {
        eventFlow.collectLatest { event ->
            when(event) {
                UiEvent.SavePost -> { closeScreen.invoke() }
                is UiEvent.ShowSnackbar -> TODO()
            }
        }
    }

    Scaffold (
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = if (isNewPost)"Create Post" else "Edit Post",
                            modifier = Modifier.align(Alignment.Center)
                        )

                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = "Return to Posts Page",
                            modifier = Modifier.clickable(
                                onClick = closeScreen
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
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Post Title",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier =  Modifier.height(4.dp))
            OutlinedTextField(
                value = state.title.text,
                isError = !state.title.error.isNullOrEmpty(),
                placeholder = {
                    Text(
                        text = state.title.hint,
                        style = MaterialTheme.typography.bodyMedium
                    )
                } ,
                onValueChange = {
                    sendEvent(AddEditPostEvent.EnteredTitle(it))
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(titleFocusRequester)
            )
            if (state.title.error != null) {
                Text(
                    text = state.title.error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.End)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Post Description",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier =  Modifier.height(4.dp))
            OutlinedTextField(
                value = state.description.text,
                placeholder = {
                    Text(
                        text = state.description.hint,
                        style = MaterialTheme.typography.bodyMedium
                    )
                } ,
                modifier = modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp)
                    .focusRequester(descriptionFocusRequester),
                onValueChange = {
                    sendEvent(AddEditPostEvent.EnteredDescription(it))
                },
                textStyle = MaterialTheme.typography.bodyMedium
            )
            if (state.description.error != null) {
                Text(
                    text = state.description.error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.End)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Post Author",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier =  Modifier.height(4.dp))
            OutlinedTextField(
                value = state.authorName.text,
                placeholder = {
                    Text(
                        text = state.authorName.hint,
                        style = MaterialTheme.typography.bodyMedium
                    )
                } ,
                modifier = modifier
                    .fillMaxWidth()
                    .focusRequester(authorFocusRequester),
                singleLine = true,
                onValueChange = {
                    sendEvent(AddEditPostEvent.EnteredAuthorName(it))
                },
                textStyle = MaterialTheme.typography.bodyMedium
            )
            if (state.authorName.error != null) {
                Text(
                    text = state.authorName.error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.End)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    sendEvent.invoke(AddEditPostEvent.SavePost)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 350.dp)
            ) {
                Text(
                    text = if (isNewPost) "Submit" else "Save Changes"
                )
            }

        }

    }

}