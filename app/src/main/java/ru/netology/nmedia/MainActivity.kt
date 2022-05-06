package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewmodel.PostViewModel
import ru.netology.nmedia.servicecode.plural

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        viewModel.data.observe(this) { post ->
            with(binding) {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                favorite.setImageResource(
                    if (post.likedByMe) R.drawable.ic_favorite_24dp_red else R.drawable.ic_favorite_24dp
                )
                favoriteCount.text = plural(post.likes, 'K', 'M')
                shareCount.text = plural(post.shared, 'K', 'M')
                visibilityCount.text = plural(post.viewed, 'K', 'M')
            }
        }

        binding.favorite.setOnClickListener {
            viewModel.like()
        }

        binding.share.setOnClickListener {
            viewModel.share()
        }
    }
}