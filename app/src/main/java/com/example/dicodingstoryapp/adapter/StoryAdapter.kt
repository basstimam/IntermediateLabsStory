import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingstoryapp.activity.DetailStoryActivity
import com.example.dicodingstoryapp.data.remote.ListStoryItem
import com.example.dicodingstoryapp.databinding.ItemStoryBinding


class StoryAdapter : ListAdapter<ListStoryItem, StoryAdapter.ViewHolder>(DIFF_CALLBACK) {





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = getItem(position)

        holder.bind(story)




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

            itemView.setOnClickListener {
                val bundle = Bundle().apply {
                    putString("NAME", story.name)
                    putString("IMAGE", story.photoUrl)
                    putString("DESCRIPTION", story.description)
                    putString("CREATEDAT", story.createdAt)

                    if (story.lat == null) putString("LAT", "Tidak Tersedia")
                    else putString("LAT", story.lat.toString())

                    if (story.lon == null) putString("LON", "Tidak Tersedia")
                    else putString("LON", story.lon.toString())
                }

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.imageCard, "detailImageStory"),
Pair(binding.titleCard, "detailTitleStory"),
Pair(binding.descriptionCard, "detailDescriptionStory"),
                        Pair(binding.titleCard, "latitude"),
                        Pair(binding.descriptionCard, "longitude")

                    )


                val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                intent.putExtras(bundle)
                startActivity(itemView.context, intent, optionsCompat.toBundle())
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


    }



}

