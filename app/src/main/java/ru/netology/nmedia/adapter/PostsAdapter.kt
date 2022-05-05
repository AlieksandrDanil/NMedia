package ru.netology.nmedia.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.servicecode.plural

interface OnInteractionListener {
    fun onLike(post: Post) {}
    fun onShare(post: Post) {}
}

class PostsAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardPostBinding.inflate(inflater, parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            favorite.setImageResource(
                if (post.likedByMe) R.drawable.ic_favorite_24dp_red else R.drawable.ic_favorite_24dp
            )
            favoriteCount.text = plural(post.likes, 'K', 'M')
            shareCount.text = plural(post.shared, 'K', 'M')
            visibilityCount.text = plural(post.viewed, 'K', 'M')
//            if (post.likedByMe) {
//                like.setImageResource(R.drawable.ic_liked_24)
//            }

            binding.root.setOnClickListener {
                Log.d("stuff", "stuff")
            }
            binding.avatar.setOnClickListener {
                Log.d("stuff", "avatar")
            }
            favorite.setOnClickListener {
                Log.d("stuff", "like")
                onInteractionListener.onLike(post)
            }
            share.setOnClickListener {
                Log.d("stuff", "shared")
                onInteractionListener.onShare(post)
            }
            binding.visibility.setOnClickListener {
                Log.d("stuff", "viewed")
            }
        }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}