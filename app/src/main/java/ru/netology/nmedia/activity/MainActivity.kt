package ru.netology.nmedia.activity

import android.content.Intent
import androidx.activity.result.launch
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        val adapter = PostsAdapter(object : OnInteractionListener {

            private val editPostLauncher =
                registerForActivityResult(NewPostActivity.EditPostResultContract) { result ->
                    result ?: return@registerForActivityResult
                    viewModel.changeContent(result)
                    viewModel.save()
                }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
                editPostLauncher.launch(post.content)
            }

            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

//            override fun onShare(post: Post) {
//                viewModel.shareById(post.id)
//            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

//            override fun onAdd() {
//                binding.group.visibility = View.VISIBLE
//            }

            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }

                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
            }
        })

        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        val newPostLauncher =
            registerForActivityResult(NewPostActivity.NewPostResultContract) { result ->
                result ?: return@registerForActivityResult
                viewModel.changeContent(result)
                viewModel.save()
            }

        binding.fab.setOnClickListener {
            newPostLauncher.launch()
        }

//        viewModel.edited.observe(this) { post ->
//            if (post.id == 0L) {
//                return@observe
//            }
//            with(binding.content) {
//                requestFocus()
//                setText(post.content)
//            }
//            binding.group.visibility = View.VISIBLE
//        }
//
//        binding.save.setOnClickListener {
//            with(binding.content) {
//                if (text.isNullOrBlank()) {
//                    Toast.makeText(
//                        this@MainActivity,
//                        context.getString(R.string.error_empty_content),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    return@setOnClickListener
//                }
//
//                viewModel.changeContent(text.toString())
//                viewModel.save()
//
//                setText("")
//                clearFocus()
//                AndroidUtils.hideKeyboard(this)
//            }
//            binding.group.visibility = View.GONE
//        }
//
//        binding.cancel.setOnClickListener {
//            with(binding.content) {
//                text.clear()
//                setText("")
//                clearFocus()
//                AndroidUtils.hideKeyboard(this)
//            }
//            binding.group.visibility = View.GONE
//        }
    }
}