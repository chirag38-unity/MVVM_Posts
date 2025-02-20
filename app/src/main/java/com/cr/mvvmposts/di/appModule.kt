package com.cr.mvvmposts.di

import androidx.lifecycle.SavedStateHandle
import androidx.room.Room
import com.cr.mvvmposts.features.posts.data.local.PostDatabase
import com.cr.mvvmposts.features.posts.data.repository.PostsRepository
import com.cr.mvvmposts.features.posts.domain.usecase.AddPost
import com.cr.mvvmposts.features.posts.domain.usecase.DeletePost
import com.cr.mvvmposts.features.posts.domain.usecase.GetPost
import com.cr.mvvmposts.features.posts.domain.usecase.GetPosts
import com.cr.mvvmposts.features.posts.domain.usecase.PostUseCase
import com.cr.mvvmposts.features.posts.presentation.add_edit_post.AddEditPostViewModel
import com.cr.mvvmposts.features.posts.presentation.add_edit_post.utils.validation.ValidationUseCase
import com.cr.mvvmposts.features.posts.presentation.add_edit_post.utils.validation.usecase.ValidateAuthorName
import com.cr.mvvmposts.features.posts.presentation.add_edit_post.utils.validation.usecase.ValidateDescription
import com.cr.mvvmposts.features.posts.presentation.add_edit_post.utils.validation.usecase.ValidateTitle
import com.cr.mvvmposts.features.posts.presentation.posts.PostsScreenViewModel
import com.cr.mvvmposts.features.posts.presentation.view_post.ViewPostScreenViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.scope.get
import org.koin.dsl.module

val appModule = module {

    single <PostDatabase> {
        Room.databaseBuilder(
            androidContext(),
            PostDatabase::class.java,
            PostDatabase.DATABASE_NAME
        ).build()
    }

    single <PostsRepository> { PostsRepository(get<PostDatabase>().postDao) }

    single <PostUseCase> {
        PostUseCase(
            getPosts = GetPosts(get()),
            getPost = GetPost(get()),
            addPost = AddPost(get()),
            deletePost = DeletePost(get())
        )
    }

    single <ValidationUseCase> {
        ValidationUseCase(
            validateTitle = ValidateTitle(),
            validateDescription = ValidateDescription(),
            validateAuthorName = ValidateAuthorName()
        )
    }

    viewModel <PostsScreenViewModel> { (handle: SavedStateHandle) ->
        PostsScreenViewModel(get(), handle)
    }
    viewModel <ViewPostScreenViewModel> { ViewPostScreenViewModel(get()) }
    viewModel <AddEditPostViewModel> { AddEditPostViewModel(postUseCase =  get(), validationUseCase = get()) }


}