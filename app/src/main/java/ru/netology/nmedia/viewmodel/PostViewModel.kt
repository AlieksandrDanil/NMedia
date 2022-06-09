package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.PostsFilled
import ru.netology.nmedia.repository.*

private val empty = PostsFilled.empty

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryImpl(
        AppDb.getInstance(context =  application).postDao()
    )
    val data = repository.getAll()
    val dataPost = repository.getPost()
    val edited = MutableLiveData(empty)

    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        toEmpty()
    }

    fun toEmpty() {
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)
    fun getPostById(id: Long) = repository.getPostById(id)
}


class DraftContentPostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositorySharedPrefsImpl(application)

    fun save(content: String) {
        val draftPost = Post(
            id = getConstDraftPostId(),
            content = content,
            author = "",
            likedByMe = false,
            published = ""
        )
        repository.save(draftPost)
    }

    fun getPostById(id: Long) = repository.getPostById(id)
}