package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.PostsFilled

class PostRepositoryFileImpl(
    private val context: Context,
) : PostRepository {
    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val filename = "posts.json"
    private var nextId = 1L

    private var posts = PostsFilled.postsFilled //emptyList<Post>()
    private val data = MutableLiveData(posts)
    private val dataPost = MutableLiveData(PostsFilled.empty)

    init {
        //sync()   // для первоначальной записи первых постов
        val file = context.filesDir.resolve(filename)
        if (file.exists()) {
            context.openFileInput(filename).bufferedReader().use {
                posts = gson.fromJson(it, type)
                data.value = posts
            }
        } else {
            sync()
        }
    }

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
                    id = posts.maxOfOrNull { it.id + 1 } ?: nextId,
                    author = "Me",
                    likedByMe = false,
                    published = "now"
                )
            ) + posts
            data.value = posts
            sync()
            return
        }

        posts = posts.map {
            if (it.id != post.id) it else it.copy(content = post.content)
        }
        data.value = posts
        sync()
    }

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                likes = if (it.likedByMe) it.likes - 1 else it.likes + 1
            )
        }
        data.value = posts
        getPostById(id)
        sync()
    }

    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id)
                it
            else
                it.copy(shared = it.shared + 1)
        }
        data.value = posts
        getPostById(id)
        sync()
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
        sync()
    }

    private fun sync() {
        context.openFileOutput(filename, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(posts))
        }
    }
}