package jec.ac.jp.incense

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostAdapter(private val postList: MutableList<Post>) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    init {
        // 根據 timestamp 降序排序貼文
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
        holder.timestampTextView.text = post.getFormattedTimestamp()

        // 將用戶名稱設為可點擊，點擊後傳入該貼文作者的 UID 跳轉到 FavoritesActivity
        holder.usernameTextView.setOnClickListener { view ->
            val context = view.context
            val intent = Intent(context, FavoritesActivity::class.java)
            intent.putExtra("USER_ID", post.userId) // 假設 Post 中有 userId 屬性
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = postList.size

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameTextView: TextView = itemView.findViewById(R.id.usernameTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
        val incenseNameTextView: TextView = itemView.findViewById(R.id.incenseNameTextView)
        val timestampTextView: TextView = itemView.findViewById(R.id.timestampTextView)
    }
}
