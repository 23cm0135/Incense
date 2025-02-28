package jec.ac.jp.incense

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class PostAdapter(private val postList: MutableList<Post>) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    init {
        postList.sortByDescending { it.timestamp }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]
        holder.usernameTextView.text = post.username
        holder.contentTextView.text = post.content
        holder.incenseNameTextView.text = post.incenseName
        holder.timestampTextView.text = post.getFormattedTimestamp() // **顯示時間**
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameTextView: TextView = itemView.findViewById(R.id.usernameTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
        val incenseNameTextView: TextView = itemView.findViewById(R.id.incenseNameTextView)
        val timestampTextView: TextView = itemView.findViewById(R.id.timestampTextView) // **時間**
    }
}
