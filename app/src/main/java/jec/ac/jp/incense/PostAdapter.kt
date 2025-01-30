package jec.ac.jp.incense

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostAdapter(private val postList: List<Post>) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]
        holder.usernameTextView.text = post.username
        holder.contentTextView.text = post.content
        holder.incenseNameTextView.setText(post.getIncenseName())
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var usernameTextView: TextView = itemView.findViewById<TextView>(R.id.usernameTextView)
        var contentTextView: TextView = itemView.findViewById<TextView>(R.id.contentTextView)
        var incenseNameTextView: TextView =
            itemView.findViewById<TextView>(R.id.incenseNameTextView)
    }
}