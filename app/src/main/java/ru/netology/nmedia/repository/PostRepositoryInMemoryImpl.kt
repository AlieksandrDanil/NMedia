package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.PostsFilled

class PostRepositoryInMemoryImpl : PostRepository {
    private var nextId = 1L
    private var posts = PostsFilled.postsFilled
    private val data = MutableLiveData(posts)
    private val dataPost = MutableLiveData(PostsFilled.empty)

    override fun getAll(): LiveData<List<Post>> = data

    override fun getPost(): LiveData<Post> = dataPost
    override fun getPostById(id: Long): Post? {
        dataPost.value = posts.find {
            it.id == id
        }
        return dataPost.value
    }

    override fun save(post: Post) {
        if (post.id == 0L) {
            // remove hardcoded author & published
            posts = listOf(
                post.copy(
                    id = nextId++,
                    author = "Me",
                    likedByMe = false,
                    published = "now"
                )
            ) + posts
            data.value = posts
            return
        }

        posts = posts.map {
            if (it.id != post.id) it else it.copy(content = post.content)
        }
        data.value = posts
    }

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id)
                it
            else
                it.copy(
                    likedByMe = !it.likedByMe,
                    likes = if (!it.likedByMe) it.likes + 1 else it.likes - 1
                )
        }
        data.value = posts
    }

    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id)
                it
            else
                it.copy(shared = it.shared + 1)
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }
}