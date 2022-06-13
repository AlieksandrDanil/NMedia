package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.dto.PostsFilled

class PostRepositoryImpl(
    private val dao: PostDao,
) : PostRepository {
    private val dataPost = MutableLiveData(PostsFilled.empty)
    // private var posts = PostsFilled.postsFilled.reversed() //emptyList<Post>()

    init {
        // для первоначальной записи первых постов
        // for(post in posts) { dao.save(PostEntity.fromDto(post)) }
    }

    override fun getAll() = Transformations.map(dao.getAll()) { list ->
        list.map {
            it.toDto()
        }
    }

    override fun getPost(): LiveData<Post> = dataPost
    override fun getPostById(id: Long): Post? {
        dataPost.value = dao.getById(id).toDto()
        return dataPost.value
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
        getPostById(id)
    }

    override fun shareById(id: Long) {
        dao.shareById(id)
        getPostById(id)
    }

    override fun save(post: Post) {
        dao.save(PostEntity.fromDto(post))
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
    }
}