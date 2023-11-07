
import com.example.dicodingstoryapp.data.remote.ListStoryItem
import com.example.dicodingstoryapp.databinding.ItemStoryBinding




import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater

import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide



class StoryAdapter : ListAdapter<ListStoryItem, StoryAdapter.ViewHolder>(DIFF_CALLBACK) {





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = getItem(position)

        holder.bind(story)

        holder.itemView.setOnClickListener {

           Toast.makeText(holder.itemView.context, story.name, Toast.LENGTH_SHORT).show()



        }


    }

    class ViewHolder(val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind(story: ListStoryItem) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .into(binding.imageCard)

                titleCard.text = story.name
                descriptionCard.text = story.description
                createdAt.text = story.createdAt
            }

        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

        }

        const val NAME = "NAME"
        const val AVATAR = "AVATAR"
    }



}

